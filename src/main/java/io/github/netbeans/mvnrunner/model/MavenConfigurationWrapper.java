package io.github.netbeans.mvnrunner.model;

import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import lombok.SneakyThrows;

public class MavenConfigurationWrapper extends Wrapper {

    private MavenConfigurationWrapper(Object mavenConfiguration) {
        super(mavenConfiguration);
    }

    @Nullable
    public static MavenConfigurationWrapper getInstance(@Nullable Object mavenConfiguration) {
        return mavenConfiguration == null ? null : new MavenConfigurationWrapper(mavenConfiguration);
    }

    @SneakyThrows
    public List<String> getActivatedProfiles() {
        return invokeSimple("getActivatedProfiles");
    }

    @SneakyThrows
    public Map<String, String> getProperties() {
        return invokeSimple("getProperties");
    }
}
