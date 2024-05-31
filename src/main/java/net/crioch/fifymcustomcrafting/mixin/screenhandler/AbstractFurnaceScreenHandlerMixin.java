package net.crioch.fifymcustomcrafting.mixin.screenhandler;

import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeInputProvider;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeScreenHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractFurnaceScreenHandler.class)
public class AbstractFurnaceScreenHandlerMixin implements ComponentRecipeScreenHandler<Inventory> {
    @Final
    @Shadow
    private Inventory inventory;

    @Final
    @Shadow
    protected World world;

    @Override
    public void populateComponentRecipeFinder(ComponentRecipeMatcher finder) {
        if (this.inventory instanceof ComponentRecipeInputProvider) {
            ((ComponentRecipeInputProvider)this.inventory).provideComponentRecipeInputs(finder);
        }
    }

    public boolean matches(RecipeEntry<? extends Recipe<Inventory>> recipe) {
        return recipe.value().matches(this.inventory, this.world);
    }
}
