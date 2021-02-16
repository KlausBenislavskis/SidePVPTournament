package sapnisdev.sidepvptournament.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.managers.MainManager;
import sapnisdev.sidepvptournament.objects.Tournament;
import sun.applet.Main;

public class playerTeleportEvent implements Listener {
    private MainManager mainManager;
    playerTeleportEvent(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void playerTeleportEvent(PlayerChangedWorldEvent event) {
        Bukkit.broadcastMessage(event.getPlayer().getLocation().getWorld().toString());
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
