package io.github.netbeans.mvnrunner.model;

import java.util.List;
import java.util.stream.Collectors;

public class MojoDescriptorWrapper extends Wrapper {

    public MojoDescriptorWrapper(Object wrappedObject) {
        super(wrappedObject);
    }

    public String getGoal() {
        return invokeSimple("getGoal");
    }

    public String getFullGoalName() {
        return invokeSimple("getFullGoalName");
    }

    public PlexusConfigurationWrapper getConfiguration() {
        Object o = invokeSimple("getConfiguration");
        return o == null ? null : new PlexusConfigurationWrapper(o);
    }

    public List<ParameterWrapper> getParameters() {
        List params = invokeSimple("getParameters");
        return (List<ParameterWrapper>) params.stream().map(ParameterWrapper::new).collect(Collectors.toList());
    }

    public String getExecuteLifecycle() {
        return invokeSimple("getExecuteLifecycle");
    }

    public String getExecutePhase() {
        return invokeSimple("getExecutePhase");
    }

    public String getId() {
        return invokeSimple("getId");
    }

    @Override
    public String toString() {
        // formatter:off
        return "MojoDescriptorWrapper{" + "id=" + getId() + ',' + "goal=" + getGoal() + ',' + "fullGoalName="
                + getFullGoalName() + ',' + "executePhase=" + getExecutePhase() + ',' + "executeLifecycle="
                + getExecuteLifecycle() + ',' + "parameter=" + getParameters() + '}';
        // formatter:on
    }

}
