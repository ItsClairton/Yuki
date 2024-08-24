package dev.clairton.yuki.commands;

import dev.clairton.yuki.GrimAPI;
import dev.clairton.yuki.utils.anticheat.LogUtil;
import dev.clairton.yuki.utils.anticheat.MessageUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

@CommandAlias("grim|grimac")
public class GrimSendAlert extends BaseCommand {
    @Subcommand("sendalert")
    @CommandPermission("grim.sendalert")
    public void sendAlert(String string) {
        string = MessageUtil.format(string);

        for (Player bukkitPlayer : GrimAPI.INSTANCE.getAlertManager().getEnabledAlerts()) {
            bukkitPlayer.sendMessage(string);
        }

        if (GrimAPI.INSTANCE.getConfigManager().getConfig().getBooleanElse("alerts.print-to-console", true)) {
            LogUtil.console(string); // Print alert to console
        }
    }
}
