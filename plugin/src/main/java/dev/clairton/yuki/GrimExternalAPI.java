package dev.clairton.yuki;

import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.api.GrimAbstractAPI;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.alerts.AlertManager;
import dev.clairton.yuki.manager.init.Initable;
import dev.clairton.yuki.player.GrimPlayer;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

//This is used for grim's external API. It has its own class just for organization.
public class GrimExternalAPI implements GrimAbstractAPI, Initable {

    private final Yuki api;

    public GrimExternalAPI(Yuki api) {
        this.api = api;
    }

    @Nullable
    @Override
    public GrimUser getGrimUser(Player player) {
        return api.getPlayerDataManager().getPlayer(player);
    }

    @Override
    public void setServerName(String name) {
        variableReplacements.put("%server%", user -> name);
    }

    @Getter
    private final Map<String, Function<GrimUser, String>> variableReplacements = new ConcurrentHashMap<>();

    @Getter private final Map<String, String> staticReplacements = new ConcurrentHashMap<>();

    public String replaceVariables(GrimUser user, String content, boolean colors) {
        if (colors) content = ChatColor.translateAlternateColorCodes('&', content);
        for (Map.Entry<String, String> entry : staticReplacements.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, Function<GrimUser, String>> entry : variableReplacements.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue().apply(user));
        }
        return content;
    }

    @Override
    public void registerVariable(String string, Function<GrimUser, String> replacement) {
        variableReplacements.put(string, replacement);
    }

    @Override
    public void registerVariable(String variable, String replacement) {
        staticReplacements.put(variable, replacement);
    }

    @Override
    public String getGrimVersion() {
        PluginDescriptionFile description = Yuki.getInstance().getPlugin().getDescription();
        return description.getVersion();
    }

    @Override
    public void registerFunction(String key, Function<Object, Object> function) {

    }

    @Override
    public Function<Object, Object> getFunction(String key) {
        return null;
    }

    @Override
    public void reload() {
        Yuki.getInstance().getConfigManager().reload();
        //Reload checks for all players
        for (GrimPlayer grimPlayer : Yuki.getInstance().getPlayerDataManager().getEntries()) {
            ChannelHelper.runInEventLoop(grimPlayer.user.getChannel(), () -> {
                grimPlayer.onReload();
                grimPlayer.updatePermissions();
                grimPlayer.punishmentManager.reload();
                for (AbstractCheck value : grimPlayer.checkManager.allChecks.values()) {
                    value.reload();
                }
            });
        }
        //Restart
        Yuki.getInstance().getDiscordManager().start();
        Yuki.getInstance().getSpectateManager().start();
        Yuki.getInstance().getExternalAPI().start();
    }

    @Override
    public AlertManager getAlertManager() {
        return Yuki.getInstance().getAlertManager();
    }

    @Override
    public void start() {
        variableReplacements.put("%player%", GrimUser::getName);
        variableReplacements.put("%uuid%", user -> user.getUniqueId().toString());
        variableReplacements.put("%ping%", user -> user.getTransactionPing() + "");
        variableReplacements.put("%brand%", GrimUser::getBrand);
        variableReplacements.put("%h_sensitivity%", user -> ((int) Math.round(user.getHorizontalSensitivity() * 200)) + "");
        variableReplacements.put("%v_sensitivity%", user -> ((int) Math.round(user.getVerticalSensitivity() * 200)) + "");
        variableReplacements.put("%fast_math%", user -> !user.isVanillaMath() + "");
        variableReplacements.put("%tps%", user -> String.format("%.2f", SpigotReflectionUtil.getTPS()));
        variableReplacements.put("%version%", GrimUser::getVersionName);
        variableReplacements.put("%prefix%", user -> ChatColor.translateAlternateColorCodes('&', Yuki.getInstance().getConfigManager().getConfig().getStringElse("prefix", "&bGrim &8»")));
    }
}
