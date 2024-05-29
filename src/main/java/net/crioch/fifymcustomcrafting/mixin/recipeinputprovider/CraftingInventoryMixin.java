package net.crioch.fifymcustomcrafting.mixin.recipeinputprovider;

import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeInputProvider;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CraftingInventory.class)
public class CraftingInventoryMixin implements ComponentRecipeInputProvider {
    @Final
    @Shadow
    private DefaultedList<ItemStack> stacks;

    @Override
    public void provideComponentRecipeInputs(ComponentRecipeMatcher finder) {
        for (ItemStack itemStack : this.stacks) {
            finder.addUnenchantedInput(itemStack);
        }
    }
}
