package net.crioch.fifymcc.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.crioch.fifymcc.component.remainder.Remainder;
import net.crioch.fifymcc.registry.FIFYMRegistries;
import net.crioch.fifymcc.util.Util;
import net.minecraft.component.DataComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class FIFYDataComponentTypes {
    public static DataComponentType<Integer> ENCHANTABILITY;
    public static DataComponentType<Integer> FUEL_VALUE;
    public static DataComponentType<Remainder> RECIPE_REMAINDER;

    public static void register() {
        ENCHANTABILITY = FIFYDataComponentTypes.register("enchantability", builder -> builder.codec(Codecs.NONNEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT));
        FUEL_VALUE = FIFYDataComponentTypes.register("fuel_value", builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));
        // HERE
        Codec<Remainder> remainderCodec = FIFYMRegistries.REMAINDER_TYPE_REGISTRY.getCodec().dispatch("type", Remainder::getType, Remainder.Type::codec);
        RECIPE_REMAINDER = FIFYDataComponentTypes.register("recipe_remainder", builder -> builder.codec(remainderCodec));
    }

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(Util.MOD_ID, id), (builderOperator.apply(DataComponentType.builder())).build());
    }
}
