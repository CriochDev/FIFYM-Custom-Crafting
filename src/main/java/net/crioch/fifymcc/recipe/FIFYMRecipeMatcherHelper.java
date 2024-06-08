package net.crioch.fifymcc.recipe;

import net.crioch.fifymcc.component.FIFYDataComponentTypes;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;

import java.util.HashSet;
import java.util.Set;

public class FIFYMRecipeMatcherHelper {
    private static final Set<DataComponentType<?>> IGNORED_COMPONENTS;

    static {
        IGNORED_COMPONENTS = new HashSet<>();

        // Add default filtered components
        setIgnoredComponent(FIFYDataComponentTypes.RECIPE_REMAINDER);
        setIgnoredComponent(DataComponentTypes.UNBREAKABLE);
    }

    public static void setIgnoredComponent(DataComponentType<?> type) {
        IGNORED_COMPONENTS.add(type);
    }

    public static ComponentChanges removeIgnoredChanges(ComponentChanges changes) {

        return changes.withRemovedIf(IGNORED_COMPONENTS::contains);
    }
}
