package com.zenith.network.client.handler.postoutgoing;

import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosRotPacket;
import com.zenith.feature.spectator.SpectatorUtils;
import com.zenith.network.client.ClientSession;
import com.zenith.network.registry.PostOutgoingHandler;

import static com.zenith.Shared.CACHE;

public class PostOutgoingPlayerPositionRotationHandler implements PostOutgoingHandler<ServerboundMovePlayerPosRotPacket, ClientSession> {
    @Override
    public void accept(ServerboundMovePlayerPosRotPacket packet, ClientSession session) {
        CACHE.getPlayerCache()
                .setX(packet.getX())
                .setY(packet.getY())
                .setZ(packet.getZ())
                .setYaw(packet.getYaw())
                .setPitch(packet.getPitch());
        SpectatorUtils.syncPlayerPositionWithSpectators();
    }

    @Override
    public Class<ServerboundMovePlayerPosRotPacket> getPacketClass() {
        return ServerboundMovePlayerPosRotPacket.class;
    }
}
