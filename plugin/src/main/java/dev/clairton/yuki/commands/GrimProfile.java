package dev.clairton.yuki.commands;

import dev.clairton.yuki.Yuki;
import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.utils.anticheat.MessageUtil;
import dev.clairton.yuki.utils.anticheat.MultiLibUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("grim|grimac")
public class GrimProfile extends BaseCommand {
    @Subcommand("profile")
    @CommandPermission("grim.profile")
    @CommandCompletion("@players")
    public void onConsoleDebug(CommandSender sender, OnlinePlayer target) {
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;

        // Short circuit due to minimum java requirements for MultiLib
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18) && MultiLibUtil.isExternalPlayer(target.getPlayer())) {
            String alertString = Yuki.getInstance().getConfigManager().getConfig().getStringElse("player-not-this-server", "%prefix% &cThis player isn't on this server!");
            sender.sendMessage(MessageUtil.format(alertString));
            return;
        }

        GrimPlayer grimPlayer = Yuki.getInstance().getPlayerDataManager().getPlayer(target.getPlayer());
        if (grimPlayer == null) {
            String message = Yuki.getInstance().getConfigManager().getConfig().getStringElse("player-not-found", "%prefix% &cPlayer is exempt or offline!");
            sender.sendMessage(MessageUtil.format(message));
            return;
        }

        for (String message : Yuki.getInstance().getConfigManager().getConfig().getStringList("profile")) {
            message = Yuki.getInstance().getExternalAPI().replaceVariables(grimPlayer, message, true);
            sender.sendMessage(message);
        }
    }
}
