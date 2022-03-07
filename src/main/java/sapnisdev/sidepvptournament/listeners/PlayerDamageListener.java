package sapnisdev.sidepvptournament.listeners;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.managers.MainManager;
import sapnisdev.sidepvptournament.objects.player.SavedPlayerState;

import java.util.HashMap;

import static sapnisdev.sidepvptournament.CommandTournament.playerLocations;

class PlayerDamageListener implements Listener {
    private final MainManager mainManager;
    private final HashMap<Player, SavedPlayerState> playerStates;

    PlayerDamageListener(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        this.playerStates = new HashMap<>();
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
                                if(playerLocations.containsKey(player)){
                                    player.teleport(playerLocations.get(player));
                                } else {
                                    player.teleport(mainManager.getWorldSpawn());
                                }
                                playerLocations.remove(player);
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
