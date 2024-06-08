package net.crioch.fifymcc.component.remainder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.crioch.fifymcc.registry.FIFYMRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RemainderTypes {
    public static Remainder.Type BREAKABLE;
    public static Remainder.Type DAMAGEABLE;
    public static Remainder.Type ITEM_STACK;

    public static void register() {
        BREAKABLE = register(BreakableRemainder.ID, BreakableRemainder.CODEC);
        DAMAGEABLE = register(DamageableRemainder.ID, DamageableRemainder.CODEC);
        ITEM_STACK = register(StackRemainder.ID, StackRemainder.CODEC);
    }

    public static Remainder.Type register(Identifier id, MapCodec<? extends Remainder> codec) {
        return Registry.register(FIFYMRegistries.REMAINDER_TYPE_REGISTRY, id, new Remainder.Type(codec));
    }
}
