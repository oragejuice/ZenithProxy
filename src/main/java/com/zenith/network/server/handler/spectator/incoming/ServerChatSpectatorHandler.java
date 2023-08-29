package com.zenith.network.server.handler.spectator.incoming;

import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundSetCameraPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundRemoveEntitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundRemoveMobEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import com.zenith.feature.spectator.SpectatorUtils;
import com.zenith.network.registry.IncomingHandler;
import com.zenith.network.server.ServerConnection;

import static com.zenith.Shared.CACHE;
import static java.util.Arrays.asList;

public class ServerChatSpectatorHandler implements IncomingHandler<ServerboundChatPacket, ServerConnection> {

    @Override
    public boolean apply(ServerboundChatPacket packet, ServerConnection session) {
        // todo: implement with new chat packets and components
        if (packet.getMessage().toLowerCase().startsWith("!help")) {
//            session.send(new ClientboundSystemChatPacket("§9§lSpectator commands:", true));
//            session.send(new ClientboundSystemChatPacket("§2Prefix : \"!\"", true));
//            session.send(new ClientboundSystemChatPacket("", true));
//            session.send(new ClientboundSystemChatPacket("§7§chelp §7- §8Display help menu", true));
//            session.send(new ClientboundSystemChatPacket("§7§cplayercam §7- §8Set camera to the player", true));
//            session.send(new ClientboundSystemChatPacket("§7§cetoggle §7- §8Hide your entity from yourself", true));
//            session.send(new ClientboundSystemChatPacket("§7§ce §7- §8List spectator entities. Change with \"!e <entity>\"", true));
        } else if (packet.getMessage().toLowerCase().startsWith("!m")) {
//            if (CONFIG.server.spectator.spectatorPublicChatEnabled) {
//                session.getProxy().getClient().send(new ServerboundChatPacket(packet.getMessage().substring(2).trim()));
//            } else {
//                session.send(new ClientboundSystemChatPacket("§cSpectator chat disabled§r", true));
//            }
        } else if (packet.getMessage().toLowerCase().startsWith("!etoggle")) {
//            session.setShowSelfEntity(!session.isShowSelfEntity());
//            if (session.isShowSelfEntity()) {
//                session.send(session.getEntitySpawnPacket());
//                session.send(session.getEntityMetadataPacket());
//            } else {
//                session.send(new ClientboundRemoveEntitiesPacket(new int[]{session.getSpectatorEntityId()}));
//            }
        } else if (packet.getMessage().toLowerCase().startsWith("!e")) {
//            String entityId = packet.getMessage().substring(2).trim();
//            boolean spectatorEntitySet = session.setSpectatorEntity(entityId);
//            if (spectatorEntitySet) {
//                // respawn entity on all connections
//                session.getProxy().getActiveConnections().forEach(connection -> {
//                    connection.send(new ClientboundRemoveEntitiesPacket(new int[]{session.getSpectatorEntityId()}));
//                    if (!connection.equals(session) || session.isShowSelfEntity()) {
//                        connection.send(session.getEntitySpawnPacket());
//                        connection.send(session.getEntityMetadataPacket());
//                    }
//                });
//                session.send(new ClientboundSystemChatPacket("§9Updated entity to: " + entityId + "§r", true));
//            } else {
//                session.send(new ClientboundSystemChatPacket("§cNo entity found with id: " + entityId + "§r", true));
//                session.send(new ClientboundSystemChatPacket("§cValid id's: " + String.join(", ", SpectatorEntityRegistry.getEntityIdentifiers()) + "§r", true));
//            }
        } else if (packet.getMessage().toLowerCase().startsWith("!playercam")) {
            session.setPlayerCam(!session.isPlayerCam());
            if (session.isPlayerCam()) {
                session.send(new ClientboundSetCameraPacket(CACHE.getPlayerCache().getEntityId()));
                session.getProxy().getActiveConnections().forEach(connection -> {
                    connection.send(new ClientboundRemoveEntitiesPacket(new int[]{session.getSpectatorEntityId()}));
                });
            } else {
                session.send(new ClientboundSetCameraPacket(session.getSpectatorSelfEntityId()));
                SpectatorUtils.syncSpectatorPositionToPlayer(session);
            }
        } else if (packet.getMessage().toLowerCase().startsWith("!cleareffects")) {
            CACHE.getPlayerCache().getThePlayer().getPotionEffectMap().clear();
            asList(Effect.values()).forEach(effect -> {
                session.send(new ClientboundRemoveMobEffectPacket(CACHE.getPlayerCache().getEntityId(), effect));
            });
//            session.send(new ClientboundSystemChatPacket("§9Cleared effects§r", true));
        } else {
//            session.getProxy().getActiveConnections().forEach(connection -> {
//                connection.send(new ClientboundSystemChatPacket("§c" + session.getProfileCache().getProfile().getName() + " > " + packet.getMessage() + "§r", true));
//            });
        }
        return false;
    }

    @Override
    public Class<ServerboundChatPacket> getPacketClass() {
        return ServerboundChatPacket.class;
    }
}
