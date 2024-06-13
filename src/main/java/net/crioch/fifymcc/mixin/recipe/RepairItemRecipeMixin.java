package net.crioch.fifymcc.mixin.recipe;

import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.*;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RepairItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeMixin {

    @ModifyReturnValue(method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;", at = @At(value = "RETURN", ordinal = 1))
    private ItemStack copyOverComponents(ItemStack result, @Local(ordinal = 0) ItemStack first) {
        ComponentChanges.Builder builder = ComponentChanges.builder();
        builder.add(DataComponentTypes.DAMAGE, result.get(DataComponentTypes.DAMAGE));
        builder.add(DataComponentTypes.ENCHANTMENTS, result.get(DataComponentTypes.ENCHANTMENTS));
        ComponentMap originals = first.getItem().getComponents();
        for (Component<?> component : first.getComponents()) {
            DataComponentType<?> type = component.type();
            if (originals.get(type) != component.value() && (type != DataComponentTypes.DAMAGE && type != DataComponentTypes.ENCHANTMENTS)) {
                builder.add(component);
            }
        }


        return new ItemStack(result.getItem().getRegistryEntry(), result.getCount(), builder.build());
    }

    @Redirect(method = "findPair", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RepairItemRecipe;canCombineStacks(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean checkComponentsAsNecessary(ItemStack first, ItemStack second) {
        boolean result = canCombineStacks(first, second);
        if (result) {
            ComponentChanges firstChanges = first.getComponentChanges().withRemovedIf(RepairItemRecipeMixin::isUncomparable);
            ComponentChanges secondChanges = second.getComponentChanges().withRemovedIf(RepairItemRecipeMixin::isUncomparable);
            result = Sets.difference(firstChanges.entrySet(), secondChanges.entrySet()).isEmpty();
        }
        return result;
    }

    @Shadow
    private static boolean canCombineStacks(ItemStack first, ItemStack second) {
        return false;
    }

    @Unique
    private static boolean isUncomparable(DataComponentType<?> type) {
        return type == DataComponentTypes.ENCHANTMENTS || type == DataComponentTypes.DAMAGE;
    }
}
