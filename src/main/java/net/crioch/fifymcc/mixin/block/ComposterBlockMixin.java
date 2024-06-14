package net.crioch.fifymcc.mixin.block;

import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.crioch.fifymcc.component.FIFYMDataComponentTypes;
import net.crioch.fifymcc.component.remainder.Remainder;
import net.crioch.fifymcc.component.remainder.StackRemainder;
import net.crioch.fifymcc.recipe.FIFYMHelper;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {
    @Redirect(method = "onUseWithItem", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z", remap = false))
    private boolean checkForCompostComponent(Object2FloatMap instance, Object object, @Local(argsOnly = true) ItemStack stack) {
        return stack.getOrDefault(FIFYMDataComponentTypes.COMPOST_CHANCE, 0.0f) > 0.0f;
    }

    @Redirect(method = "onUseWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V"))
    private void applyRemainderComponent(ItemStack instance, int amount, LivingEntity entity) {
        if (entity != null && !entity.isInCreativeMode()) {
            Remainder remainder = instance.get(FIFYMDataComponentTypes.RECIPE_REMAINDER);
            ItemStack result = remainder != null ? remainder.getRemainder(instance) : null;
            PlayerInventory inventory = ((PlayerEntity)entity).getInventory();

            if (remainder instanceof StackRemainder) {
                if (result != null) {
                    if (!FIFYMHelper.stacksAreExactlyEqual(instance, result)) {
                        inventory.offerOrDrop(result);
                    }
                } else {
                    instance.decrement(amount);
                }
            } else if (remainder != null) {
                int slot = inventory.getSlotWithStack(instance);
                inventory.setStack(slot, result);
            } else {
                instance.decrement(amount);
            }

        }
    }

    @Redirect(method = "addToComposter", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;getFloat(Ljava/lang/Object;)F", remap = false))
    private static float retrieveCompostComponentValue(Object2FloatMap instance, Object object, @Local(argsOnly = true) ItemStack stack) {
        return stack.getOrDefault(FIFYMDataComponentTypes.COMPOST_CHANCE, 0.0f);
    }
}
