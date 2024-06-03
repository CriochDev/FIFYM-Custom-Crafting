package net.crioch.fifymcc.mixin.recipeinputprovider;

import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.crioch.fifymcc.interfaces.ComponentRecipeInputProvider;
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
