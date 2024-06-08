package net.crioch.fifymcc.component.remainder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.crioch.fifymcc.registry.FIFYMRegistries;
import net.crioch.fifymcc.util.Util;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public class DamageableRemainder extends Remainder {
    public static final Identifier ID = new Identifier(Util.MOD_ID, "damageable_remainder");

    public static final MapCodec<DamageableRemainder> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codecs.POSITIVE_INT.optionalFieldOf("damage", 1).forGetter(DamageableRemainder::damage)
            ).apply(instance, DamageableRemainder::new)
    );

    private final int damage;
    public int damage() {
        return this.damage;
    }

    public DamageableRemainder(int damage) {
        super(RemainderTypes.DAMAGEABLE);
        this.damage = damage;
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

            if (damage > remainder.getMaxDamage()) {
                remainder.setDamage(0);
                remainder.decrement(1);
            }
        } else if (stack.contains(DataComponentTypes.UNBREAKABLE)) {
            remainder = stack.copy();
        }

        return remainder;
    }
}
