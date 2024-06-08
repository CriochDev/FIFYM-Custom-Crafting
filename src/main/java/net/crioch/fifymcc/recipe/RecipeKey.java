package net.crioch.fifymcc.recipe;

import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Triplet;

public class RecipeKey extends Triplet<Integer, Integer, Integer> implements Comparable<RecipeKey> {
    public RecipeKey(Integer itemId, Integer componentHash, Integer originalComponentHash) {
        super(itemId, componentHash, originalComponentHash);
    }

    public int itemId() {
        return this.getA();
    }

    public int componentHash() {
        return this.getB();
    }

    public int originalComponentHash() {
        return this.getC();
    }

    @Override
    public int compareTo(@NotNull RecipeKey o) {
        int itemIdDiff = this.getA() - o.getA();
        if (itemIdDiff != 0) {
            return itemIdDiff;
        }

        return this.getB() - o.getB();
    }
}
