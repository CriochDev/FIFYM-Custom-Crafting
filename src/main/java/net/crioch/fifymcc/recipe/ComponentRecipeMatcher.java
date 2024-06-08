/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package net.crioch.fifymcc.recipe;

import com.google.common.collect.Lists;

import java.util.*;

import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.component.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

/**
 * Matching class that matches a recipe to its required resources.
 * This specifically does not check patterns (See {@link net.minecraft.recipe.ShapedRecipe} for that).
 */
public class ComponentRecipeMatcher {
    private final Map<Integer, Map<Integer, Pair<Map<Integer, Integer>, Set<ComponentChanges>>>> inputs = new Int2ObjectArrayMap<>();
    private static final Map<Integer, ComponentChanges> EMPTY = new Int2ObjectArrayMap<>();

    /**
     * Adds a full item remainder to the pool of available resources.
     *
     * <p>This is equivalent to calling {@code addInput(remainder, Item.DEFAULT_MAX_COUNT)}.
     */
    public void addUnenchantedInput(ItemStack stack) {
        if (!(stack.hasEnchantments() || stack.contains(DataComponentTypes.CUSTOM_NAME))) {
            this.addInput(stack);
        }
    }

    /**
     * Adds a full item remainder to the pool of available resources.
     *
     * <p>This is equivalent to calling {@code addInput(remainder, Item.DEFAULT_MAX_COUNT)}.
     */
    public void addInput(ItemStack stack) {
        this.addInput(stack, stack.getMaxCount());
    }

    /**
     * Adds an item remainder to the pool of available resources.
     */
    public void addInput(ItemStack stack, int maxCount) {
        if (!stack.isEmpty()) {
            ComponentChanges original = stack.getComponentChanges();

            RecipeKey key = ComponentRecipeMatcher.getKey(stack);
            Map<Integer, Pair<Map<Integer, Integer>, Set<ComponentChanges>>> potentials = this.inputs.computeIfAbsent(key.itemId(), k -> new Int2ObjectArrayMap<>());
            Pair<Map<Integer, Integer>, Set<ComponentChanges>> subpotentials = potentials.computeIfAbsent(key.componentHash(), k -> new Pair<>(new Int2IntOpenHashMap(), new HashSet<>()));
            subpotentials.getRight().add(original);
            int count = subpotentials.getLeft().getOrDefault(key.originalComponentHash(), 0)  + Math.min(stack.getCount(), maxCount);
            subpotentials.getLeft().put(key.originalComponentHash(), count);
        }
    }

    void addInput(RecipeKey key, int count) {
        Map<Integer, Pair<Map<Integer, Integer>, Set<ComponentChanges>>> potentials = this.inputs.get(key.itemId());
        Pair<Map<Integer, Integer>, Set<ComponentChanges>> subpotentials = potentials.get(key.componentHash());
        subpotentials.getLeft().put(key.originalComponentHash(), subpotentials.getLeft().get(key.originalComponentHash()) + count);
    }

    public static RecipeKey getKey(ItemStack stack) {
        ComponentChanges componentChanges = stack.getComponentChanges();
        int originalStackHash = componentChanges.hashCode();
        componentChanges = FIFYMRecipeMatcherHelper.removeIgnoredChanges(stack);
        return new RecipeKey(ComponentRecipeMatcher.getItemId(stack), componentChanges.hashCode(), originalStackHash);
    }

    public List<RecipeKey> getKeys(Ingredient ingredient) {
        return Arrays.stream(ingredient.getMatchingStacks()).map(stack -> {
            int itemId = getItemId(stack);
            Map<Integer, Pair<Map<Integer, Integer>, Set<ComponentChanges>>> potentials = this.inputs.get(getItemId(stack));
            if (potentials == null) return null;

            ComponentChanges stackChanges = stack.getComponentChanges();
            int stackChangesHash = stackChanges.hashCode();
            Pair<Map<Integer, Integer>, Set<ComponentChanges>> subpotentials = potentials.get(stackChangesHash);
            if (subpotentials == null) return null;


            Set<Map.Entry<DataComponentType<?>, Optional<?>>> ingredientChanges = stackChanges.entrySet();
            ComponentChanges matching = null;
            for (ComponentChanges changes: subpotentials.getRight()) {
                if (changes.entrySet().containsAll(ingredientChanges)) {
                    matching = changes;
                    break;
                }
            }
            if (matching == null) return null;

            return new RecipeKey(itemId, stackChangesHash, matching.hashCode());
        }).filter(Objects::nonNull).toList();
    }

