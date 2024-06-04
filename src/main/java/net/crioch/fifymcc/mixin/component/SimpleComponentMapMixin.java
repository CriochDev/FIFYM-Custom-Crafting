package net.crioch.fifymcc.mixin.component;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.crioch.fifymcc.interfaces.SimpleComponentMapSet;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ComponentMap.Builder.SimpleComponentMap.class)
public class SimpleComponentMapMixin implements SimpleComponentMapSet {
    @Shadow
    @Final
    private Reference2ObjectMap<DataComponentType<?>, Object> map;

    @Override
    public <T> void set(DataComponentType<T> type, T value) {
        this.map.put(type, value);
    }
}
