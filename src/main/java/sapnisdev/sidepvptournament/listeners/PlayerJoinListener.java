package sapnisdev.sidepvptournament.listeners;

import sapnisdev.sidepvptournament.TournamentPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import sapnisdev.sidepvptournament.managers.MainManager;

import java.util.List;

class PlayerJoinListener implements Listener {
    private TournamentPlugin plugin;
    private MainManager manager;

    PlayerJoinListener(TournamentPlugin plugin, MainManager manager) {
        this.plugin = plugin;
        this.manager = manager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent evt) {
        List<String> list = plugin.getConfig().getStringList("player-logout-data");

        if(list.contains(evt.getPlayer().getUniqueId().toString())) {
            evt.getPlayer().teleport(TournamentPlugin.getMainManager().getWorldSpawn());

            list.remove(evt.getPlayer().getUniqueId().toString());
            plugin.getConfig().set("player-logout-data", list);
            plugin.getRawConfig().saveConfig();
        }
        if (manager.isTournamentRunning()){
            if(evt.getPlayer().getLocation().getWorld().getName().equals("event"))
            manager.getScoreBoard().setScoreBoard(evt.getPlayer());
        }
    }
}
