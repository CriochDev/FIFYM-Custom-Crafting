package net.crioch.fifymcc.component.remainder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.crioch.fifymcc.interfaces.RandomGetSeed;
import net.crioch.fifymcc.util.FIFYCodecs;
import net.crioch.fifymcc.util.Util;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class BreakableRemainder extends RemainderWithSeed {
    public static final Identifier ID = new Identifier(Util.MOD_ID, "breakable_remainder");

    public static final MapCodec<BreakableRemainder> CODEC = (RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    FIFYCodecs.FLOAT_CHANCE.fieldOf("break_chance").forGetter(BreakableRemainder::chance),
                    Codec.LONG.optionalFieldOf("seed", 0L).forGetter(BreakableRemainder::getSeed)
            ).apply(instance, BreakableRemainder::new)
    ));
    private final float chance;
    public float chance() {
        return this.chance;
    }

    public BreakableRemainder(float chance, long seed) {
        super(RemainderTypes.BREAKABLE, seed);
        this.chance = chance;
    }

    @Override
    public ItemStack getRemainder(ItemStack stack) {
        ItemStack remainder = stack.copy();
        if (!remainder.contains(DataComponentTypes.UNBREAKABLE)) {
            float value = this.random.nextFloat();
            if (this.chance > value)
                remainder = super.getRemainder(stack);
        }
        return remainder;
    }
}
