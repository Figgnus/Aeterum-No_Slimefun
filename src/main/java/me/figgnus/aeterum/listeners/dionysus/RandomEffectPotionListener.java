package me.figgnus.aeterum.listeners.dionysus;

import me.figgnus.aeterum.Plugin;
import me.figgnus.aeterum.items.CustomItems;
import me.figgnus.aeterum.utils.GodUtils;
import me.figgnus.aeterum.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class RandomEffectPotionListener implements Listener {
    private final Plugin plugin;
    private final List effects = List.of(PotionEffectType.SPEED,
            PotionEffectType.JUMP_BOOST,
            PotionEffectType.RESISTANCE,
            PotionEffectType.STRENGTH,
            PotionEffectType.POISON,
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.BLINDNESS,
            PotionEffectType.HASTE,
            PotionEffectType.LUCK);

    public RandomEffectPotionListener(Plugin plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (ItemUtils.isCustomItem(item, CustomItems.RANDOM_EFFECT_POTION_ID)){
            if (!player.hasPermission(GodUtils.dionysusPermission)){
                player.sendMessage(GodUtils.permissionItemMessage);
                return;
            }
            applyRandomEffect(player);
        }
    }

    private void applyRandomEffect(Player player) {
        Random random = new Random();
        int index = random.nextInt(0, effects.size());
        int duration = random.nextInt(20, 600);
        int amplifier = random.nextInt(1, 3);
        player.addPotionEffect(new PotionEffect((PotionEffectType) effects.get(index), duration, amplifier, false));
    }
}
