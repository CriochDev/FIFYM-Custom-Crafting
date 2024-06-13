package net.crioch.fifymcc.component.remainder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.crioch.fifymcc.util.FIFYCodecs;
import net.crioch.fifymcc.util.Util;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ChanceRemainder extends RemainderWithSeed {
    public static final Identifier ID = Identifier.of(Util.MOD_ID, "chance");

    public static final MapCodec<ChanceRemainder> CODEC = (RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    FIFYCodecs.FLOAT_CHANCE.fieldOf("break_chance").forGetter(ChanceRemainder::chance),
                    Codec.LONG.optionalFieldOf("seed", 0L).forGetter(ChanceRemainder::getSeed)
            ).apply(instance, ChanceRemainder::new)
    ));
    private final float chance;
    public float chance() {
        return this.chance;
    }

    public ChanceRemainder(float chance, long seed) {
        super(RemainderTypes.BREAKABLE, seed);
        this.chance = chance;
    }

    @Override
    public ItemStack getRemainder(ItemStack stack, World world) {
        ItemStack remainder = stack.copy();
        if (!remainder.contains(DataComponentTypes.UNBREAKABLE)) {
            float value = this.random.nextFloat();
            if (this.chance > value)
                remainder.decrement(1);
        }
        return remainder;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ChanceRemainder && ((ChanceRemainder)object).chance == this.chance;
    }
}
