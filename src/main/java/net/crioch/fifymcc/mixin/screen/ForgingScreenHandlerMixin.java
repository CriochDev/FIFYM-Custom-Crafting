package net.crioch.fifymcc.mixin.screen;

import net.crioch.fifymcc.interfaces.ForgingScreenHandlerGetters;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ForgingScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ForgingScreenHandler.class)
public class ForgingScreenHandlerMixin implements ForgingScreenHandlerGetters {
    @Shadow
    @Final
    protected Inventory input;

    @Override
    public Inventory fIFYM_CustomCrafting$getInventory() {
        return this.input;
    }
}
