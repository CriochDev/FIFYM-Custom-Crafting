package net.crioch.fifymcustomcrafting.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;

import java.util.Iterator;

public interface ComponentInputSlotFiller {

    default void  alignComponentRecipeToGrid(int gridWidth, int gridHeight, int gridOutputSlot, RecipeEntry<?> recipe, Iterator<ItemStack> inputs, int amount) {

    }
}
