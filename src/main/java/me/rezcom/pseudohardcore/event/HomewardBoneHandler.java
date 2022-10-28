package me.rezcom.pseudohardcore.event;

import me.rezcom.pseudohardcore.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;


public class HomewardBoneHandler implements Listener {

    public static final NamespacedKey homewardBoneKey = new NamespacedKey(Main.thisPlugin, "homewardBoneKey");

    private static final TextComponent homewardBoneName =
            Component.text("Homeward Bone").color(TextColor.color(0xfa6c48)).decoration(TextDecoration.ITALIC,true);

    private static final ArrayList<Component> homewardBoneLore = new ArrayList<>(Arrays.asList(
            Component.text("Bone fragment reduced to white ash.").color(TextColor.color(0xFACBA2)),
            Component.text("Return to the beginning of this world.").color(TextColor.color(0xFACBA2)),
            Component.text(" "),
            Component.text("Bonfires are sustained by the bones of").color(TextColor.color(0xe9a800)),
            Component.text("the Undead. In rare cases, their previous").color(TextColor.color(0xe9a800)),
            Component.text("owner's strong urge to seek bonfires").color(TextColor.color(0xe9a800)),
            Component.text("enchants their bones with a homeward instinct.").color(TextColor.color(0xe9a800))
    ));

    private static Map<UUID,Long> damagedPlayers = new HashMap<>();

    public static ItemStack homewardBoneStack;

    public static void initializeHomewardBone(){
        // Please call this early in main.

        ItemStack itemStack = new ItemStack(Material.BONE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.displayName(homewardBoneName);
        itemMeta.lore(homewardBoneLore);

        itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 0, false);

        PersistentDataContainer bonePDC = itemMeta.getPersistentDataContainer();
        bonePDC.set(homewardBoneKey, PersistentDataType.INTEGER,1);

        itemStack.setItemMeta(itemMeta);

        homewardBoneStack = itemStack;

    }

    public static boolean isItemHomewardBone(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null){return false;}
        PersistentDataContainer metaPDC = itemMeta.getPersistentDataContainer();

        return metaPDC.get(homewardBoneKey,PersistentDataType.INTEGER) != null;
    }

    @EventHandler
    void createBoneAnvilEvent(PrepareAnvilEvent event){
        Main.logger.log(Level.INFO,"Prepare anvil event");
        AnvilInventory anvilInventory = event.getInventory();
        ItemStack firstItem = anvilInventory.getFirstItem();
        ItemStack secondItem = anvilInventory.getSecondItem();

        if (firstItem == null || secondItem == null){
            Main.logger.log(Level.INFO,"BOTH ARE NULL");
            return;
        }

        Main.logger.log(Level.INFO,"FIRST: " + firstItem + "\nSECOND: " + secondItem);

        if (firstItem.getType() == Material.BONE && firstItem.getAmount() == 1 && secondItem.getType() == Material.ENDER_EYE && secondItem.getAmount() == 1){
            Main.logger.log(Level.INFO,"BOTH ARE GOOD");
            anvilInventory.setRepairCost(5);
            event.setResult(homewardBoneStack);

        }
    }

    @EventHandler
    void onPlayerTakeDamage(EntityDamageEvent event){
        if (!(event.getEntity() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity();
        UUID uuid = player.getUniqueId();

        long damageTime = System.currentTimeMillis();

        damagedPlayers.put(uuid, damageTime + 180000);
    }

    @EventHandler
    void onHomewardBoneUsage(PlayerInteractEvent event){
        if (!event.getAction().isRightClick()){
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        EquipmentSlot equipmentSlot = event.getHand();
        PlayerInventory playerInventory = player.getInventory();

        ItemStack item = equipmentSlot == EquipmentSlot.HAND ?  playerInventory.getItemInMainHand() : playerInventory.getItemInOffHand();
        if (!isItemHomewardBone(item)){
            return;
        }

        if (damagedPlayers.containsKey(uuid)){
            Instant now = Instant.ofEpochMilli(System.currentTimeMillis());
            Instant rTime = Instant.ofEpochMilli(damagedPlayers.get(uuid));

            if (!now.isAfter(rTime)){
                // CAN'T USE IT!
                long timeDiff = now.until(rTime, ChronoUnit.SECONDS);
                TextComponent message = Component.text("Cannot use Homeward Bone for " + timeDiff + " seconds.").color(TextColor.color(0xFACBA2));
                player.sendMessage(message);
                player.playSound(player.getLocation(),Sound.ENTITY_COW_HURT,1.0f,1.4f);
                return;
            }

        }

        // WE CAN USE IT!
        player.teleport(player.getWorld().getSpawnLocation());
        item.subtract();
        TextComponent teleportMessage = Component.text("You are sent homeward...").color(TextColor.color(0xFACBA2));
        player.sendMessage(teleportMessage);
        player.playSound(player.getLocation(),Sound.ENTITY_BLAZE_AMBIENT,1.0f,0.4f);
        player.playSound(player.getLocation(),Sound.ENTITY_BLAZE_BURN,0.6f, 0.4f);
        damagedPlayers.put(uuid,System.currentTimeMillis() + 10000);

    }

}
