package net.crioch.fifymcc.mixin.recipe;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.component.ComponentChanges;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

@Mixin(Ingredient.StackEntry.class)
public class Ingredient$StackEntryMixin {
    @Shadow
    @Final
    @Mutable
    private static Codec<Ingredient.StackEntry> CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ItemStack.REGISTRY_ENTRY_CODEC.fieldOf("item").forGetter(Ingredient.StackEntry::stack),
                        ComponentChanges.CODEC.optionalFieldOf("components", ComponentChanges.EMPTY).forGetter(entry -> entry.stack().getComponentChanges())
                ).apply(instance, Ingredient$StackEntryMixin::createStack)
        );
    }

    private static Ingredient.StackEntry createStack(ItemStack stack, ComponentChanges changes) {
        return init(new ItemStack(stack.getRegistryEntry(), 1, changes));
    }

    @Invoker("<init>")
    private static Ingredient.StackEntry init(ItemStack stack) {
        throw new AssertionError();
    }
}
