package net.crioch.fifymcc.recipe;

import net.minecraft.component.ComponentChanges;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

public class RecipeKey extends Pair<Integer, ComponentChanges> implements Comparable<RecipeKey> {
    public RecipeKey(Integer left, ComponentChanges right) {
        super(left, right);
    }

    @Override
    public int compareTo(@NotNull RecipeKey o) {
        int diff = getLeft() - o.getLeft();
        if (diff != 0) {
            return diff;
        }
        return getRight().equals(o.getRight())? 0 : 1;
    }
}
