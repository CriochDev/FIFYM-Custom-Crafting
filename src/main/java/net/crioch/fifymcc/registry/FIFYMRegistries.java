package net.crioch.fifymcc.registry;

import com.mojang.serialization.Lifecycle;
import net.crioch.fifymcc.component.remainder.Remainder;
import net.crioch.fifymcc.util.Util;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class FIFYMRegistries {
    public static final Registry<Remainder.Type> REMAINDER_TYPE_REGISTRY = new SimpleRegistry<>(RegistryKey.ofRegistry(Identifier.of(Util.MOD_ID, "remainder_types")), Lifecycle.stable());
}
