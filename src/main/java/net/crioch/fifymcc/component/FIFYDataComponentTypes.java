package net.crioch.fifymcc.component;

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
    public static DataComponentType<RecipeRemainder> RECIPE_REMAINDER;

    public static void register() {
        ENCHANTABILITY = FIFYDataComponentTypes.register("enchantability", builder -> builder.codec(Codecs.NONNEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT));
        FUEL_VALUE = FIFYDataComponentTypes.register("fuel_value", builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));
        RECIPE_REMAINDER = FIFYDataComponentTypes.register("recipe_remainder", builder -> builder.codec(RecipeRemainder.CODEC).packetCodec(RecipeRemainder.PACKET_CODEC));
    }

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, String.format("%s:%s", Util.MOD_ID, id), (builderOperator.apply(DataComponentType.builder())).build());
    }
}
