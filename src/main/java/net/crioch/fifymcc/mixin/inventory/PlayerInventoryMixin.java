package net.crioch.fifymcc.mixin.inventory;

import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Redirect(method = "indexOf", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isDamaged()Z"))
    private boolean makeIsDamagedAlwaysFalse(ItemStack instance) {
        return instance.getOrDefault(DataComponentTypes.MAX_STACK_SIZE, 64) != 1 && instance.isDamaged();
    }
}
