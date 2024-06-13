package net.crioch.fifymcc.component.remainder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.crioch.fifymcc.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class UnconsumedRemainder extends Remainder {
    public static final Identifier ID = Identifier.of(Util.MOD_ID, "unconsumed");

    public static MapCodec<UnconsumedRemainder> CODEC = MapCodec.unit(UnconsumedRemainder::new);

    UnconsumedRemainder() {
        super(RemainderTypes.NON_CONSUMABLE);
    }

    @Override
    public ItemStack getRemainder(ItemStack stack, World world) {
        return stack.copy();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof UnconsumedRemainder;
    }
}
