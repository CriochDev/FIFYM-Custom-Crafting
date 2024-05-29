package net.crioch.fifymcustomcrafting.mixin;

import net.crioch.fifymcustomcrafting.interfaces.ComponentInputSlotFiller;
import net.crioch.fifymcustomcrafting.interfaces.ComponentRecipeScreenHandler;
import it.unimi.dsi.fastutil.ints.IntList;
import net.crioch.fifymcustomcrafting.ComponentRecipeMatcher;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
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

    @Unique
    private final ComponentRecipeMatcher matcher = new ComponentRecipeMatcher();

    @Redirect(method = "fillInputSlots(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/recipe/RecipeEntry;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RecipeMatcher;clear()V"))
    private void replaceClear(RecipeMatcher instance){
        this.matcher.clear();
    }

    @Redirect(method = "fillInputSlots(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/recipe/RecipeEntry;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/AbstractRecipeScreenHandler;populateRecipeFinder(Lnet/minecraft/recipe/RecipeMatcher;)V"))
    private void replaceFill(AbstractRecipeScreenHandler<?> instance, RecipeMatcher recipeMatcher) {
        ((ComponentRecipeScreenHandler)instance).populateComponentRecipeFinder(this.matcher);
    }

    @Redirect(method = "fillInputSlots(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/recipe/RecipeEntry;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RecipeMatcher;match(Lnet/minecraft/recipe/Recipe;Lit/unimi/dsi/fastutil/ints/IntList;)Z"))
    private boolean replaceMatch(RecipeMatcher instance, Recipe<?> recipe, @Nullable IntList output) {
        return this.matcher.match(recipe, null) && false;
    }

    @Inject(method = "fillInputSlots(Lnet/minecraft/recipe/RecipeEntry;Z)V", at = @At(value = "HEAD"), cancellable = true)
    private void fillInputSlotsClient(RecipeEntry<? extends Recipe<? extends Inventory>> recipe, boolean craftAll, CallbackInfo ci) {
        int j;
        boolean bl = ((ComponentRecipeScreenHandler)this.handler).matchesWithComponents(recipe);
        int i = this.matcher.countCrafts(recipe, null);
        if (bl) {
            for (j = 0; j < this.handler.getCraftingHeight() * this.handler.getCraftingWidth() + 1; ++j) {
                ItemStack itemStack;
                if (j == this.handler.getCraftingResultSlotIndex() || (itemStack = this.handler.getSlot(j).getStack()).isEmpty() || Math.min(i, itemStack.getMaxCount()) >= itemStack.getCount() + 1) continue;
                return;
            }
        }
        j = this.invokeGetAmountToFill(craftAll, i, bl);
        List<ItemStack> stackList = new ArrayList<>();
        if (this.matcher.match(recipe.value(), stackList, j)) {
            int k = j;
            Iterator<ItemStack> stackIterator = stackList.iterator();
            while (stackIterator.hasNext()) {
                int m;
                ItemStack itemStack = stackIterator.next();
                if (itemStack.isEmpty() || (m = itemStack.getMaxCount()) >= k) continue;
                k = m;
            }
            j = k;
            if (this.matcher.match(recipe.value(), stackList, j)) {
                this.invokeReturnInputs();
                this.alignComponentRecipeToGrid(this.handler.getCraftingWidth(), this.handler.getCraftingHeight(), this.handler.getCraftingResultSlotIndex(), recipe, stackList.iterator(), j);
            }
        }
        ci.cancel();
    }

    @Invoker(value = "getAmountToFill")
    abstract int invokeGetAmountToFill(boolean craftAll, int limit, boolean recipeInCraftingSlots);

    @Invoker(value = "returnInputs")
    abstract void invokeReturnInputs();

    @Override
    public void acceptAlignedInput(Iterator<ItemStack> inputs, int slot, int amount, int gridX, int gridY) {
        Slot slot2 = this.handler.getSlot(slot);
        ItemStack itemStack = inputs.next();
        if (!itemStack.isEmpty()) {
            for (int i = 0; i < amount; ++i) {
                this.invokeFillInputSlot(slot2, itemStack);
            }
        }
    }
    @Invoker(value = "fillInputSlot")
    abstract void invokeFillInputSlot(Slot slot2, ItemStack itemStack);
}
