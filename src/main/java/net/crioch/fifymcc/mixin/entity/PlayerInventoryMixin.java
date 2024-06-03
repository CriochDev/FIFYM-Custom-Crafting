package net.crioch.fifymcc.mixin.entity;

import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.crioch.fifymcc.interfaces.ComponentPlayerInventory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin implements ComponentPlayerInventory {
    @Final
    @Shadow
    public DefaultedList<ItemStack> main;

    @Override
    public void populateComponentRecipeFinder(ComponentRecipeMatcher matcher) {
        Iterator<ItemStack> mainIterator = this.main.iterator();

        while(mainIterator.hasNext()) {
            ItemStack itemStack = mainIterator.next();
            if (itemStack != ItemStack.EMPTY) {
                matcher.addUnenchantedInput(itemStack);
            }
        }
    }
}
