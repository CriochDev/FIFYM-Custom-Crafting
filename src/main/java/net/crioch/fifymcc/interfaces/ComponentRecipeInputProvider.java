package net.crioch.fifymcc.interfaces;

import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;

public interface ComponentRecipeInputProvider {
    default void provideComponentRecipeInputs(ComponentRecipeMatcher finder) {
    }
}
