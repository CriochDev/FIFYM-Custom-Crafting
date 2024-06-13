package net.crioch.fifymcc.component;

import com.mojang.serialization.Codec;
import net.crioch.fifymcc.component.remainder.Remainder;
import net.crioch.fifymcc.registry.FIFYMRegistries;
import net.crioch.fifymcc.util.Util;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public class FIFYMDataComponentTypes {
    public static ComponentType<Integer> ENCHANTABILITY;
    public static ComponentType<Integer> FUEL_VALUE;
    public static ComponentType<Remainder> RECIPE_REMAINDER;

    public static void register() {
        ENCHANTABILITY = FIFYMDataComponentTypes.register("enchantability", builder -> builder.codec(Codecs.NONNEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT));
        FUEL_VALUE = FIFYMDataComponentTypes.register("fuel_value", builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));
        Codec<Remainder> remainderCodec = FIFYMRegistries.REMAINDER_TYPE_REGISTRY.getCodec().dispatch("type", Remainder::getType, Remainder.Type::codec);
        RECIPE_REMAINDER = FIFYMDataComponentTypes.register("recipe_remainder", builder -> builder.codec(remainderCodec));
    }

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Util.MOD_ID, id), (builderOperator.apply(ComponentType.builder())).build());
    }
}
