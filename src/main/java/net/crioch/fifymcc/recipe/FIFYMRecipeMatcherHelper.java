package net.crioch.fifymcc.recipe;

import net.crioch.fifymcc.component.FIFYMDataComponentTypes;
import net.crioch.fifymcc.component.remainder.DamagedRemainder;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class FIFYMRecipeMatcherHelper {
    private static final Set<DataComponentType<?>> ALLOWED_COMPONENTS;

    static {
        ALLOWED_COMPONENTS = new HashSet<>();

        addAllowedComponent(DataComponentTypes.CUSTOM_NAME);
        addAllowedComponent(DataComponentTypes.CUSTOM_MODEL_DATA);
    }

    public static void addAllowedComponent(DataComponentType<?> type) {
        ALLOWED_COMPONENTS.add(type);
    }

    public static ComponentChanges removeIgnoredChanges(ItemStack stack) {
        return removeIgnoredChanges(stack, true);
    }

    public static ComponentChanges removeIgnoredChanges(ItemStack stack, boolean allowEnchantments) {
        ComponentChanges changes = stack.getComponentChanges();
        boolean allowDamageComponent;
        if (stack.get(FIFYMDataComponentTypes.RECIPE_REMAINDER) instanceof DamagedRemainder remainder) {
            allowDamageComponent = !remainder.canBreak() && stack.getDamage() >= stack.getMaxDamage();
        } else {
            allowDamageComponent = false;
        }
        return changes.withRemovedIf(dataComponentType ->
                !ALLOWED_COMPONENTS.contains(dataComponentType)
                        && (dataComponentType != DataComponentTypes.DAMAGE || !allowDamageComponent)
                        && (dataComponentType != DataComponentTypes.ENCHANTMENTS || !allowEnchantments)
        );
    }
}
