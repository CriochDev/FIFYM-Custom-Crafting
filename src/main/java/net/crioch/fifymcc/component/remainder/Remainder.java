package net.crioch.fifymcc.component.remainder;

import com.mojang.serialization.MapCodec;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;

public class Remainder {
    private final Type type;
    protected static final Random random = Random.create();

    Remainder(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public ItemStack getRemainder(ItemStack stack) {
        return ItemStack.EMPTY;
    }

    public record Type(MapCodec<? extends Remainder> codec) {
    }
}
