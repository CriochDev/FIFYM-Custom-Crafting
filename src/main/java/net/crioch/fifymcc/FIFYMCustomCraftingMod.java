package net.crioch.fifymcc;

import net.crioch.fifymcc.components.FIFYDataComponentTypes;
import net.fabricmc.api.ModInitializer;

public class FIFYMCustomCraftingMod implements ModInitializer {
    @Override
    public void onInitialize() {
        FIFYDataComponentTypes.register();
    }
}
