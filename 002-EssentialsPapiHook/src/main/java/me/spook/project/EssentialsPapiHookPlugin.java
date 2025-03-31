package me.spook.project;

import me.spook.project.listeners.ChatListener;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;

// Note: Any use of "depreciation" suppressing is because AsyncPlayerChatEvent is depreciated by paper as it uses legacy text. (there is no alternative)
public class EssentialsPapiHookPlugin extends JavaPlugin implements Listener {

    @Override
    @SuppressWarnings("deprecation")
    public void onEnable() {
        getLogger().info("EssentialsPapiHook plugin enabled Unknown)");
        saveDefaultConfig();

        final EventPriority eventPriority = EventPriority.valueOf(getConfig().getString("event-listener-priority", "NORMAL").toUpperCase());
        final Listener listener = new ChatListener();
        final EventExecutor executor = (l, e) -> ((ChatListener) l).onChat((AsyncPlayerChatEvent) e);

        getServer().getPluginManager().registerEvent(
                AsyncPlayerChatEvent.class,
                listener,
                eventPriority,
                executor,
                this
        );
    }

    @Override
    public void onDisable() {
        getLogger().info("EssentialsPapiHook plugin disabled");
    }
}
