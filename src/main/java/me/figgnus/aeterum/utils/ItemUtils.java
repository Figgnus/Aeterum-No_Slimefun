package me.figgnus.aeterum.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemUtils {
    // UUID for creating custom player heads
    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4"); // We reuse the same "random" UUID all the time


    public static boolean isCustomItem(ItemStack item, int id){
        return item != null && item.getItemMeta() != null && item.getItemMeta().hasCustomModelData() &&
                item.getItemMeta().getCustomModelData() == id;
    }
    // Creates basic potion
    public static ItemStack createPotion(PotionType potionType){
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        assert meta != null;
        meta.setBasePotionType(potionType);
        potion.setItemMeta(meta);
        return potion;
    }
    // Methods for creating head item stack
    public static PlayerProfile getProfile(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url); // The URL to the skin, for example: https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile
        return profile;
    }
    public static ItemStack createHead(String texture, String name){
        String url = "https://textures.minecraft.net/texture/" + texture;
        PlayerProfile profile = getProfile(url);
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwnerProfile(profile);
        meta.setDisplayName(name);
        head.setItemMeta(meta);
        return head;
    }
    // Configures metadata of custom Slimefun items
    public static void configureMeta(ItemStack item, Color color, List<Map.Entry<Enchantment, Integer>> enchantments){
        if (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION){
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            assert meta != null;
            meta.setColor(color);
            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            item.setItemMeta(meta);
        }else{
            ItemMeta meta = item.getItemMeta();
            for (Map.Entry<Enchantment, Integer> enchantment : enchantments){
                assert meta != null;
                meta.addEnchant(enchantment.getKey(), enchantment.getValue(), true);
            }
            item.setItemMeta(meta);
        }
    }
}
