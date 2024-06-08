package net.crioch.fifymcc.mixin.util.math.random;

import net.crioch.fifymcc.interfaces.RandomGetSeed;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Random.class)
public interface RandomMixin extends RandomGetSeed {

}
