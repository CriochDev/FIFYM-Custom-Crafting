package net.crioch.fifymcc;

import net.crioch.fifymcc.component.FIFYMDataComponentTypes;
import net.crioch.fifymcc.component.remainder.RemainderTypes;
import net.crioch.fifymcc.component.remainder.StackRemainder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;

import java.util.Iterator;
import java.util.Map;

public class FIFYMCustomCraftingMod implements ModInitializer {
    @Override
    public void onInitialize() {
        RemainderTypes.register();
        FIFYMDataComponentTypes.register();

        DefaultItemComponentEvents.MODIFY.register((context) -> {
            Map<Item, Integer> fuelValues = AbstractFurnaceBlockEntity.createFuelTimeMap();
            for (Iterator<Item> it = Registries.ITEM.iterator(); it.hasNext(); ) {
                Item item = it.next();

                // Make items enchantable
                int enchantability = item.getEnchantability();
                if (enchantability > 0) {
                    context.modify(item, (components) -> components.add(FIFYMDataComponentTypes.ENCHANTABILITY, enchantability));
                }

                // Make items usable as fuel
                int fuelValue = fuelValues.getOrDefault(item, 0);
                if (fuelValue > 0) {
                    context.modify(item, components -> components.add(FIFYMDataComponentTypes.FUEL_VALUE, fuelValue));
                }

                // Add recipe remainders
                if (item.hasRecipeRemainder()) {
                    Item remainder = item.getRecipeRemainder();
                    context.modify(item, components -> components.add(FIFYMDataComponentTypes.RECIPE_REMAINDER, new StackRemainder(new ItemStack(remainder))));
                }

                // Add compost chance, if applicable
                if (ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.containsKey(item)) {
                    context.modify(item, components -> components.add(FIFYMDataComponentTypes.COMPOST_CHANCE, ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.getFloat(item)));
                }
            }

            // Make Blaze Powder work as fuel for the brewing stand
            context.modify(Items.BLAZE_POWDER, components -> components.add(FIFYMDataComponentTypes.BREWING_STAND_FUEL, 20));
        });


    }
}
