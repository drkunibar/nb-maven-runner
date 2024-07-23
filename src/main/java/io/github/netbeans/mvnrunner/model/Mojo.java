package io.github.netbeans.mvnrunner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeExclude;
import com.google.common.collect.ImmutableBiMap;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
// Inspired by Netbeans org.netbeans.modules.maven.execute.navigator.GoaPanel
public class Mojo implements Comparable<Mojo> {

    // @formatter:off
    public static final Map<String, Integer> DEFAULT_PHASES = ImmutableBiMap.<String, Integer>builder()
            .put("validate", 0)
            .put("initialize", 1)
            .put("generate-sources", 2)
            .put("process-sources", 4)
            .put("generate-resources", 5)
            .put("process-resources", 6)
            .put("compile", 7)
            .put("process-classes", 8)
            .put("generate-test-sources", 9)
            .put("process-test-sources", 10)
            .put("generate-test-resources", 11)
            .put("process-test-resources", 12)
            .put("test-compile", 13)
            .put("process-test-classes", 14)
            .put("test", 15)
            .put("prepare-package", 16)
            .put("package", 17)
            .put("pre-integration-test", 18)
            .put("integration-test", 19)
            .put("post-integration-test", 20)
            .put("verify", 21)
            .put("install", 22)
            .put("deploy", 23)
            .build();
    // @formatter:on

    @HashCodeExclude
    final ArtifactWrapper artifact;
    final String goal;
    final String phase;
    final String prefix;
    @HashCodeExclude
    final List<Param> parameters;
    @HashCodeExclude
    final boolean lifecycleBound;

    public Mojo(String prefix, String goal, String phase, ArtifactWrapper a, List<Param> parameters,
            boolean lifecycleBound) {
        this.artifact = a;
        this.goal = goal;
        this.phase = phase;
        this.prefix = prefix;
        this.parameters = parameters;
        this.lifecycleBound = lifecycleBound;
    }

    public List<Param> getNotSetParams() {
        List<Param> toRet = new ArrayList<>();
        for (Param p : parameters) {
            if (p.required && !p.parameterInModel && (p.property == null || !p.propertyInModel)) {
                toRet.add(p);
            }
        }
        return toRet;
    }

    @Override
    public int compareTo(Mojo o) {
        int res = prefix.compareTo(o.prefix);
        if (res == 0) {
            res = goal.compareTo(o.goal);
        }
        return res;
    }

    @Getter
    public static final class Param {

        private final String parameterName;
        private final boolean required;
        @Setter
        private String property;
        @Setter
        private String defValue;
        @Setter
        private boolean parameterInModel = false;
        @Setter
        private boolean propertyInModel = false;

        public Param(String parameterName, boolean required) {
            this.parameterName = parameterName;
            this.required = required;
        }

    }

    @Value
    public static class Phase {

        private final String name;
        @HashCodeExclude
        private final Set<Mojo> mojos;
    }
}
