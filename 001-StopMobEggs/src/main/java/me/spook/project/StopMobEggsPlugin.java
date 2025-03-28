package me.spook.project;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class StopMobEggsPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info("StopMobEggs plugin enabled (Thread ID: 1353989296759246879)");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("StopMobEggs plugin disabled");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMobEggPlace(PlayerInteractEvent event) {
        final Player player;

        // You can add permission checks, and other specific checks here!
        if ((player = event.getPlayer()).isOnline() && event.getAction() == Action.RIGHT_CLICK_BLOCK
                && event.hasItem() && event.getItem() != null && event.getItem().getType().name().endsWith("_SPAWN_EGG")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You are not allowed to place mob eggs!");
        }
    }
}
