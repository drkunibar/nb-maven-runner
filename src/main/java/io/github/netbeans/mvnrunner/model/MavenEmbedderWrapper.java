package io.github.netbeans.mvnrunner.model;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import lombok.SneakyThrows;

public class MavenEmbedderWrapper extends Wrapper {

    MavenEmbedderWrapper(Object mavenEmbedder) {
        super(mavenEmbedder);
    }

    @SneakyThrows
    public ArtifactRepositoryWrapper getLocalRepository() {
        return new ArtifactRepositoryWrapper(invokeSimple("getLocalRepository"));
    }

    @SneakyThrows
    public void resolve(ArtifactWrapper sources, List<ArtifactRepositoryWrapper> remoteRepositories,
            ArtifactRepositoryWrapper localRepository) throws ClassNotFoundException {
        ClassLoader classLoader = getUnwrapClass().getClassLoader();
        Class<?> artifactClass = classLoader.loadClass("org.apache.maven.artifact.Artifact");
        Class<?> artifactRepositoryClass
                = classLoader.loadClass("org.apache.maven.artifact.repository.ArtifactRepository");
        Method method = getUnwrapClass().getMethod("resolve", artifactClass, List.class, artifactRepositoryClass);
        List remoteRepos = remoteRepositories.stream().map(Wrapper::unwrap).collect(Collectors.toList());
        method.invoke(unwrap(), sources.unwrap(), remoteRepos, localRepository.unwrap());
    }

}
