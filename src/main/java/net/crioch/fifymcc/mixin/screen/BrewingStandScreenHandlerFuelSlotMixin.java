package net.crioch.fifymcc.mixin.screen;

import net.crioch.fifymcc.component.FIFYMDataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.BrewingStandScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BrewingStandScreenHandler.FuelSlot.class)
public class BrewingStandScreenHandlerFuelSlotMixin {
    @Redirect(method = "matches", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private static boolean slotAcceptsItemsWithBSFuelComponent(ItemStack instance, Item item) {
        return instance.get(FIFYMDataComponentTypes.BREWING_STAND_FUEL) != null;
    }
}
