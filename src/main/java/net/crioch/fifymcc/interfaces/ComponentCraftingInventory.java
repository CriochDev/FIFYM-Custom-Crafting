package net.crioch.fifymcc.interfaces;

import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;

public interface ComponentCraftingInventory {
    default void provideComponentRecipeInputs(ComponentRecipeMatcher finder) {}
}
