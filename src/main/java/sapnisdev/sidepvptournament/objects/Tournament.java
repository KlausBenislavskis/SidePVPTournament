package sapnisdev.sidepvptournament.objects;

import lombok.Getter;
import lombok.Setter;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.TournamentStage;
import sapnisdev.sidepvptournament.config.Lang;
import sapnisdev.sidepvptournament.managers.MainManager;
import sapnisdev.sidepvptournament.objects.player.SavedPlayerState;
import sapnisdev.sidepvptournament.tasks.PreTournamentTask;
import sapnisdev.sidepvptournament.tasks.TournamentTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;


public class Tournament {
    private final MainManager mainManager;

    @Getter @Setter
    private TournamentStage stage;

    @Getter @Setter
    private TournamentTask tournamentTask;

    @Getter
    private PreTournamentTask preTournamentTask;

    public Tournament() {
        this.mainManager = TournamentPlugin.getMainManager();
        this.stage = TournamentStage.INACTIVE;
    }

    public void start(boolean forced) {
        reset(false);
        mainManager.loadArenas();

        if(forced) {
            Bukkit.broadcastMessage(Lang.TOURNAMENT_POST_START_BROADCAST.toString());
            tournamentTask = new TournamentTask(this);
            setTournamentTask(tournamentTask);

            tournamentTask.runTaskTimer(TournamentPlugin.getInstance(), 0L, 40L);
        } else {
            preTournamentTask = new PreTournamentTask(this);
            preTournamentTask.runTaskTimer(TournamentPlugin.getInstance(), 0L, 20L);
        }
    }

    public void end() {
        if(preTournamentTask != null) {
            preTournamentTask.cancel();
        }

        if(tournamentTask != null) {
            tournamentTask.cancel();
        }

        reset(true);
        setStage(TournamentStage.INACTIVE);
    }

    private void reset(boolean end) {

        Collection<? extends Player> online = Bukkit.getOnlinePlayers();

        if(end) {
            online.stream().filter(mainManager::isInTournament).forEach(player -> {
                HashMap<String, SavedPlayerState> playerStates = mainManager.getPlayerStates();

                if(playerStates.containsKey(player.getName())) {
                    playerStates.get(player.getName()).revert();
                    playerStates.remove(player.getName());
                }

                player.teleport(mainManager.getWorldSpawn());
            });
            mainManager.clearParticipants();
            mainManager.getMatches().forEach(match -> match.getMatchTask().cancel());
        }

        mainManager.clearMatchWinners();
        mainManager.clearMatches();
        mainManager.clearArenas();
    }
}
