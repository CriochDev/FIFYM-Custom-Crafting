package net.crioch.fifymcustomcrafting.interfaces;

import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;

public interface ComponentRecipeInputProvider {
    default void provideComponentRecipeInputs(ComponentRecipeMatcher finder) {
    }
}
