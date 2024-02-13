package com.zenith.feature.actionlimiter.handlers.inbound;

import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosPacket;
import com.zenith.module.impl.ActionLimiter;
import com.zenith.network.registry.PacketHandler;
import com.zenith.network.server.ServerConnection;
import com.zenith.util.math.MathHelper;

import static com.zenith.Shared.CONFIG;
import static com.zenith.Shared.MODULE_MANAGER;

public class ALMovePlayerPosHandler implements PacketHandler<ServerboundMovePlayerPosPacket, ServerConnection> {
    @Override
    public ServerboundMovePlayerPosPacket apply(final ServerboundMovePlayerPosPacket packet, final ServerConnection session) {
        if (MODULE_MANAGER.get(ActionLimiter.class).bypassesLimits(session)) return packet;
        if (CONFIG.client.extra.actionLimiter.allowMovement)
            return packet;
        if (packet.getY() <= CONFIG.client.extra.actionLimiter.movementMinY) {
            session.disconnect("ActionLimiter: Movement not allowed");
            return null;
        }
        if (MathHelper.distance2d(CONFIG.client.extra.actionLimiter.movementHomeX,
                                  CONFIG.client.extra.actionLimiter.movementHomeZ,
                                  packet.getX(),
                                  packet.getZ()) > CONFIG.client.extra.actionLimiter.movementDistance) {
            session.disconnect("ActionLimiter: Movement not allowed");
            return null;
        }
        return packet;
    }
}
