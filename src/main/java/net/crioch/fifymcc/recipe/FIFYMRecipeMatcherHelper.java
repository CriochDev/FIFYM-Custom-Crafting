package net.crioch.fifymcc.recipe;

import net.crioch.fifymcc.component.FIFYDataComponentTypes;
import net.crioch.fifymcc.component.remainder.DamageableRemainder;
import net.crioch.fifymcc.component.remainder.Remainder;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FIFYMRecipeMatcherHelper {
    private static final Set<DataComponentType<?>> IGNORED_COMPONENTS;

    static {
        IGNORED_COMPONENTS = new HashSet<>();

        // Add default filtered components
        setIgnoredComponent(FIFYDataComponentTypes.RECIPE_REMAINDER);
        setIgnoredComponent(DataComponentTypes.UNBREAKABLE);
        setIgnoredComponent(DataComponentTypes.MAX_STACK_SIZE);
        setIgnoredComponent(DataComponentTypes.MAX_DAMAGE);
        setIgnoredComponent(DataComponentTypes.REPAIR_COST);
    }

    public static void setIgnoredComponent(DataComponentType<?> type) {
        IGNORED_COMPONENTS.add(type);
    }

    public static ComponentChanges removeIgnoredChanges(ItemStack stack) {
        ComponentChanges changes = stack.getComponentChanges();
        if (stack.get(FIFYDataComponentTypes.RECIPE_REMAINDER) instanceof DamageableRemainder remainder) {
            if ((remainder.canBreak() || stack.getDamage() < stack.getMaxDamage())) {
                changes = changes.withRemovedIf(dataComponentType -> dataComponentType.equals(DataComponentTypes.DAMAGE));
            }
        }
        return changes.withRemovedIf(IGNORED_COMPONENTS::contains);
    }
}
