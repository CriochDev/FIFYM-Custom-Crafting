package net.crioch.fifymcc.mixin.block.entity;

import net.crioch.fifymcc.components.FIFYDataComponentTypes;
import net.crioch.fifymcc.interfaces.SimpleComponentMapSet;
import net.crioch.fifymcc.recipe.ComponentRecipeMatcher;
import net.crioch.fifymcc.interfaces.ComponentRecipeInputProvider;
import net.crioch.fifymcc.util.Util;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.Map;

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

    @Inject(method = "addFuel(Ljava/util/Map;Lnet/minecraft/item/ItemConvertible;I)V", at = @At("HEAD"), cancellable = true)
    private static void attachFuelValueComponent(Map<Item, Integer> fuelTimes, ItemConvertible converible, int fuelTime, CallbackInfo ci) {
        Item item = converible.asItem();
        ComponentMap components = item.getComponents();
        if (components.get(FIFYDataComponentTypes.FUEL_VALUE) == null) {
            ((SimpleComponentMapSet)(Object)components).set(FIFYDataComponentTypes.FUEL_VALUE, fuelTime);
        }
        ci.cancel();
    }

    @Inject(method = "addFuel(Ljava/util/Map;Lnet/minecraft/registry/tag/TagKey;I)V", at = @At("HEAD"), cancellable = true)
    private static void attachFuelValueComponent2(Map<Item, Integer> fuelTimes, TagKey<Item> tag, int fuelTime, CallbackInfo ci) {
        for (RegistryEntry<Item> registryEntry : Registries.ITEM.iterateEntries(tag)) {
            Item item = registryEntry.value();
            ComponentMap components = item.getComponents();
            if (components.get(FIFYDataComponentTypes.FUEL_VALUE) == null) {
                ((SimpleComponentMapSet) (Object) components).set(FIFYDataComponentTypes.FUEL_VALUE, fuelTime);
            }
        }
        ci.cancel();
    }

    @Inject(method = "getFuelTime", at = @At("HEAD"), cancellable = true)
    private void getFuelTime(ItemStack fuel, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(fuel.getComponents().getOrDefault(FIFYDataComponentTypes.FUEL_VALUE, 0));
    }

    @Inject(method = "canUseAsFuel", at = @At("HEAD"), cancellable = true)
    private static void canUseAsFuel(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        AbstractFurnaceBlockEntity.createFuelTimeMap();
        cir.setReturnValue(stack.getComponents().contains(FIFYDataComponentTypes.FUEL_VALUE));
    }
}
