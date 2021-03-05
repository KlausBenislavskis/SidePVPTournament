package sapnisdev.sidepvptournament.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.managers.MainManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

class PlayerDamageListener implements Listener {
    private final MainManager mainManager;

    PlayerDamageListener(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent evt) {
        if(mainManager.isTournamentRunning()) {
            if (!(evt.getEntity() instanceof Player)) {
                return;
            }

            if(!mainManager.isInTournament((Player) evt.getEntity())) {
                return;
            }

            Player player = (Player) evt.getEntity();

            switch (mainManager.getTournament().getStage()) {
                case WAITING:
                    if (player.getHealth() <= evt.getFinalDamage()) {
                        evt.setCancelled(true);
                        evt.getEntity().teleport(mainManager.getSpectatorArea());
                    }
                    break;
                case ACTIVE:
                    mainManager.getMatches().stream()
                            .filter(match -> match.toSet().contains(player))
                            .findAny()
                            .ifPresent(match -> {
                                if (player.getHealth() > evt.getFinalDamage()) {
                                    return;
                                }
                                evt.setCancelled(true);
                                player.teleport(mainManager.getWorldSpawn());
                                match.setWinner(player.getName().equalsIgnoreCase(match.getInitiator().getName()) ? match.getOpponent() : match.getInitiator());
                                Player winner = player.getName().equalsIgnoreCase(match.getInitiator().getName()) ? match.getOpponent() : match.getInitiator();
                                winner.setStatistic(Statistic.CRAFT_ITEM, Material.HOPPER, winner.getStatistic(Statistic.CRAFT_ITEM, Material.HOPPER) + 1);
                                match.setLoser(player.getName().equalsIgnoreCase(match.getInitiator().getName()) ? match.getInitiator() : match.getOpponent());
                                mainManager.removeFromTournament(player);
                                mainManager.addMatchWinner(match.getWinner());
                                mainManager.endMatch(match);
                            });
                    break;
                default:
                    break;
            }
        }
    }
}
