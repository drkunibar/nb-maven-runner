package io.github.netbeans.mvnrunner.model;

import java.util.List;
import java.util.stream.Collectors;

public class PluginDescriptorWrapper extends Wrapper {

    public PluginDescriptorWrapper(Object wrappedObject) {
        super(wrappedObject);
    }

    public List<MojoDescriptorWrapper> getMojos() {
        List mds = invokeSimple("getMojos");
        return (List<MojoDescriptorWrapper>) mds.stream().map(MojoDescriptorWrapper::new).collect(Collectors.toList());
    }

    public String getGoalPrefix() {
        return invokeSimple("getGoalPrefix");
    }

}
