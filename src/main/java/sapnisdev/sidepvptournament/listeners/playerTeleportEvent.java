package sapnisdev.sidepvptournament.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.managers.MainManager;

public class playerTeleportEvent implements Listener {
    private MainManager mainManager;
    playerTeleportEvent(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void playerTeleportEvent(PlayerChangedWorldEvent event) {
        if (TournamentPlugin.getMainManager().isTournamentRunning()) {
            if (event.getPlayer().getLocation().getWorld().getName().equals("event")) {
                TournamentPlugin.getMainManager().getScoreBoard().setScoreBoard(event.getPlayer());
            }
            else {
                TournamentPlugin.getMainManager().getScoreBoard().removeScoreboard(event.getPlayer());
            }
        }
    }
}
