package net.crioch.fifymcc.mixin.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Ingredient.TagEntry.class)
public class Ingredient$TagEntryMixin {
    @Shadow
    @Final
    @Mutable
    private static Codec<Ingredient.TagEntry> CODEC;

    @Shadow
    @Final
    private Ingredient.TagEntry tag;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clinit(CallbackInfo ci) {
        CODEC = RecordCodecBuilder.create((instance) -> {
                return instance.group(TagKey.unprefixedCodec(RegistryKeys.ITEM).fieldOf("tag").forGetter((entry) -> {
                    return entry.tag();
                })).apply(instance, tag -> invokeInit(tag));
        };
    }

    @Invoker(value = "<init>")
    private static Ingredient.TagEntry invokeInit(TagKey<Item> tag) {
            throw new AssertionError();
    }
}
