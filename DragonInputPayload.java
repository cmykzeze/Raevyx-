package com.dragonriding;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Sent client -> server while riding.
 * Held keys: beam (G), boost (Sprint/Accelerate), up (Jump/Ascend), down (Descend).
 * One-shot action: see ACTION_* constants (0 = none).
 */
public record DragonInputPayload(boolean beam, boolean boost, boolean up, boolean down, int action)
        implements CustomPacketPayload {
    public static final int ACTION_NONE = 0;
    public static final int ACTION_ROAR = 1;          // R
    public static final int ACTION_STORM = 2;         // H
    public static final int ACTION_TOGGLE_MELEE = 3;  // X
    public static final int ACTION_FLEX = 4;          // Ctrl+4
    public static final int ACTION_ATTACK = 5;        // Left click (bite / horn gore)

    public static final CustomPacketPayload.Type<DragonInputPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(DragonRiding.MOD_ID, "dragon_input"));

    public static final StreamCodec<ByteBuf, DragonInputPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, DragonInputPayload::beam,
            ByteBufCodecs.BOOL, DragonInputPayload::boost,
            ByteBufCodecs.BOOL, DragonInputPayload::up,
            ByteBufCodecs.BOOL, DragonInputPayload::down,
            ByteBufCodecs.VAR_INT, DragonInputPayload::action,
            DragonInputPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