    public static int getItemId(ItemStack stack) {
        return Registries.ITEM.getRawId(stack.getItem());
    }

    /**
     * Consumes a resource from the pool of available items.
     *
     * @param key the raw id of the item being consumed
     * @param count the number of times that item must be consumed
     */
    boolean consume(RecipeKey key, int count) {
        Map<Integer, Pair<Map<Integer, Integer>, Set<ComponentChanges>>> potentials = this.inputs.get(key.itemId());
        if (potentials == null) return false;

        Pair<Map<Integer, Integer>, Set<ComponentChanges>> subpotentials = potentials.get(key.componentHash());

        if (subpotentials != null) {
            Map<Integer, Integer> map = subpotentials.getLeft();
            int available = map.getOrDefault(key.originalComponentHash(), 0);
            if (available >= count) {
                map.put(key.originalComponentHash(), available - count);
                return true;
            }
        }

        return false;
    }

    /**
     * Attempts to match the recipe against the collected inputs.
     * Assumes only one output is required.
     *
     * @param recipe the recipe to match against
     * @param output optional output list of item ids that were matched whilst evaluating the recipe conditions
     */
    public boolean match(Recipe<?> recipe, @Nullable List<ItemStack> output) {
        return this.match(recipe, output, 1);
    }

    /**
     * Attempts to match the recipe against the collected inputs. Will only succeed if there has been enough
     * resources gathered to produce the requested number of outputs.
     *
     * @param multiplier the number of expected outputs
     * @param output optional output list of item ids that were matched whilst evaluating the recipe conditions
     * @param recipe the recipe to match against
     */
    public boolean match(Recipe<?> recipe, @Nullable List<ItemStack> output, int multiplier) {
        return new Matcher(recipe).match(multiplier, output);
    }

    /**
     * Determines the number of crafts that can be produced for a recipe using the
     * collected resources available to this crafter.
     *
     * @param output optional output list of item ids that were matched whilst evaluating the recipe conditions
     */
    public int countCrafts(RecipeEntry<?> recipe, @Nullable List<ItemStack> output) {
        return this.countCrafts(recipe, Integer.MAX_VALUE, output);
    }

    /**
     * Determines the number of crafts that can be produced for a recipe using the
     * collected resources available to this crafter.
     *
     * @param output optional output list of item ids that were matched whilst evaluating the recipe conditions
     */
    public int countCrafts(RecipeEntry<?> recipe, int limit, @Nullable List<ItemStack> output) {
        return new Matcher(recipe.value()).countCrafts(limit, output);
    }

    public void clear() {
        this.inputs.clear();
    }

    public class Matcher {
        private final Recipe<?> recipe;
        private final List<Ingredient> ingredients = Lists.newArrayList();
        private final int totalIngredients;
        private final RecipeKey[] requiredItems;
        private final int totalRequiredItems;
        private final BitSet requirementsMatrix;
        private final IntList ingredientItemLookup = new IntArrayList();

        public Matcher(final Recipe<?> recipe) {
            this.recipe = recipe;
            this.ingredients.addAll(recipe.getIngredients());
            this.ingredients.removeIf(Ingredient::isEmpty);
            this.totalIngredients = this.ingredients.size();
            this.requiredItems = this.createItemRequirementList();
            this.totalRequiredItems = this.requiredItems.length;
            this.requirementsMatrix = new BitSet(this.totalIngredients + this.totalRequiredItems + this.totalIngredients + this.totalIngredients * this.totalRequiredItems);

            for(int i = 0; i < this.ingredients.size(); ++i) {
                List<RecipeKey> requiredList = Arrays.stream((this.ingredients.get(i)).getMatchingStacks()).map(ComponentRecipeMatcher::getKey).toList();

                for(int j = 0; j < this.totalRequiredItems; ++j) {
                    for (RecipeKey required: requiredList) {
                        if (required.compareTo(this.requiredItems[j]) == 0) {
                            this.requirementsMatrix.set(this.getRequirementIndex(true, j, i));
                        }
                    }
                }
            }

        }

