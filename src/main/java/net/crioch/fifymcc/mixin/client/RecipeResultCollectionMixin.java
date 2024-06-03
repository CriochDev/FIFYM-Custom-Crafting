package net.crioch.fifymcc.mixin.client;

import com.google.common.collect.Sets;
import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.crioch.fifymcc.interfaces.ComponentRecipeResultCollection;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.book.RecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Mixin(RecipeResultCollection.class)
public class RecipeResultCollectionMixin implements ComponentRecipeResultCollection {
    @Shadow
    private List<RecipeEntry<?>> recipes;
    @Shadow
    private Set<RecipeEntry<?>> craftableRecipes = Sets.newHashSet();
    @Shadow
    private Set<RecipeEntry<?>> fittingRecipes = Sets.newHashSet();

    @Override
    public void computeComponentCraftables(ComponentRecipeMatcher recipeFinder, int gridWidth, int gridHeight, RecipeBook recipeBook) {
        Iterator<RecipeEntry<?>> recipeIterator = this.recipes.iterator();

            while(recipeIterator.hasNext()) {
                RecipeEntry<?> recipeEntry = recipeIterator.next();
                boolean bl = recipeEntry.value().fits(gridWidth, gridHeight) && recipeBook.contains(recipeEntry);
                if (bl) {
                    this.fittingRecipes.add(recipeEntry);
                } else {
                    this.fittingRecipes.remove(recipeEntry);
                }

                if (bl && recipeFinder.match(recipeEntry.value(), null)) {
                    this.craftableRecipes.add(recipeEntry);
                } else {
                    this.craftableRecipes.remove(recipeEntry);
                }
            }
    }
}
