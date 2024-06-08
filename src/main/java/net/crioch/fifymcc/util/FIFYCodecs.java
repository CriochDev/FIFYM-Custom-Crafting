package net.crioch.fifymcc.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.function.Function;

public class FIFYCodecs {
    public static final Codec<Float> FLOAT_CHANCE = rangedFloat(0, 1, value -> "value must be between 0 and 1, excluding zero");

    public static Codec<Float> rangedFloat(float min, float max, Function<Float, String> messageFactory) {
        return Codec.FLOAT.validate((value) -> {
            return value > min && value <= max ? DataResult.success(value) : DataResult.error(() -> {
                return messageFactory.apply(value);
            });
        });
    }
}