        public boolean match(int limit, @Nullable List<ItemStack> output) {
            if (limit <= 0) {
                return true;
            } else {
                int i;
                for(i = 0; this.checkRequirements(limit); ++i) {
                    ComponentRecipeMatcher.this.consume(this.requiredItems[this.ingredientItemLookup.getInt(0)], limit);
                    int j = this.ingredientItemLookup.size() - 1;
                    this.unfulfillRequirement(this.ingredientItemLookup.getInt(j));

                    for(int k = 0; k < j; ++k) {
                        this.flipRequirement((k & 1) == 0, this.ingredientItemLookup.getInt(k), this.ingredientItemLookup.getInt(k + 1));
                    }

                    this.ingredientItemLookup.clear();
                    this.requirementsMatrix.clear(0, this.totalIngredients + this.totalRequiredItems);
                }

                boolean bl = i == this.totalIngredients;
                boolean bl2 = bl && output != null;
                if (bl2) {
                    output.clear();
                }

                this.requirementsMatrix.clear(0, this.totalIngredients + this.totalRequiredItems + this.totalIngredients);
                int l = 0;
                List<Ingredient> list = this.recipe.getIngredients();
                Iterator var8 = list.iterator();

                while(true) {
                    while(var8.hasNext()) {
                        Ingredient ingredient = (Ingredient)var8.next();
                        if (bl2 && ingredient.isEmpty()) {
                            output.add(ItemStack.EMPTY);
                        } else {
                            for(int m = 0; m < this.totalRequiredItems; ++m) {
                                if (this.checkRequirement(false, l, m)) {
                                    this.flipRequirement(true, m, l);
                                    RecipeKey key = this.requiredItems[m];
                                    ComponentRecipeMatcher.this.addInput(key, limit);
                                    if (bl2) {
                                        ComponentChanges changes = this.getMatching(key);
                                        output.add(new ItemStack(Item.byRawId(key.itemId()).getRegistryEntry(), 1, changes));

                                    }
                                }
                            }

                            ++l;
                        }
                    }

                    return bl;
                }
            }
        }

        private RecipeKey[] createItemRequirementList() {
            Set<RecipeKey> keyCollection = new TreeSet<>();

            for (Ingredient ingredient : this.ingredients) {
                List<RecipeKey> keys = ComponentRecipeMatcher.this.getKeys(ingredient);
                keyCollection.addAll(keys);
            }

            return keyCollection.toArray( new RecipeKey[0]);
        }

