package net.crioch.fifymcc.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.*;

import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Function;

@Mixin(Ingredient.TagEntry.class)
public class Ingredient$TagEntryMixin  {

    @Unique
    public ComponentChanges changes;

    @Redirect(method = "getStacks", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private <E> boolean createItemStackWithComponentChanges(List<ItemStack> instance, E e, @Local RegistryEntry<Item> entry) {
        return instance.add(new ItemStack(entry, 1, this.changes));
    }

    @ModifyArg(method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
                    remap = false)
    )
    private static Function<RecordCodecBuilder.Instance<Ingredient.TagEntry>, ? extends App<RecordCodecBuilder.Mu<Ingredient.TagEntry>, Ingredient.TagEntry>> attachComponentChanges(Function<RecordCodecBuilder.Instance<Ingredient.TagEntry>, ? extends App<RecordCodecBuilder.Mu<Ingredient.TagEntry>, Ingredient.TagEntry>> builder) {
        return instance -> instance.group(
                RecordCodecBuilder.mapCodec(builder).forGetter(Function.identity()),
                ComponentChanges.CODEC.optionalFieldOf("components", ComponentChanges.EMPTY).forGetter(entry -> ((Ingredient$TagEntryMixin)(Object)entry).changes)
        ).apply(instance, (entry, componentChanges) -> {
            ((Ingredient$TagEntryMixin)(Object)entry).changes = componentChanges;
            return entry;
        });
    }
}
