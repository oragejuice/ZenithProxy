package com.zenith.client.handler.incoming;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerSetExperiencePacket;
import com.zenith.client.PorkClientSession;
import com.zenith.util.handler.HandlerRegistry;

import static com.zenith.util.Constants.CACHE;

public class PlayerSetExperienceHandler implements HandlerRegistry.AsyncIncomingHandler<ServerPlayerSetExperiencePacket, PorkClientSession> {
    @Override
    public boolean applyAsync(ServerPlayerSetExperiencePacket packet, PorkClientSession session) {
        CACHE.getPlayerCache().getThePlayer()
                .setTotalExperience(packet.getTotalExperience())
                .setLevel(packet.getLevel())
                .setExperience(packet.getSlot());
        return true;
    }

    @Override
    public Class<ServerPlayerSetExperiencePacket> getPacketClass() {
        return ServerPlayerSetExperiencePacket.class;
    }
}