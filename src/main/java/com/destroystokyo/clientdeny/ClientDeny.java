package com.destroystokyo.clientdeny;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class ClientDeny extends JavaPlugin implements Listener {
    private int MAX_VERSION = 5; // 1.7.6 - 1.7.10 || see http://wiki.vg/Protocol_version_numbers

    @Override
    public void onEnable()
    {
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if ( ((CraftPlayer) event.getPlayer() ).getHandle().playerConnection.networkManager.getVersion() > MAX_VERSION )
        {
            event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("kick-message")));
            this.getLogger().log(Level.INFO, "Kicking player " + event.getPlayer().getName() + " for using an unsupported client");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getName().equals("cd-reload") && checkPerms(sender))
        {
            this.reloadConfig();
            sender.sendMessage("ClientDeny config reloaded");
            return true;
        }

        return false;
    }

    private boolean checkPerms(CommandSender sender)
    {
        return sender.hasPermission("clientdeny.reload") || sender instanceof ConsoleCommandSender;
    }
}
