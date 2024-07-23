package io.github.netbeans.mvnrunner.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.openide.modules.ModuleInfo;
import org.openide.util.Lookup;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ModuleUtils {

    private static final Map<String, ModuleInfo> MODULES = new HashMap<>();

    public static Optional<? extends ModuleInfo> getModule(String codeNameBase) {
        if (MODULES.containsKey(codeNameBase)) {
            return Optional.of(MODULES.get(codeNameBase));
        }
        Optional<? extends ModuleInfo> module = Lookup.getDefault()
                .lookupAll(ModuleInfo.class)
                .stream()
                .filter(m -> Objects.equals(m.getCodeNameBase(), codeNameBase))
                .findAny();
        if (module.isPresent()) {
            MODULES.put(codeNameBase, module.get());
        }
        return module;
    }

    @SneakyThrows
    public static <T> Class<T> loadClassFromModule(String codeNameBase, String className) {
        Optional<? extends ModuleInfo> moduleRef = getModule(codeNameBase);
        ModuleInfo module = moduleRef.orElseThrow();
        ClassLoader classLoader = module.getClassLoader();
        return (Class<T>) classLoader.loadClass(className);
    }
}
