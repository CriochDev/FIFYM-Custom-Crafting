package net.crioch.fifymcustomcrafting.interfaces;

import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;

public interface ComponentPlayerInventory {
    default void populateComponentRecipeFinder(ComponentRecipeMatcher matcher) {
    }
}
