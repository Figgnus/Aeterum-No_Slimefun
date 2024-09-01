package me.figgnus.aeterum.listeners.poseidon;

import me.figgnus.aeterum.AeterumX;
import me.figgnus.aeterum.utils.PermissionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class DolphinGraceListener implements Listener, CommandExecutor {
    public HashSet<UUID> enabledPlayers = new HashSet<>();
    private final AeterumX plugin;

    public DolphinGraceListener(AeterumX plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "This command can be only used by players");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission(PermissionUtils.poseidonTogglePermission)){
            player.sendMessage(PermissionUtils.permissionCommandMessage);
            return true;
        }
        UUID playerId = player.getUniqueId();
        if (enabledPlayers.contains(playerId)) {
            enabledPlayers.remove(playerId);
            player.sendMessage(ChatColor.GREEN + "Dolphin's Grace OFF.");
            getLogger().info("DG disabled for " + player.getUniqueId());
        } else {
            if (!enabledPlayers.contains(playerId)){
                enabledPlayers.add(playerId);
                player.sendMessage(ChatColor.GREEN + "Dolphin's Grace ON.");
                getLogger().info("DG enabled for " + player.getUniqueId());
            }
        }
        return true;
    }
    @EventHandler
    public void onWaterEnter(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Material blockType = player.getLocation().getBlock().getType();
        if (player.hasPermission(PermissionUtils.poseidonTogglePermission) && enabledPlayers.contains(player.getUniqueId())){
            if (blockType == Material.WATER || blockType == Material.BUBBLE_COLUMN){
                player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 100, 1, true, true));
            }
        }
    }
}
