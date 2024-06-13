package net.crioch.fifymcc.component.remainder;

import net.crioch.fifymcc.interfaces.RandomGetSeed;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public abstract class RemainderWithSeed extends Remainder {
    protected final Random random = Random.create();

    RemainderWithSeed(Type type, long seed) {
        super(type);
        seed = seed != 0 ? seed : this.random.nextLong();
        this.random.setSeed(seed);
    }

    protected long getSeed() {
        return ((RandomGetSeed)(Object)(this.random)).seed();
    }
}
