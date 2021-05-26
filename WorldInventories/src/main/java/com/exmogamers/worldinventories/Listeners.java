package com.exmogamers.worldinventories;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Listeners implements Listener {
    @EventHandler
    public void onTP (PlayerTeleportEvent event) throws IOException {
        if(event.getFrom().getWorld() != event.getTo().getWorld() && !event.getTo().getWorld().getName().contains(event.getFrom().getWorld().getName())){
            // ----Get Files
            File fromYaml = new File(event.getFrom().getWorld().getWorldFolder().getPath() + "/" + "playerInventories.yml");
            if(!fromYaml.exists()){
                fromYaml.createNewFile();
            }
            File toYaml = new File(event.getTo().getWorld().getWorldFolder().getPath() + "/" + "playerInventories.yml");
            if(!toYaml.exists()){
                toYaml.createNewFile();
            }

            // ----Save Inventory in from world
            FileConfiguration config = YamlConfiguration.loadConfiguration(fromYaml);
            config.set(event.getPlayer().getUniqueId().toString()+".content", event.getPlayer().getInventory().getContents());
            config.set(event.getPlayer().getUniqueId().toString()+".armor", event.getPlayer().getInventory().getArmorContents());
            config.save(fromYaml);

            // ----Clear Players Inventory
            event.getPlayer().getInventory().clear();

            // ----Load players inventory from world
            FileConfiguration toConfig = YamlConfiguration.loadConfiguration(toYaml);
            ItemStack[] content = ((List<ItemStack>) toConfig.get(event.getPlayer().getUniqueId().toString()+".content")).toArray(new ItemStack[0]);
            ItemStack[] armor = ((List<ItemStack>) toConfig.get(event.getPlayer().getUniqueId().toString()+".armor")).toArray(new ItemStack[0]);


            // ----Set players inventory
            event.getPlayer().getInventory().setContents(content);
            event.getPlayer().getInventory().setArmorContents(armor);

        }
    }
}
