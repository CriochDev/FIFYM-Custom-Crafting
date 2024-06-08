package net.crioch.fifymcc.mixin.util.math.random;

import net.crioch.fifymcc.interfaces.RandomGetSeed;
import net.minecraft.util.math.random.CheckedRandom;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.concurrent.atomic.AtomicLong;

@Mixin(CheckedRandom.class)
public class CheckedRandomMixin implements RandomGetSeed {

    @Shadow
    @Final
    private AtomicLong seed;

    @Override
    public long seed() {
        return this.seed.get();
    }
}
