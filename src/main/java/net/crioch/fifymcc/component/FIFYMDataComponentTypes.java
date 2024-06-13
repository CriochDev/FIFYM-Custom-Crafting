package net.crioch.fifymcc.component;

import com.mojang.serialization.Codec;
import net.crioch.fifymcc.component.remainder.Remainder;
import net.crioch.fifymcc.registry.FIFYMRegistries;
import net.crioch.fifymcc.util.FIFYMCodecs;
import net.crioch.fifymcc.util.Util;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

import javax.sound.sampled.Port;

public class FIFYMDataComponentTypes {
    public static ComponentType<Integer> BREWING_STAND_FUEL;
    public static ComponentType<Float> COMPOST_CHANCE;
    public static ComponentType<Integer> ENCHANTABILITY;
    public static ComponentType<Integer> FUEL_VALUE;
    public static ComponentType<Remainder> RECIPE_REMAINDER;

    public static void register() {
        // Simple components
        BREWING_STAND_FUEL = FIFYMDataComponentTypes.register("brewing_fuel", builder -> builder.codec(Codecs.rangedInt(1, 20)).packetCodec(PacketCodecs.VAR_INT));
        COMPOST_CHANCE = FIFYMDataComponentTypes.register("compost_chance", builder -> builder.codec(FIFYMCodecs.FLOAT_CHANCE).packetCodec(PacketCodecs.FLOAT));
        ENCHANTABILITY = FIFYMDataComponentTypes.register("enchantability", builder -> builder.codec(Codecs.NONNEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT));
        FUEL_VALUE = FIFYMDataComponentTypes.register("fuel_value", builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));

        // Complex components
        Codec<Remainder> remainderCodec = FIFYMRegistries.REMAINDER_TYPE_REGISTRY.getCodec().dispatch("type", Remainder::getType, Remainder.Type::codec);
        RECIPE_REMAINDER = FIFYMDataComponentTypes.register("recipe_remainder", builder -> builder.codec(remainderCodec));
    }

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Util.MOD_ID, id), (builderOperator.apply(ComponentType.builder())).build());
    }
}
