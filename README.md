# Fixed It For You, Mojang - Custom Crafting
Sometimes Mojang gets really close to a great thing and then drops it like a Bucket of Lava.

With the introduction of [Item Components](https://minecraft.wiki/w/Data_component_format#List_of_components) in Minecraft 1.20.5, we were given the ability to add and remove components from the result of a crafting recipe, but Mojang didn't allow recipe ingredients to specify components, making it so you could accidentally use unaltered items to fill the requirements.

This mod fixes that and allows all recipe types to benefit.

## New Components
### Enchantability
This component is written in json as `"fifymcc:enchantablity": <any non-negative integer>`. When this is on an itemstack, the value in it is used to determine enchantability.

### Fuel Value
This component is written in json as `"fifymcc:fuel_value": <any positive integer>`. When this is on an item stack, the value in it is used to determin whether it can be used as fuel and for how long, in ticks, it'll burn.

If you wish to remove the ability for a fuel item to burn, you can do `"!fifymcc:fuel_value": {}` instead.

### Recipe Remainder
This component gives item stacks the ability to change how they are consumed within recipes. The base json for this is `"fifymcc:recipe_remainder: { "type": <remainder identifier>, ...additional fields }`.

#### Stack Remainder
This allows an item to specify that when they are consumed in a recipe, they transform into the specified item stack.
```json5
{
    "type": "fifymcc:item_stack",
    // Any item id can be put here
    "id": "minecraft:stone",
    // Optional field to attach any components you want
    "components": { }
}
```

#### Chance Remainder
When an item has this remainder attached to it, there is a configurable chance that the item will not be consumed when crafting.
```json5
{
    "type": "fifymcc:chance",
    // Value between 0 and 1, excluding 0.
    // This is a 30% chance that the item will be consumed when crafting with it
    "break_chance": 0.3,
    // Optional field for specifying the RNG. If not provided a random value will be used
    "seed": 12383
}
```

#### Damaged Remainder
Stacks that have this remainder will take durability damage when used in crafting. Respects the unbreaking enchantment. If the stack has the `minecraft:unbreakable` component on it, it will not take durability damage. If the stack does not have the required `minecraft:damage` and `minecraft:max_damage` components, then the stack will be consumed instead.
```json5
{
    "type": "fifymcc:damaged",
    // Optional field. Determines how much durability damage the item
    // takes per craft. Defaults to 1
    "damage": 4,
    // Optional field. Determines if the stack disappears after reaching 0 durability. If false, the stack doesn't disappear but can no longer be used for crafting. Defaults to true
    "can_break": false
}
```

#### Unconsumed Remainder
If this is specified on a stack, then it will remain after crafting occurs.
```json
// No additional fields necessary
{ "type": "fifymcc:unconsumed" }
```


## Example Recipe
Below is a shapeless crafting recipe that takes a poisonous potato that has been reskinned into a pile of ash, and any item in the `minecraft:sand` tag that has custom_model_data of 1. The recipe requires that the poisonous potato not have the food component, and that it have the correct item name and custom model data.
```json5
{
    "type": "minecraft:crafting_shapeless",
    "category": "misc",
    "ingredients": [
        {
            "item": "minecraft:poisonous_potato",
            "components": {
                // Any matching stack must not have the food component on it
                "!minecraft:food": {},
                "minecraft:item_name": "{\"translate\": \"namespace.item.ash\",\"fallback\": \"Ash\"}",
                "minecraft:custom_model_data": 1
            }
        },
        {
            "item": "minecraft:bone_meal",
            "components": {
                // This bonemeal will not be cosumed by the recipe
                "fifymcc:recipe_remainder": { "type": "fifymcc:unconsumed" }
            }
        },
        {
            "tag": "minecraft:sand",
            "components": {
                "minecraft:custom_model_data": 1
            }
        },
        {
            "item": "minecraft:dirt"
        },
        {
            "item": "minecraft:gravel"
        }
    ],
    "result": {
        "count": 4,
        "id": "minecraft:dirt"
    }
}
```

## Affected Blocks/Screens
- Crafting Table
- Player Inventory (Crafting Grid)
- Furnace and any derived block (Smoker, Blast Furnce, etc.). Affects both input and fuel slot
- Smithing Table (Only `minecraft:smithing_transform` recipes for now. Trim recipes will be updated soon)
- Loom (Only Dye slot)


## Caveats
- Due to how EMI, JEI, and REI use items to look up recipes involving them, these custom recipes will be associated with the item used or produced by them and won't show up as seperate item within their GUIs.
- I have not tested this outside of the vanilla crafting space. This may or may not work with mods that provide their own crafting functionality outside of the vanilla crafting blocks. If the mod in question uses the builtin Ingredient.test() method, then it should be compatible.

## Roadmap
- Get anvil working with custom remainders
- New component to allow specifying repair ingredient
- Add component to specify repair ingredient
