package net.crioch.fifymcustomcrafting.mixin.screenhandler;

import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeInputProvider;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeScreenHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractFurnaceScreenHandler.class)
public class AbstractFurnaceScreenHandlerMixin implements ComponentRecipeScreenHandler {

    @Final
    @Shadow
    private Inventory inventory;

    @Override
    public void populateComponentRecipeFinder(ComponentRecipeMatcher finder) {
        if (this.inventory instanceof ComponentRecipeInputProvider) {
            ((ComponentRecipeInputProvider)this.inventory).provideComponentRecipeInputs(finder);
        }
    }
}
