package com.zenith.feature.actionlimiter.handlers.inbound;

import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundClientCommandPacket;
import com.zenith.network.registry.IncomingHandler;
import com.zenith.network.server.ServerConnection;

import static com.zenith.Shared.CONFIG;

public class ALClientCommandHandler implements IncomingHandler<ServerboundClientCommandPacket, ServerConnection> {
    @Override
    public boolean apply(final ServerboundClientCommandPacket packet, final ServerConnection session) {
        if (CONFIG.client.extra.actionLimiter.allowRespawn)
            return true;
        session.disconnect("ActionLimiter: Respawn not allowed");
        return false;
    }
}
