package io.github.netbeans.mvnrunner.util;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Action;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ActionProvider;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import io.github.netbeans.mvnrunner.model.ArtifactWrapper;
import io.github.netbeans.mvnrunner.model.EmbedderFactoryWrapper;
import io.github.netbeans.mvnrunner.model.MavenEmbedderWrapper;
import io.github.netbeans.mvnrunner.model.MavenProjectWrapper;
import io.github.netbeans.mvnrunner.model.Mojo;
import io.github.netbeans.mvnrunner.model.Mojo.Phase;
import io.github.netbeans.mvnrunner.model.MojoDescriptorWrapper;
import io.github.netbeans.mvnrunner.model.NetbeansActionMappingWrapper;
import io.github.netbeans.mvnrunner.model.ParameterWrapper;
import io.github.netbeans.mvnrunner.model.PlexusConfigurationWrapper;
import io.github.netbeans.mvnrunner.model.PluginDescriptorBuilderWrapper;
import io.github.netbeans.mvnrunner.model.PluginDescriptorWrapper;
import io.github.netbeans.mvnrunner.model.PluginExecutionWrapper;
import io.github.netbeans.mvnrunner.model.PluginWrapper;
import io.github.netbeans.mvnrunner.model.Xpp3DomWrapper;

@UtilityClass
@Slf4j
// @formatter:off
@Messages({
    "# {0} - artifact id",
    "MSG_missingLocalPluginArtifact=Plugin artifact {0} does not resolve to a local file",
    "# {0} - mojo file",
    "MSG_missingMojo=no mojos in {0}",
    "# {0} - mojo file",
    "MSG_missingGoalPrefix=no goalPrefix in {0}",
    "# {0} - mojo file",
    "MSG_missingGoal=mojo missing goal in {0}",
    "# {0} - ctro types",
    "# {1} - classname",
    "MSG_missingCtor=Missing constract with arguments {0} in class {1}"
})
// @formatter:on
public class MavenProjectUtils {

    @SneakyThrows
    public static Set<Mojo> getGoals(Collection<ArtifactWrapper> plugins, MavenProjectWrapper mavenProject) {
        return plugins.stream().flatMap(plugin -> getGoals(plugin, mavenProject).stream()).collect(Collectors.toSet());
    }

    // Inspired by Netbeans org.netbeans.modules.maven.execute.navigator.GoaPanel
    @SneakyThrows
    @SuppressWarnings("UseSpecificCatch")
    public static Set<Mojo> getGoals(ArtifactWrapper artifact, MavenProjectWrapper mavenProject) {
        Set<Mojo> goals = new TreeSet<>();
        try {
            MavenEmbedderWrapper onlineEmbedder = EmbedderFactoryWrapper.getOnlineEmbedder();
            onlineEmbedder.resolve(artifact, mavenProject.getPluginArtifactRepositories(),
                    onlineEmbedder.getLocalRepository());
            if (artifact.getFile() == null) {
                log.warn(Bundle.MSG_missingLocalPluginArtifact(artifact.getArtifactId()));
                return goals;
            }
            PluginDescriptorWrapper pd = loadPluginDescriptor(artifact.getFile());
            if (pd == null) {
                return goals;
            }
            List<MojoDescriptorWrapper> mojos = pd.getMojos();
            if (mojos == null) {
                log.warn(Bundle.MSG_missingMojo(artifact.getFile()));
                return goals;
            }
            String goalPrefix = pd.getGoalPrefix();
            if (goalPrefix == null) {
                log.warn(Bundle.MSG_missingGoalPrefix(artifact.getFile()));
                return goals;
            }
            for (MojoDescriptorWrapper mojo : mojos) {
                String goal = mojo.getGoal();
                if (goal == null) {
                    log.warn(Bundle.MSG_missingGoal(artifact.getFile()));
                    continue;
                }
                List<Mojo.Param> params = getParameters(mojo);
                PlexusConfigurationWrapper config = mojo.getConfiguration();
                configMojoParam(config, params, artifact, mavenProject);
                PluginWrapper plg = mavenProject
                        .getPlugin(PluginWrapper.constructKey(artifact.getGroupId(), artifact.getArtifactId()));
                boolean lifecycleBound = false;
                List<PluginExecutionWrapper> execs = plg.getExecutions();
                if (execs != null) {
                    for (PluginExecutionWrapper exec : execs) {
                        List<String> execGoals = exec.getGoals();
                        if (execGoals.contains(goal)) {
                            lifecycleBound = true;
                            break;
                        }
                    }
                }
                goals.add(new Mojo(goalPrefix, goal, null, artifact, params, lifecycleBound));
            }
        } catch (Exception ex) {
            String exClassName = ClassUtils.getSimpleName(ex.getClass());
            if (Objects.equals(exClassName, "ArtifactResolutionException")
                    || Objects.equals(exClassName, "ArtifactNotFoundException")) {
                log.debug("Plugin artifact {0} cannot be resolved");
                log.debug("Artifact resoltion error", ex);
            } else {
                throw ex;
            }
        }
        return goals;
    }

