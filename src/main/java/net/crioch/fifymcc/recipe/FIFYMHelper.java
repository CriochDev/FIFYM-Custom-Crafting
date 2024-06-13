package net.crioch.fifymcc.recipe;

import com.google.common.collect.Sets;
import net.crioch.fifymcc.component.FIFYMDataComponentTypes;
import net.crioch.fifymcc.component.remainder.DamagedRemainder;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class FIFYMHelper {
    private static final Set<ComponentType<?>> BLACKLISTED_COMPONENTS;

    static {
        BLACKLISTED_COMPONENTS = new HashSet<>();

        addBlacklistedComponent(FIFYMDataComponentTypes.RECIPE_REMAINDER);
        addBlacklistedComponent(DataComponentTypes.UNBREAKABLE);
        addBlacklistedComponent(DataComponentTypes.LORE);
        addBlacklistedComponent(DataComponentTypes.CAN_PLACE_ON);
        addBlacklistedComponent(DataComponentTypes.CAN_BREAK);
        addBlacklistedComponent(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP);
        addBlacklistedComponent(DataComponentTypes.HIDE_TOOLTIP);
        addBlacklistedComponent(DataComponentTypes.DEBUG_STICK_STATE);
    }

    public static void addBlacklistedComponent(ComponentType<?> type) { BLACKLISTED_COMPONENTS.add(type); }

    public static ComponentChanges filterWithBlacklist(ItemStack stack) {
        return filterWithBlacklist(stack, true);
    }

    public static ComponentChanges filterWithBlacklist(ItemStack stack, boolean allowEnchantments) {
        ComponentChanges changes = stack.getComponentChanges();
        boolean allowDamageComponent;
        if (stack.get(FIFYMDataComponentTypes.RECIPE_REMAINDER) instanceof DamagedRemainder remainder) {
            allowDamageComponent = !remainder.canBreak() && stack.getDamage() >= stack.getMaxDamage();
        } else {
            allowDamageComponent = false;
        }

        return changes.withRemovedIf(ComponentType ->
                BLACKLISTED_COMPONENTS.contains(ComponentType)
                        || (ComponentType == DataComponentTypes.DAMAGE && !allowDamageComponent)
                        || (ComponentType == DataComponentTypes.ENCHANTMENTS && !allowEnchantments)
        );
    }

    public static boolean stacksAreExactlyEqual(ItemStack stack1, ItemStack stack2) {
        return stack1.isOf(stack2.getItem()) && Sets.difference(stack1.getComponentChanges().entrySet(), stack2.getComponentChanges().entrySet()).isEmpty();
    }
}
