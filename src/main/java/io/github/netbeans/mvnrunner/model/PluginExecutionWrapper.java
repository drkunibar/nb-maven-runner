package io.github.netbeans.mvnrunner.model;

import java.util.List;

public class PluginExecutionWrapper extends Wrapper {

    public PluginExecutionWrapper(Object wrappedObject) {
        super(wrappedObject);
    }

    public List<String> getGoals() {
        return invokeSimple("getGoals");
    }

    public String getId() {
        return invokeSimple("getId");
    }

    public String getPhase() {
        return invokeSimple("getPhase");
    }

    public int getPriority() {
        return invokeSimple("getPriority");
    }

    @Override
    public String toString() {
        // @formatter:off
        return "PluginExecutionWrapper{"
                + "id=" + getId() + ','
                + "priority=" + getPriority() + ','
                + "phase=" + getPhase() + ','
                + "goals=" + getGoals() + ','
                + '}';
        // @formatter:on
    }

}
