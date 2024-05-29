/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package net.crioch.fifymcustomcrafting;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Triplet;

/**
 * Matching class that matches a recipe to its required resources.
 * This specifically does not check patterns (See {@link net.minecraft.recipe.ShapedRecipe} for that).
 */
public class ComponentRecipeMatcher {
    private final Map<Integer, List<Pair<ComponentChanges, Integer>>> inputs = new HashMap<>();

    /**
     * Adds a full item stack to the pool of available resources.
     *
     * <p>This is equivalent to calling {@code addInput(stack, Item.DEFAULT_MAX_COUNT)}.
     */
    public void addUnenchantedInput(ItemStack stack) {
        if (!(stack.isDamaged() || stack.hasEnchantments() || stack.contains(DataComponentTypes.CUSTOM_NAME))) {
            this.addInput(stack);
        }
    }

    /**
     * Adds a full item stack to the pool of available resources.
     *
     * <p>This is equivalent to calling {@code addInput(stack, Item.DEFAULT_MAX_COUNT)}.
     */
    public void addInput(ItemStack stack) {
        this.addInput(stack, stack.getMaxCount());
    }

    /**
     * Adds an item stack to the pool of available resources.
     */
    public void addInput(ItemStack stack, int maxCount) {
        if (!stack.isEmpty()) {
            int key = ComponentRecipeMatcher.getKey(stack);
            List<Pair<ComponentChanges, Integer>> potentials = this.inputs.getOrDefault(key, new ArrayList<>());
            Pair<ComponentChanges, Integer> result = null;
            for (Pair<ComponentChanges, Integer> potential: potentials) {
                if (potential.getLeft().equals(stack.getComponentChanges())) {
                    result = potential;
                    break;
                }
            }

            if (result == null) {
                result = new Pair<>(stack.getComponentChanges(), 0);
                potentials.add(result);
            }
            result.setRight(result.getRight() + stack.getCount());
            this.inputs.put(key, potentials);
        }
    }

    public static Integer getKey(ItemStack stack) {
        return Registries.ITEM.getRawId(stack.getItem());
    }

    /**
     * Determines whether a raw item id is present in the pool of crafting resources.
     */
    boolean contains(Integer key) {
        return this.inputs.containsKey(key);
    }

    /**
     * Consumes a resource from the pool of available items.
     *
     * @param key the raw id of the item being consumed
     * @param count the number of times that item must be consumed
     */
    boolean consume(Integer key, ComponentChanges changes, int count) {
        List<Pair<ComponentChanges, Integer>> available = this.inputs.get(key);

        if (available != null) {
            for (Pair<ComponentChanges, Integer> entry : available) {
                if (entry.getLeft().equals(changes) && entry.getRight() >= count) {
                    entry.setRight(entry.getRight() - count);
                    return true;
                }
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
        return new Matcher((Recipe<?>)recipe.value()).countCrafts(limit, output);
    }

    public static ItemStack getStackFromId(int itemId) {
        if (itemId == 0) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(Item.byRawId(itemId));
    }

    public void clear() {
        this.inputs.clear();
    }

    class Matcher {
        private final List<Ingredient> ingredients = Lists.newArrayList();

        public Matcher(Recipe<?> recipe) {
            this.ingredients.addAll(recipe.getIngredients());
        }

        public boolean match(int multiplier, @Nullable List<ItemStack> output) {
            boolean matches = true;

            List<Triplet<Integer, ComponentChanges, Integer>> consumed = new ArrayList<>();

            for (Ingredient ingredient: this.ingredients) {
                boolean ingredientFulfilled = false;
                if (!ingredient.isEmpty()) {
                    for (ItemStack itemStack: ingredient.getMatchingStacks()) {
                        int key = ComponentRecipeMatcher.getKey(itemStack);
                        List<Pair<ComponentChanges, Integer>> available = ComponentRecipeMatcher.this.inputs.getOrDefault(key, new ArrayList<>());
                        Set<?> set = itemStack.getComponentChanges().entrySet();
                        for (Pair<ComponentChanges, Integer> entry: available) {
                            ItemStack tmp = new ItemStack(Item.byRawId(key).getRegistryEntry(), entry.getRight(), entry.getLeft());
                            if (((set.isEmpty() && entry.getLeft().isEmpty()) || !set.isEmpty() && entry.getLeft().entrySet().containsAll(set)) && entry.getRight() >= multiplier) {
                                ingredientFulfilled = true;
                                consumed.add(new Triplet<>(key, entry.getLeft(), multiplier));
                                entry.setRight(entry.getRight() - multiplier);
                                break;
                            }
                        }
                    }
                } else {
                    ingredientFulfilled = true;
                    consumed.add(new Triplet<>(0, ComponentChanges.EMPTY, 0));
                }

                matches = matches && ingredientFulfilled;

                if (!matches) {
                    for (Triplet<Integer, ComponentChanges, Integer> result: consumed) {
                        int count = result.getC();
                        if (count < 1) {
                            continue;
                        }

                        int key = result.getA();
                        List<Pair<ComponentChanges, Integer>> available = ComponentRecipeMatcher.this.inputs.get(key);

                        ComponentChanges changes = result.getB();
                        for (Pair<ComponentChanges, Integer> entry : available) {
                            if (entry.getLeft().equals(changes)) {
                                entry.setRight(entry.getRight() + count);
                                break;
                            }
                        }

                    }
                    break;
                }
            }

            if (matches && output != null) {
                output.addAll(
                    consumed.stream().map(result -> {
                        int count = result.getC();
                        return new ItemStack(Item.byRawId(result.getA()).getRegistryEntry(), count, result.getB());
                    }).toList()
                );
            }

            return matches;
        }

        public int countCrafts(int minimum, @Nullable List<ItemStack> output) {
            int k;
            int i = 0;
            int j = Math.min(minimum, this.getMaximumCrafts()) + 1;
            while (true) {
                if (this.match(k = (i + j) / 2, null)) {
                    if (j - i <= 1) break;
                    i = k;
                    continue;
                }
                j = k;
            }
            if (k > 0) {
                this.match(k, output);
            }
            return k;
        }

        private int getMaximumCrafts() {
            int i = Integer.MAX_VALUE;
            for (Ingredient ingredient : this.ingredients) {
                int j = 0;
                Iterator<ItemStack> matchingStacksIterator = Arrays.stream(ingredient.getMatchingStacks()).iterator();
                while (matchingStacksIterator.hasNext()) {
                    ItemStack stack = matchingStacksIterator.next();
                    List<Pair<ComponentChanges, Integer>> entries = ComponentRecipeMatcher.this.inputs.get(ComponentRecipeMatcher.getKey(stack));
                    int tmp = 0;
                    Set<?> set = stack.getComponentChanges().entrySet();
                    for (Pair<ComponentChanges, Integer> entry : entries) {
                        if (entry.getLeft().entrySet().containsAll(set)) {
                            tmp += entry.getRight();
                        }
                    }
                    j = Math.max(j, tmp);
                }
                if (i < 1) continue;
                i = Math.min(i, j);
            }
            return i;
        }
    }
}


