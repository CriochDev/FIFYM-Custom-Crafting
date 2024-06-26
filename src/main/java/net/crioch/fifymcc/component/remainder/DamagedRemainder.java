package net.crioch.fifymcc.component.remainder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.crioch.fifymcc.util.Util;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public class DamagedRemainder extends RemainderWithSeed {
    public static final Identifier ID = new Identifier(Util.MOD_ID, "damaged");

    public static final MapCodec<DamagedRemainder> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codecs.POSITIVE_INT.optionalFieldOf("damage", 1).forGetter(DamagedRemainder::damage),
                    Codec.LONG.optionalFieldOf("seed", 0L).forGetter(DamagedRemainder::getSeed),
                    Codec.BOOL.optionalFieldOf("can_break", true).forGetter(DamagedRemainder::canBreak)
            ).apply(instance, DamagedRemainder::new)
    );

    private final int damage;
    public int damage() {
        return this.damage;
    }

    private final boolean canBreak;
    public boolean canBreak() {
        return this.canBreak;
    }

    public DamagedRemainder(int damage, long seed, boolean canBreak) {
        super(RemainderTypes.DAMAGEABLE, seed);
        this.damage = damage;
        this.canBreak = canBreak;
    }

    @Override
    public ItemStack getRemainder(ItemStack stack) {
        ItemStack remainder = super.getRemainder(stack);
        if (stack.isDamageable()) {
            remainder = stack.copy();
            int damage = this.damage;
            int level = EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack);
            int prevented = 0;

            for(int k = 0; level > 0 && k < damage; ++k) {
                if (UnbreakingEnchantment.shouldPreventDamage(stack, level, random)) {
                    ++prevented;
                }
            }
            damage -= prevented;

            if (damage > 0) {
                damage += remainder.getDamage();
                remainder.setDamage(damage);
            }

            if (damage >= remainder.getMaxDamage()) {
                if (this.canBreak) {
                    remainder.setDamage(0);
                    remainder.decrement(1);
                } else {
                    remainder.setDamage(remainder.getMaxDamage());
                }
            }
        } else if (stack.contains(DataComponentTypes.UNBREAKABLE)) {
            remainder = stack.copy();
        }

        return remainder;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof DamagedRemainder && ((DamagedRemainder)object).damage == this.damage && ((DamagedRemainder)object).canBreak == this.canBreak;
    }
}
