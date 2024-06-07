package net.crioch.fifymcc.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.crioch.fifymcc.mixin.recipe.Ingredient$StackEntryMixin;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.dynamic.Codecs;

public record RecipeRemainder(ItemStack remainder) {
    public static final Codec<RecipeRemainder> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ItemStack.REGISTRY_ENTRY_CODEC.fieldOf("id").forGetter(RecipeRemainder::remainder),
                    ComponentChanges.CODEC.optionalFieldOf("components", ComponentChanges.EMPTY).forGetter((stack) -> stack.remainder().getComponentChanges())
            ).apply(instance, (stack, changed) -> new RecipeRemainder(new ItemStack(stack.getRegistryEntry(), 1, changed))));

    public static final PacketCodec<RegistryByteBuf, RecipeRemainder> PACKET_CODEC = new PacketCodec<RegistryByteBuf, RecipeRemainder>() {
        @Override
        public RecipeRemainder decode(RegistryByteBuf buf) {
            return new RecipeRemainder(ItemStack.PACKET_CODEC.decode(buf));
        }

        @Override
        public void encode(RegistryByteBuf buf, RecipeRemainder value) {
            ItemStack.PACKET_CODEC.encode(buf, value.remainder());
        }
    };

    public ItemStack getRemainder(ItemStack stack) {
        return this.remainder().copy();
    }
}
