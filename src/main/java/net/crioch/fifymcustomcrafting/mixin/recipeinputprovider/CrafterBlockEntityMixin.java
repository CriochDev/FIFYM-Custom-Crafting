package net.crioch.fifymcustomcrafting.mixin.recipeinputprovider;

import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeInputProvider;
import net.minecraft.block.entity.CrafterBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CrafterBlockEntity.class)
public class CrafterBlockEntityMixin implements ComponentRecipeInputProvider {
    @Shadow
    private DefaultedList<ItemStack> inputStacks;

    @Override
    public void provideComponentRecipeInputs(ComponentRecipeMatcher finder) {
        for (ItemStack itemStack : this.inputStacks) {
            finder.addUnenchantedInput(itemStack);
        }
    }
}
