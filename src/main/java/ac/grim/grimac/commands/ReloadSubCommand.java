package ac.grim.grimac.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandAlias("ac")
public class ReloadSubCommand extends BaseCommand {

    @Subcommand("reload")
    @CommandPermission("ac.reload")
    public void onReload(CommandSender sender) {
        //reload config
        try {
            GrimAPI.INSTANCE.getExternalAPI().reload();
        } catch (RuntimeException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return;
        }

        sender.sendMessage(MessageUtil.format(GrimAPI.INSTANCE.getConfigManager().getConfig().getString("reload")));
    }

}
