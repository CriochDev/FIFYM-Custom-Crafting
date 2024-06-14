package net.crioch.fifymcc.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import net.crioch.fifymcc.component.remainder.RecipeRemainderWithWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Redirect(method = "getRemainingStacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Recipe;getRemainder(Lnet/minecraft/recipe/input/RecipeInput;)Lnet/minecraft/util/collection/DefaultedList;"))
    private DefaultedList<ItemStack> getRemainderWithWorldAccess(Recipe<?> recipe, RecipeInput input, @Local(argsOnly = true) World world) {
        return ((RecipeRemainderWithWorld<RecipeInput>)(Object)recipe).fIFYM_CustomCrafting$getRemainder(input, world);
    }
}
