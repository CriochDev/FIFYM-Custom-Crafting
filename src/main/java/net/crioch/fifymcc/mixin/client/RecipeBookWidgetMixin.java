package net.crioch.fifymcc.mixin.client;

import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.crioch.fifymcc.interfaces.ComponentPlayerInventory;
import net.crioch.fifymcc.interfaces.ComponentRecipeResultCollection;
import net.crioch.fifymcc.interfaces.ComponentRecipeScreenHandler;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Consumer;

@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetMixin {
    @Shadow
    private ClientRecipeBook recipeBook;
    @Shadow
    protected AbstractRecipeScreenHandler<?> craftingScreenHandler;

    @Unique
    private final ComponentRecipeMatcher matcher = new ComponentRecipeMatcher();

    @Redirect(method = "reset", at = @At(value="INVOKE", target = "Lnet/minecraft/recipe/RecipeMatcher;clear()V"))
    private void clearMatcher(RecipeMatcher instance) {
        this.matcher.clear();
    }

    @Redirect(method = "reset", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/AbstractRecipeScreenHandler;populateRecipeFinder(Lnet/minecraft/recipe/RecipeMatcher;)V"))
    private void populateFromScreen(AbstractRecipeScreenHandler instance, RecipeMatcher recipeMatcher) {
        ((ComponentRecipeScreenHandler)instance).populateComponentRecipeFinder(this.matcher);
    }

    @Redirect(method = "reset", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;populateRecipeFinder(Lnet/minecraft/recipe/RecipeMatcher;)V"))
    private void populateFromPlayerInventory(PlayerInventory instance, RecipeMatcher finder) {
        ((ComponentPlayerInventory)instance).populateComponentRecipeFinder(this.matcher);
    }

    @Redirect(method = "refreshResults", at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"))
    private void refreshResults(List<RecipeResultCollection> instance, Consumer consumer) {
        instance.forEach(collection -> ((ComponentRecipeResultCollection)collection).computeComponentCraftables(this.matcher, this.craftingScreenHandler.getCraftingWidth(), this.craftingScreenHandler.getCraftingHeight(), this.recipeBook));
    }

    @Redirect(method = "refreshInputs", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RecipeMatcher;clear()V"))
    private void clearMatcher2(RecipeMatcher instance) {
        this.matcher.clear();
    }

    @Redirect(method = "refreshInputs", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/AbstractRecipeScreenHandler;populateRecipeFinder(Lnet/minecraft/recipe/RecipeMatcher;)V"))
    private void populateFromScreen2(AbstractRecipeScreenHandler instance, RecipeMatcher recipeMatcher) {
        ((ComponentRecipeScreenHandler)instance).populateComponentRecipeFinder(this.matcher);
    }

    @Redirect(method = "refreshInputs", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;populateRecipeFinder(Lnet/minecraft/recipe/RecipeMatcher;)V"))
    private void populateFromPlayerInventory2(PlayerInventory instance, RecipeMatcher finder) {
        ((ComponentPlayerInventory)instance).populateComponentRecipeFinder(this.matcher);
    }
}