        private boolean checkRequirements(int multiplier) {
            int i = this.totalRequiredItems;

            for(int j = 0; j < i; ++j) {
                RecipeKey key = this.requiredItems[j];
                Map<Integer, Pair<Map<Integer, Integer>, Set<ComponentChanges>>> potentials = ComponentRecipeMatcher.this.inputs.get(key.itemId());
                if (potentials == null) continue;
                Pair<Map<Integer, Integer>, Set<ComponentChanges>> subpotentials = potentials.get(key.componentHash());
                if (subpotentials == null) continue;
                    int count = subpotentials.getLeft().getOrDefault(key.originalComponentHash(), 0);
                if (count >= multiplier) {
                    this.addRequirement(false, j);

                    while(!this.ingredientItemLookup.isEmpty()) {
                        int k = this.ingredientItemLookup.size();
                        boolean bl = (k & 1) == 1;
                        int l = this.ingredientItemLookup.getInt(k - 1);
                        if (!bl && !this.getRequirement(l)) {
                            break;
                        }

                        int m = bl ? this.totalIngredients : i;

                        int n;
                        for(n = 0; n < m; ++n) {
                            boolean requirementUnfulfilled = this.isRequirementUnfulfilled(bl, n);
                            boolean needsRequirement = this.needsRequirement(bl, l, n);
                            boolean checkRequirement = this.checkRequirement(bl, l, n);
                            if (!requirementUnfulfilled && needsRequirement && checkRequirement) {
                                this.addRequirement(bl, n);
                                break;
                            }
                        }

                        n = this.ingredientItemLookup.size();
                        if (n == k) {
                            this.ingredientItemLookup.removeInt(n - 1);
                        }
                    }

                    if (!this.ingredientItemLookup.isEmpty()) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean getRequirement(int itemId) {
            return this.requirementsMatrix.get(this.getRequirementIndex(itemId));
        }

        private void unfulfillRequirement(int index) {
            this.requirementsMatrix.set(this.getRequirementIndex(index));
        }

        private int getRequirementIndex(int index) {
            return this.totalIngredients + this.totalRequiredItems + index;
        }

        private boolean needsRequirement(boolean reversed, int itemIndex, int ingredientIndex) {
            return this.requirementsMatrix.get(this.getRequirementIndex(reversed, itemIndex, ingredientIndex));
        }

        private boolean checkRequirement(boolean reversed, int itemIndex, int ingredientIndex) {
            return reversed != this.requirementsMatrix.get(1 + this.getRequirementIndex(reversed, itemIndex, ingredientIndex));
        }

        private void flipRequirement(boolean reversed, int itemIndex, int ingredientIndex) {
            this.requirementsMatrix.flip(1 + this.getRequirementIndex(reversed, itemIndex, ingredientIndex));
        }

        private int getRequirementIndex(boolean reversed, int itemIndex, int ingredientIndex) {
            int i = reversed ? itemIndex * this.totalIngredients + ingredientIndex : ingredientIndex * this.totalIngredients + itemIndex;
            return this.totalIngredients + this.totalRequiredItems + this.totalIngredients + 2 * i;
        }

        private void addRequirement(boolean reversed, int index) {
            this.requirementsMatrix.set(this.getRequirementIndex(reversed, index));
            this.ingredientItemLookup.add(index);
        }

        private boolean isRequirementUnfulfilled(boolean reversed, int itemId) {
            return this.requirementsMatrix.get(this.getRequirementIndex(reversed, itemId));
        }

        private int getRequirementIndex(boolean reversed, int itemId) {
            return (reversed ? 0 : this.totalIngredients) + itemId;
        }

        public int countCrafts(int minimum, @Nullable List<ItemStack> output) {
            int i = 0;
            int j = Math.min(minimum, this.getMaximumCrafts()) + 1;

            while(true) {
                while(true) {
                    int k = (i + j) / 2;
                    if (this.match(k, null)) {
                        if (j - i <= 1) {
                            if (k > 0) {
                                this.match(k, output);
                            }

                            return k;
                        }

                        i = k;
                    } else {
                        j = k;
                    }
                }
            }
        }

        private int getMaximumCrafts() {
            int maxCraftable = Integer.MAX_VALUE;

            for (Ingredient ingredient : this.ingredients) {
                int ingredientMax = 0;

                for(RecipeKey key : ComponentRecipeMatcher.this.getKeys(ingredient)) {
                    int count = ComponentRecipeMatcher.this.inputs.get(key.itemId()).get(key.componentHash()).getLeft().get(key.originalComponentHash());
                    ingredientMax = Math.max(ingredientMax, count);
                }

                if (maxCraftable > 0) {
                    maxCraftable = Math.min(maxCraftable, ingredientMax);
                }
            }

            return maxCraftable;
        }

        private ComponentChanges getMatching(RecipeKey key) {
            ComponentChanges matched = null;
            for (ComponentChanges changes : ComponentRecipeMatcher.this.inputs.get(key.itemId()).get(key.componentHash()).getRight()) {
                if (changes.hashCode() == key.originalComponentHash()) {
                    matched = changes;
                    break;
                }
            }

            return matched;
        }
    }
}


