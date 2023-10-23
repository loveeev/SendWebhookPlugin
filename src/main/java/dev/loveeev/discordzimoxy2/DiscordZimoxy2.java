package dev.loveeev.discordzimoxy2;

import dev.loveeev.discordzimoxy2.command.AdminBanCommand;
import dev.loveeev.discordzimoxy2.command.admincompltor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class DiscordZimoxy2 extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getCommand("ban").setTabCompleter(new admincompltor());
        getCommand("ban").setExecutor(new AdminBanCommand("https://discord.com/api/webhooks/1166062411330101379/kWqCZIfmeAdFzU0Unp4Nd_5sOx3qCCyEbzxQdbdFHDcfOIaTAeywRPRZb5QVZZDcdAsA"));
    }
}