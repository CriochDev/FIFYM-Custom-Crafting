package net.crioch.fifymcc.component.remainder;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.crioch.fifymcc.registry.FIFYMRegistries;
import net.crioch.fifymcc.util.Util;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class StackRemainder extends Remainder {
    public static final Identifier ID = new Identifier(Util.MOD_ID, "stack_remainder");
    public static final MapCodec<StackRemainder> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ItemStack.REGISTRY_ENTRY_CODEC.fieldOf("id").forGetter(StackRemainder::stack),
                    ComponentChanges.CODEC.optionalFieldOf("components", ComponentChanges.EMPTY).forGetter((remainder) -> remainder.stack().getComponentChanges())
            ).apply(instance, (stack, changed) -> new StackRemainder(new ItemStack(stack.getRegistryEntry(), 1, changed))));

    private final ItemStack stack;
    public ItemStack stack() {
        return this.stack;
    }

    public StackRemainder(ItemStack stack) {
        super(RemainderTypes.ITEM_STACK);
        this.stack = stack;
    }


    @Override
    public ItemStack getRemainder(ItemStack stack) {
        return this.stack.copy();
    }
}
