package net.crioch.fifymcc.component.remainder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.crioch.fifymcc.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class UnconsumedRemainder extends Remainder {
    public static final Identifier ID = new Identifier(Util.MOD_ID, "unconsumed_remainder");

    public static MapCodec<UnconsumedRemainder> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.BYTE.optionalFieldOf("", (byte) 0).forGetter(remainder -> (byte)0)
            ).apply(instance, value -> new UnconsumedRemainder())
    );

    UnconsumedRemainder() {
        super(RemainderTypes.NON_CONSUMABLE);
    }

    @Override
    public ItemStack getRemainder(ItemStack stack) {
        return stack.copy();
    }
}
