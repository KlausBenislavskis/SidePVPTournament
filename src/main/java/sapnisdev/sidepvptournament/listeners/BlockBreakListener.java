package sapnisdev.sidepvptournament.listeners;

import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.managers.MainManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

class BlockBreakListener implements Listener {
    private final MainManager mainManager;

    BlockBreakListener(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent evt) {
        if(mainManager.isTournamentRunning()) {
            if(mainManager.isInTournament(evt.getPlayer()) && !evt.getPlayer().hasPermission("sidetournament.blockbreak.bypass")) {
                evt.setCancelled(true);
            }
        }
    }
}
