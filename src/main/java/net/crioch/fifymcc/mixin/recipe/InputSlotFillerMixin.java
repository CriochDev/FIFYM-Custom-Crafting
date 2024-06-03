package net.crioch.fifymcc.mixin.recipe;

import net.crioch.fifymcc.interfaces.ComponentInputSlotFiller;
import it.unimi.dsi.fastutil.ints.IntList;
import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(InputSlotFiller.class)
public abstract class InputSlotFillerMixin implements ComponentInputSlotFiller, RecipeGridAligner<ItemStack> {
    @Shadow
    protected AbstractRecipeScreenHandler<? extends Inventory> handler;

    @Shadow
    protected PlayerInventory inventory;

    @Unique
    private final ComponentRecipeMatcher finder = new ComponentRecipeMatcher();

    @Redirect(method = "fillInputSlots(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/recipe/RecipeEntry;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RecipeMatcher;clear()V"))
    private void replaceClear(RecipeMatcher instance){
        this.finder.clear();
    }

    @Redirect(method = "fillInputSlots(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/recipe/RecipeEntry;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;populateRecipeFinder(Lnet/minecraft/recipe/RecipeMatcher;)V"))
    private void replaceEntityPopulate(PlayerInventory instance, RecipeMatcher recipeMatcher) {
        Iterator<ItemStack> iterator = instance.main.iterator();

        while(iterator.hasNext()) {
            ItemStack itemStack = iterator.next();
            this.finder.addUnenchantedInput(itemStack);
        }
    }

    @Redirect(method = "fillInputSlots(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/recipe/RecipeEntry;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/AbstractRecipeScreenHandler;populateRecipeFinder(Lnet/minecraft/recipe/RecipeMatcher;)V"))
    private void replaceFill(AbstractRecipeScreenHandler instance, RecipeMatcher recipeMatcher) {
        instance.populateComponentRecipeFinder(this.finder);
    }

    @Redirect(method = "fillInputSlots(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/recipe/RecipeEntry;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RecipeMatcher;match(Lnet/minecraft/recipe/Recipe;Lit/unimi/dsi/fastutil/ints/IntList;)Z"))
    private boolean replaceMatch(RecipeMatcher instance, Recipe<?> recipe, @Nullable IntList output) {
        return this.finder.match(recipe, null);
    }

    @Inject(method = "fillInputSlots(Lnet/minecraft/recipe/RecipeEntry;Z)V", at = @At(value = "HEAD"), cancellable = true)
    private void fillInputSlotsClient(RecipeEntry<? extends Recipe<? extends Inventory>> recipe, boolean craftAll, CallbackInfo ci) {
        int j;
        boolean bl = this.handler.matchesWithComponents(recipe);
        int i = this.finder.countCrafts(recipe, null);
        if (bl) {
            for (j = 0; j < this.handler.getCraftingHeight() * this.handler.getCraftingWidth() + 1; ++j) {
                ItemStack itemStack;
                if (j == this.handler.getCraftingResultSlotIndex() || (itemStack = this.handler.getSlot(j).getStack()).isEmpty() || Math.min(i, itemStack.getMaxCount()) >= itemStack.getCount() + 1) continue;
                ci.cancel();
                return;
            }
        }
        j = this.getAmountToFill(craftAll, i, bl);
        List<ItemStack> stackList = new ArrayList<>();
        if (this.finder.match(recipe.value(), stackList, j)) {
            int k = j;
            Iterator<ItemStack> stackIterator = stackList.iterator();
            while (stackIterator.hasNext()) {
                int m;
                ItemStack itemStack = stackIterator.next();
                if (itemStack.isEmpty() || (m = itemStack.getMaxCount()) >= k) continue;
                k = m;
            }
            j = k;
            if (this.finder.match(recipe.value(), stackList, j)) {
                this.returnInputs();
                this.alignComponentRecipeToGrid(this.handler.getCraftingWidth(), this.handler.getCraftingHeight(), this.handler.getCraftingResultSlotIndex(), recipe, stackList.iterator(), j);
            }
        }
        ci.cancel();
    }

    @Shadow
    protected int getAmountToFill(boolean craftAll, int limit, boolean recipeInCraftingSlots) {
        return 0;
    }

    @Shadow
    protected void returnInputs() {}

    @Shadow
    private boolean canReturnInputs() {
        return false;
    }

    @Shadow
    protected void fillInputSlots(RecipeEntry<? extends Recipe<?>> recipe, boolean craftAll) {}

    @Override
    public void acceptAlignedInput(Iterator<ItemStack> inputs, int slot, int amount, int gridX, int gridY) {
        Slot slot2 = this.handler.getSlot(slot);
        ItemStack itemStack = inputs.next();
        if (!itemStack.isEmpty()) {
            for (int i = 0; i < amount; ++i) {
                this.fillInputSlot(slot2, itemStack);
            }
        }
    }

    @Shadow
    protected void fillInputSlot(Slot slot2, ItemStack itemStack) {

    }


    @Override
    public void alignComponentRecipeToGrid(int gridWidth, int gridHeight, int gridOutputSlot, RecipeEntry<?> recipe, Iterator<ItemStack> inputs, int amount) {
        int i = gridWidth;
        int j = gridHeight;
        Recipe<?> recipe2 = recipe.value();
        if (recipe2 instanceof ShapedRecipe shapedRecipe) {
            i = shapedRecipe.getWidth();
            j = shapedRecipe.getHeight();
        }

        int k = 0;

        for(int l = 0; l < gridHeight; ++l) {
            if (k == gridOutputSlot) {
                ++k;
            }

            boolean bl = (float)j < (float)gridHeight / 2.0F;
            int m = MathHelper.floor((float)gridHeight / 2.0F - (float)j / 2.0F);
            if (bl && m > l) {
                k += gridWidth;
                ++l;
            }

            for(int n = 0; n < gridWidth; ++n) {
                if (!inputs.hasNext()) {
                    return;
                }

                bl = (float)i < (float)gridWidth / 2.0F;
                m = MathHelper.floor((float)gridWidth / 2.0F - (float)i / 2.0F);
                int o = i;
                boolean bl2 = n < i;
                if (bl) {
                    o = m + i;
                    bl2 = m <= n && n < m + i;
                }

                if (bl2) {
                    this.acceptAlignedInput(inputs, k, amount, l, n);
                } else if (o == n) {
                    k += gridWidth - n;
                    break;
                }

                ++k;
            }
        }
    }
}
