package net.crioch.fifymcc.mixin.enchantment;

import com.llamalad7.mixinextras.sugar.Local;
import net.crioch.fifymcc.components.FIFYDataComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Redirect(method = "calculateRequiredExperienceLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getEnchantability()I"))
    private static int getEnchantability(Item instance, @Local(argsOnly = true) ItemStack stack) {
        return getActualEnchantability(stack);
    }

    @Redirect(method = "generateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getEnchantability()I"))
    private static int generateEnchantments(Item instance, @Local(argsOnly = true) ItemStack stack) {
        return getActualEnchantability(stack);
    }

    @Unique
    private static int getActualEnchantability(ItemStack stack) {
        return stack.getComponents().getOrDefault(FIFYDataComponentTypes.ENCHANTABILITY, 0);
    }
}
