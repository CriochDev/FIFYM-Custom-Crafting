package net.crioch.fifymcc.component.remainder;

import com.mojang.serialization.MapCodec;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class Remainder {
    private final Type type;

    Remainder(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public ItemStack getRemainder(ItemStack stack) {
        return this.getRemainder(stack, null);
    }


    public ItemStack getRemainder(ItemStack stack, World world) {
        return ItemStack.EMPTY;
    }

    public record Type(MapCodec<? extends Remainder> codec) {
    }
}
