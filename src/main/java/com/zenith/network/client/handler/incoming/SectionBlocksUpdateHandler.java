package com.zenith.network.client.handler.incoming;

import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundSectionBlocksUpdatePacket;
import com.zenith.network.client.ClientSession;
import com.zenith.network.registry.AsyncIncomingHandler;
import lombok.NonNull;

import static com.zenith.Shared.CACHE;

public class SectionBlocksUpdateHandler implements AsyncIncomingHandler<ClientboundSectionBlocksUpdatePacket, ClientSession> {
    @Override
    public boolean applyAsync(@NonNull ClientboundSectionBlocksUpdatePacket packet, @NonNull ClientSession session) {
        return CACHE.getChunkCache().multiBlockUpdate(packet);
    }

    @Override
    public Class<ClientboundSectionBlocksUpdatePacket> getPacketClass() {
        return ClientboundSectionBlocksUpdatePacket.class;
    }
}
