package net.crioch.fifymcc.component.remainder;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public interface RecipeRemainderWithWorld<T extends RecipeInput> {
    DefaultedList<ItemStack> fIFYM_CustomCrafting$getRemainder(T input, World world);
}
