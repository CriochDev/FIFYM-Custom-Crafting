package net.crioch.fifymcc.interfaces;

import net.minecraft.component.DataComponentType;

public interface SimpleComponentMapSet {
    <T> void set(DataComponentType<T> type, T value);
}
