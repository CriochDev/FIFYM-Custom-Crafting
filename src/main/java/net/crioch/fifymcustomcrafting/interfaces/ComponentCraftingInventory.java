package net.crioch.fifymcustomcrafting.interfaces;

import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;

public interface ComponentCraftingInventory {
    default void provideComponentRecipeInputs(ComponentRecipeMatcher finder) {}
}
