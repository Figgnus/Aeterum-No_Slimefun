package me.figgnus.aeterum.listeners.poseidon;

import me.figgnus.aeterum.AeterumX;
import me.figgnus.aeterum.utils.PermissionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SeaHorseAbilityListener implements Listener {
    private final AeterumX plugin;

    public SeaHorseAbilityListener(AeterumX plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Check if the player is riding a horse
        if (player.isInsideVehicle() && player.getVehicle() instanceof Horse) {
            Horse horse = (Horse) player.getVehicle();
            String metadataValue = plugin.getEntityMetadata(horse, PoseidonWhistleListener.HORSE_KEY);

            // Check if the horse has the Frost Walker ability
            if ("true".equals(metadataValue)) {
                // Get the horse's location
                Location horseLocation = horse.getLocation();
                horseLocation.getWorld().spawnParticle(Particle.SNOWFLAKE, horseLocation.clone().add(0, 0.5, 0), 1, 0.2, 0.05, 0.2, 0.01);
                horseLocation.getWorld().spawnParticle(Particle.CLOUD, horseLocation.clone().add(0, 0.5, 0), 1, 0.2, 0.05, 0.2, 0.01);
                // Define a 2x2 area around the horse
                int[][] offsets = {
                        {0, 0},
                        {1, 0},
                        {0, 1},
                        {1, 1}
                };
                for (int[] offset : offsets) {
                    // Get the block under each position in the 2x2 area
                    Block blockUnder = horseLocation.clone().add(offset[0], -1, offset[1]).getBlock();

                    // Check if the block under the horse is water
                    if (blockUnder.getType() == Material.WATER) {
                        if (!player.hasPermission(PermissionUtils.poseidonHorseAbility)) {
                            player.sendMessage(PermissionUtils.ridingPermissionMessage);
                            return;
                        }
                        // Convert water to ice
                        blockUnder.setType(Material.FROSTED_ICE);

                        // Spawn particle effect when converting to water
                        blockUnder.getWorld().spawnParticle(Particle.SNOWFLAKE, blockUnder.getLocation().add(0.5, 0.5, 0.5), 10, 0.2, 0.2, 0.2, 0.02);


                        // Schedule a task to revert ice back to water after a delay
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (blockUnder.getType() == Material.FROSTED_ICE) {
                                    // Spawn particle effect when reverting back to water
                                    blockUnder.getWorld().spawnParticle(Particle.CLOUD, blockUnder.getLocation().add(0.5, 0.5, 0.5), 10, 0.2, 0.2, 0.2, 0.02);
                                    blockUnder.setType(Material.WATER);
                                }
                            }
                        }.runTaskLater(plugin, 100); // 100 ticks = 5 seconds
                    }
                }
            }
        }
    }
}
