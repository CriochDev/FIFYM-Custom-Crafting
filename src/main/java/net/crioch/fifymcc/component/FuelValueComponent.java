package net.crioch.fifymcc.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.item.TooltipType;
import net.minecraft.item.Item;
import net.minecraft.item.TooltipAppender;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.Consumer;

public record FuelValueComponent(int fuelValue) implements TooltipAppender {
    public static final Codec<FuelValueComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codecs.POSITIVE_INT.fieldOf("burn_time").forGetter(FuelValueComponent::fuelValue)
            ).apply(instance, FuelValueComponent::new)
    );

    public static final PacketCodec<ByteBuf, FuelValueComponent> PACKET_CODEC = PacketCodecs.VAR_INT.xmap(FuelValueComponent::new, FuelValueComponent::fuelValue);

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        tooltip.accept(Text.translatable("fifymcc.tooltip.burn_time", this.fuelValue));
    }
}
