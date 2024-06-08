package net.crioch.fifymcc;

import net.crioch.fifymcc.component.FIFYDataComponentTypes;
import net.crioch.fifymcc.component.FuelValueComponent;
import net.crioch.fifymcc.component.remainder.RemainderTypes;
import net.crioch.fifymcc.component.remainder.StackRemainder;
import net.crioch.fifymcc.registry.FIFYMRegistries;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

import java.util.Iterator;
import java.util.Map;

public class FIFYMCustomCraftingMod implements ModInitializer {
    @Override
    public void onInitialize() {
        RemainderTypes.register();
        FIFYDataComponentTypes.register();

        DefaultItemComponentEvents.MODIFY.register((context) -> {
            Map<Item, Integer> fuelValues = AbstractFurnaceBlockEntity.createFuelTimeMap();
            for (Iterator<Item> it = Registries.ITEM.iterator(); it.hasNext(); ) {
                Item item = it.next();
                int enchantability = item.getEnchantability();
                if (enchantability > 0) {
                    context.modify(item, (components) -> components.add(FIFYDataComponentTypes.ENCHANTABILITY, enchantability));
                }

                int fuelValue = fuelValues.getOrDefault(item, 0);
                if (fuelValue > 0) {
                    context.modify(item, components -> components.add(FIFYDataComponentTypes.FUEL_VALUE, new FuelValueComponent(fuelValue)));
                }
                
                if (item.hasRecipeRemainder()) {
                    Item remainder = item.getRecipeRemainder();
                    context.modify(item, components -> components.add(FIFYDataComponentTypes.RECIPE_REMAINDER, new StackRemainder(new ItemStack(remainder))));
                }
            }
        });
    }
}