    @SneakyThrows
    @SuppressWarnings("UseSpecificCatch")
    public static Collection<Phase> getLifecycle(MavenProjectWrapper mavenProject) {
        Map<String, Set<Mojo>> phases = Mojo.DEFAULT_PHASES.keySet()
                .stream()
                .collect(Collectors.toMap(p -> p, p -> new LinkedHashSet<>(), (v1, v2) -> v1, LinkedHashMap::new));
        try {
            MavenEmbedderWrapper onlineEmbedder = EmbedderFactoryWrapper.getOnlineEmbedder();
            Collection<ArtifactWrapper> pluginArtifacts = mavenProject.getPluginArtifacts();
            for (ArtifactWrapper artifact : pluginArtifacts) {
                onlineEmbedder.resolve(artifact, mavenProject.getPluginArtifactRepositories(),
                        onlineEmbedder.getLocalRepository());
                if (artifact.getFile() == null) {
                    log.warn(Bundle.MSG_missingLocalPluginArtifact(artifact.getArtifactId()));
                    return Collections.emptyList();
                }
                PluginDescriptorWrapper pd = loadPluginDescriptor(artifact.getFile());
                if (pd == null) {
                    return Collections.emptyList();
                }
                List<MojoDescriptorWrapper> mojos = pd.getMojos();
                if (mojos == null) {
                    log.warn(Bundle.MSG_missingMojo(artifact.getFile()));
                    return Collections.emptyList();
                }
                String goalPrefix = pd.getGoalPrefix();
                if (goalPrefix == null) {
                    log.warn(Bundle.MSG_missingGoalPrefix(artifact.getFile()));
                    return Collections.emptyList();
                }
                for (MojoDescriptorWrapper mojo : mojos) {
                    String goal = mojo.getGoal();
                    if (goal == null) {
                        log.warn(Bundle.MSG_missingGoal(artifact.getFile()));
                        continue;
                    }
                    List<Mojo.Param> params = getParameters(mojo);
                    PlexusConfigurationWrapper config = mojo.getConfiguration();
                    configMojoParam(config, params, artifact, mavenProject);
                    PluginWrapper plg = mavenProject
                            .getPlugin(PluginWrapper.constructKey(artifact.getGroupId(), artifact.getArtifactId()));
                    boolean lifecycleBound = false;
                    List<PluginExecutionWrapper> execs = plg.getExecutions();
                    if (execs != null) {
                        for (PluginExecutionWrapper exec : execs) {
                            String phase = exec.getPhase();
                            List<String> execGoals = exec.getGoals();
                            if (phase == null || !execGoals.contains(goal)) {
                                continue;
                            }
                            Set<Mojo> mojoList = phases.get(phase);
                            if (mojoList == null) {
                                log.warn("Unknow phase {} in mojo {}", phase, mojo.getId());
                                break;
                            }
                            mojoList.add(new Mojo(goalPrefix, goal, null, artifact, params, lifecycleBound));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            String exClassName = ClassUtils.getSimpleName(ex.getClass());
            if (Objects.equals(exClassName, "ArtifactResolutionException")
                    || Objects.equals(exClassName, "ArtifactNotFoundException")) {
                log.debug("Plugin artifact {0} cannot be resolved");
                log.debug("Artifact resoltion error", ex);
            } else {
                throw ex;
            }
        }
        return phases.entrySet()
                .stream()
                .filter(e -> !e.getValue().isEmpty())
                .map(e -> new Phase(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private List<Mojo.Param> getParameters(MojoDescriptorWrapper mojo) {
        List<ParameterWrapper> parameters = mojo.getParameters();
        if (parameters == null) {
            return Collections.emptyList();
        }
        return parameters.stream()
                .filter(p -> p.getName() != null)
                .filter(p -> p.isEditable())
                .map(p -> new Mojo.Param(p.getName(), p.isRequired()))
                .collect(Collectors.toList());
    }

    private void configMojoParam(PlexusConfigurationWrapper config, Collection<Mojo.Param> params, ArtifactWrapper p,
            MavenProjectWrapper mp) {
        if (config == null) {
            return;
        }
        for (Mojo.Param par : params) {
            PlexusConfigurationWrapper pconfEl = config.getChild(par.getParameterName());
            if (pconfEl != null) {
                String attr = pconfEl.getAttribute("default-value");
                if (attr != null) {
                    par.setDefValue(attr);
                }
                String val = pconfEl.getValue();
                if (val != null && val.startsWith("${") && val.endsWith("}")) {
                    par.setProperty(val.substring(2, val.length() - 1));
                }
            }
            PluginWrapper pl = mp.getPlugin(PluginWrapper.constructKey(p.getGroupId(), p.getArtifactId()));
            if (pl != null) {
                Xpp3DomWrapper c = pl.getConfiguration();
                if (c != null) {
                    par.setParameterInModel(c.getChild(par.getParameterName()) != null);
                }
            }
            if (par.getProperty() != null) {
                Properties properties = mp.getProperties();
                par.setPropertyInModel(properties.getProperty(par.getProperty()) != null);
            }
        }
    }

    @SneakyThrows
    @CheckForNull
    private static PluginDescriptorWrapper loadPluginDescriptor(File jar) {
        if (!jar.isFile() || !StringUtils.endsWith(jar.getName(), ".jar")) {
            return null;
        }
        log.debug("parsing plugin.xml from {0}", jar);
        URL uri = new URL("jar:" + Utilities.toURI(jar) + "!/META-INF/maven/plugin.xml");
        InputStream openStream = uri.openStream();
        InputStreamReader reader = new InputStreamReader(openStream);
        return new PluginDescriptorBuilderWrapper().build(reader);
    }

    @SneakyThrows
    public static NetbeansActionMappingWrapper[] getActiveCustomMappings(@Nonnull Project project) {
        Class<?> actionToGoalUtilsClass = ModuleUtils.loadClassFromModule("org.netbeans.modules.maven",
                "org.netbeans.modules.maven.execute.ActionToGoalUtils"); // NOI18N
        Method getActiveCustomMappingsMethod = MethodUtils.getAccessibleMethod(actionToGoalUtilsClass,
                "getActiveCustomMappings", project.getClass());
        Object[] netbeansActionMappingWrappers = (Object[]) getActiveCustomMappingsMethod.invoke(null, project);
        NetbeansActionMappingWrapper[] result = new NetbeansActionMappingWrapper[netbeansActionMappingWrappers.length];
        for (int i = 0; i < netbeansActionMappingWrappers.length; i++) {
            result[i] = NetbeansActionMappingWrapper.wrap(netbeansActionMappingWrappers[i]);
        }
        return result;
    }

    @SneakyThrows
    public static Action createCustomMavenAction(String name, NetbeansActionMappingWrapper mapping, boolean showUI,
            Lookup context, Project project) {
        ActionProvider actionProvider = project.getLookup().lookup(ActionProvider.class);
        Method m = MethodUtils.getAccessibleMethod(actionProvider.getClass(), "createCustomMavenAction", String.class,
                mapping.getUnwrapClass(), boolean.class, Lookup.class, Project.class);
        return (Action) m.invoke(null, name, mapping.unwrap(), showUI, context, project);
    }

    @SneakyThrows
    @Nullable
    public static <T> T createInstance(ClassLoader classLoader, String className,
            Map<Class<?>, Object> constructorParams, Map<String, Object> fieldValues) {
        Class<?> cls = classLoader.loadClass(className);
        Constructor<?> constructor = cls.getConstructor(constructorParams.keySet().toArray(Class<?>[]::new));
        if (constructor == null) {
            log.warn(Bundle.MSG_missingCtor(constructorParams.keySet(), className));
            return null;
        }
        T result = (T) constructor.newInstance(constructorParams.values().toArray(Object[]::new));
        for (Entry<String, Object> fieldValue : fieldValues.entrySet()) {
            Field field = FieldUtils.getField(cls, fieldValue.getKey(), true);
            field.set(result, fieldValue.getValue());
        }
        return result;
    }

    public static NetbeansActionMappingWrapper createNetbeansActionMapping(Project project, Phase phase) {
        Properties properties = new Properties();
        Map<String, Object> fieldValues = new HashMap<>();
        fieldValues.put("properties", properties);
        fieldValues.put("goals", Collections.singletonList(phase.getName()));
        return new NetbeansActionMappingWrapper(MavenProjectUtils.createNetbeansActionMapping(project, fieldValues));
    }

    public static NetbeansActionMappingWrapper createNetbeansActionMapping(Project project, Mojo mojo) {
        Properties properties = new Properties();
        for (Mojo.Param p : mojo.getNotSetParams()) {
            if (p.getProperty() != null) {
                properties.put(p.getProperty(), "");
            }
        }
        Map<String, Object> fieldValues = new HashMap<>();
        fieldValues.put("properties", properties);
        fieldValues.put("goals", Collections.singletonList(mojo.getGoal()));
        return new NetbeansActionMappingWrapper(MavenProjectUtils.createNetbeansActionMapping(project, fieldValues));
    }

    public static <T> T createNetbeansActionMapping(Project project, Map<String, Object> fieldValues) {
        ActionProvider actionProvider = project.getLookup().lookup(ActionProvider.class);
        ClassLoader classLoader = actionProvider.getClass().getClassLoader();
        return createInstance(classLoader, "org.netbeans.modules.maven.execute.model.NetbeansActionMapping",
                Collections.emptyMap(), fieldValues);
    }
}
