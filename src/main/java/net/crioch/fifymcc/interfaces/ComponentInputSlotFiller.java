package net.crioch.fifymcc.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;

import java.util.Iterator;

public interface ComponentInputSlotFiller {

    default void fIFYM_CustomCrafting$alignComponentRecipeToGrid(int gridWidth, int gridHeight, int gridOutputSlot, RecipeEntry<?> recipe, Iterator<ItemStack> inputs, int amount) {

    }
}
