package me.rezcom.pseudohardcore;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BreakBlock implements Listener {
    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event){
        Block blockBroken = event.getBlock();

        if (blockBroken.getType() == Material.DIAMOND_ORE){
            System.out.println("SOMEONE FOUND SOME DIAMOND! Uh oh!");
            event.setCancelled(true);

            blockBroken.setType(Material.AIR);
            ItemStack coals = new ItemStack(Material.COAL, 2);
            blockBroken.getWorld().dropItemNaturally(blockBroken.getLocation(), coals);


        }
    }
}
