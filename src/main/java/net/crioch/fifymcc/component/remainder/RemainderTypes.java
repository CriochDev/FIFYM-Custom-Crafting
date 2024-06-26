package net.crioch.fifymcc.component.remainder;

import com.mojang.serialization.MapCodec;
import net.crioch.fifymcc.registry.FIFYMRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RemainderTypes {
    public static Remainder.Type BREAKABLE;
    public static Remainder.Type DAMAGEABLE;
    public static Remainder.Type ITEM_STACK;
    public static Remainder.Type NON_CONSUMABLE;


    public static void register() {
        BREAKABLE = register(ChanceRemainder.ID, ChanceRemainder.CODEC);
        DAMAGEABLE = register(DamagedRemainder.ID, DamagedRemainder.CODEC);
        ITEM_STACK = register(StackRemainder.ID, StackRemainder.CODEC);
        NON_CONSUMABLE = register(UnconsumedRemainder.ID, UnconsumedRemainder.CODEC);
    }

    public static Remainder.Type register(Identifier id, MapCodec<? extends Remainder> codec) {
        return Registry.register(FIFYMRegistries.REMAINDER_TYPE_REGISTRY, id, new Remainder.Type(codec));
    }
}
