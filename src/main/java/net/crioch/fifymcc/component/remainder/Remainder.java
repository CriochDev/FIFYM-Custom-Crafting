package net.crioch.fifymcc.component.remainder;

import com.mojang.serialization.MapCodec;
import net.minecraft.item.ItemStack;

public abstract class Remainder {
    private final Type type;

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
