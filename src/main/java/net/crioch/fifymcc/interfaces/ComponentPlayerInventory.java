package net.crioch.fifymcc.interfaces;

import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;

public interface ComponentPlayerInventory {
    default void populateComponentRecipeFinder(ComponentRecipeMatcher matcher) {
    }
}
