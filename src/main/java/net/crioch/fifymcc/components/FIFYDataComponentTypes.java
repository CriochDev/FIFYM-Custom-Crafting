package net.crioch.fifymcc.components;

import net.crioch.fifymcc.util.Util;
import net.minecraft.component.DataComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public class FIFYDataComponentTypes {
    public static DataComponentType<Integer> ENCHANTABILITY;
    public static DataComponentType<Integer> FUEL_VALUE;

    public static void register() {
        ENCHANTABILITY = FIFYDataComponentTypes.register("enchantability", builder -> builder.codec(Codecs.NONNEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT));
        FUEL_VALUE = FIFYDataComponentTypes.register("fuel_value", builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));
    }

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, String.format("%s:%s", Util.MOD_ID, id), (builderOperator.apply(DataComponentType.builder())).build());
    }
}
