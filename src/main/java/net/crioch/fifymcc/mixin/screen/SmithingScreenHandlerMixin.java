package net.crioch.fifymcc.mixin.screen;

import com.llamalad7.mixinextras.sugar.Local;
import net.crioch.fifymcc.component.FIFYMDataComponentTypes;
import net.crioch.fifymcc.component.remainder.Remainder;
import net.crioch.fifymcc.interfaces.ForgingScreenHandlerGetters;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SmithingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SmithingScreenHandler.class)
public class SmithingScreenHandlerMixin {
    @Unique
    private final ForgingScreenHandlerGetters forgingScreen = (ForgingScreenHandlerGetters)this;

    @Redirect(method = "decrementStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    private boolean useRemainderComponentIfAvailable(ItemStack instance, @Local(argsOnly = true) int slot) {
        Remainder remainder = instance.get(FIFYMDataComponentTypes.RECIPE_REMAINDER);
        if (remainder != null && slot != 1) {
            forgingScreen.fIFYM_CustomCrafting$getInventory().setStack(slot, remainder.getRemainder(instance));
            return true;
        }

        return instance.isEmpty();
    }
}
