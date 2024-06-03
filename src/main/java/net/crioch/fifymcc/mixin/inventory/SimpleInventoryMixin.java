package net.crioch.fifymcc.mixin.inventory;

import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.crioch.fifymcc.interfaces.ComponentRecipeInputProvider;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SimpleInventory.class)
public class SimpleInventoryMixin implements ComponentRecipeInputProvider {
    @Final
    @Shadow
    public DefaultedList<ItemStack> heldStacks;

    @Override
    public void provideComponentRecipeInputs(ComponentRecipeMatcher finder) {
        for (ItemStack itemStack : this.heldStacks) {
            finder.addInput(itemStack);
        }
    }
}
