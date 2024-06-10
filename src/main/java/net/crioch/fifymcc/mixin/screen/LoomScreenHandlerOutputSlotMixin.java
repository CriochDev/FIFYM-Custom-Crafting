package net.crioch.fifymcc.mixin.screen;

import net.crioch.fifymcc.component.FIFYMDataComponentTypes;
import net.crioch.fifymcc.component.remainder.Remainder;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net/minecraft/screen/LoomScreenHandler$6")
public class LoomScreenHandlerOutputSlotMixin {

    @Redirect(method = "onTakeItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;takeStack(I)Lnet/minecraft/item/ItemStack;", ordinal = 1))
    private ItemStack dyeRespectsRemainderComponent(Slot instance, int amount) {
        ItemStack result = instance.getStack();
        Remainder remainder = result.get(FIFYMDataComponentTypes.RECIPE_REMAINDER);
        if (remainder != null) {
            result = remainder.getRemainder(result);
            instance.setStack(result);
        } else {
            result = instance.takeStack(amount);
        }
        return result;
    }
}
