package net.crioch.fifymcustomcrafting.mixin.recipeinputprovider;

import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeInputProvider;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin implements ComponentRecipeInputProvider {
    @Shadow
    protected DefaultedList<ItemStack> inventory;

    @Override
    public void provideComponentRecipeInputs(ComponentRecipeMatcher finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.addInput(itemStack);
        }
    }
}
