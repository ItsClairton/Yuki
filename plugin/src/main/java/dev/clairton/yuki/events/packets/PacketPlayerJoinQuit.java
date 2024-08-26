package dev.clairton.yuki.events.packets;

import dev.clairton.yuki.Yuki;
import dev.clairton.yuki.utils.anticheat.LogUtil;
import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketPlayerJoinQuit extends PacketListenerAbstract {

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
            // Do this after send to avoid sending packets before the PLAY state
            event.getTasksAfterSend().add(() -> Yuki.getInstance().getPlayerDataManager().addUser(event.getUser()));
        }
    }

    @Override
    public void onUserConnect(UserConnectEvent event) {
        // Player connected too soon, perhaps late bind is off
        // Don't kick everyone on reload
        if (event.getUser().getConnectionState() == ConnectionState.PLAY && !Yuki.getInstance().getPlayerDataManager().exemptUsers.contains(event.getUser())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onUserLogin(UserLoginEvent event) {
        Player player = (Player) event.getPlayer();
        if (Yuki.getInstance().getConfigManager().getConfig().getBooleanElse("debug-pipeline-on-join", false)) {
            LogUtil.info("Pipeline: " + ChannelHelper.pipelineHandlerNamesAsString(event.getUser().getChannel()));
        }
        if (player.hasPermission("grim.alerts") && player.hasPermission("grim.alerts.enable-on-join")) {
            Yuki.getInstance().getAlertManager().toggleAlerts(player);
        }
        if (player.hasPermission("grim.spectate") && Yuki.getInstance().getConfigManager().getConfig().getBooleanElse("spectators.hide-regardless", false)) {
            Yuki.getInstance().getSpectateManager().onLogin(player);
        }
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        Yuki.getInstance().getPlayerDataManager().remove(event.getUser());
        Yuki.getInstance().getPlayerDataManager().exemptUsers.remove(event.getUser());
        //Check if calling async is safe
        if (event.getUser().getProfile().getUUID() == null) return; // folia doesn't like null getPlayer()
        Player player = Bukkit.getPlayer(event.getUser().getProfile().getUUID());
        if (player != null) {
            Yuki.getInstance().getAlertManager().handlePlayerQuit(player);
            Yuki.getInstance().getSpectateManager().onQuit(player);
        }
    }
}
