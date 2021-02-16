package sapnisdev.sidepvptournament.FeatherBoard;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.TournamentStage;
import sapnisdev.sidepvptournament.managers.MainManager;

public class ScoreBoard {
    private MainManager manager;

    public ScoreBoard(MainManager manager) {
        this.manager = manager;
    }

    public void setScoreboardAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (TournamentPlugin.getMainManager().getTournament().getStage().equals(TournamentStage.WAITING)) {
                FeatherBoardAPI.showScoreboard(player, "pretournament");
            } else if (TournamentPlugin.getMainManager().getTournament().getStage().equals(TournamentStage.ACTIVE)) {
                FeatherBoardAPI.showScoreboard(player, "tournament");
            } else if (TournamentPlugin.getMainManager().getTournament().getStage().equals(TournamentStage.INACTIVE)) {
                Bukkit.broadcastMessage("Inactive");
            }
        }
    }

    public void setScoreBoard(Player player) {
        if (TournamentPlugin.getMainManager().getTournament().getStage().equals(TournamentStage.WAITING)) {
            FeatherBoardAPI.showScoreboard(player, "pretournament");
        } else if (TournamentPlugin.getMainManager().getTournament().getStage().equals(TournamentStage.ACTIVE)) {
            FeatherBoardAPI.showScoreboard(player, "tournament");
        } else if (TournamentPlugin.getMainManager().getTournament().getStage().equals(TournamentStage.INACTIVE)) {
            Bukkit.broadcastMessage("Inactive");
        }
    }

    public void removeScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            FeatherBoardAPI.resetDefaultScoreboard(player);
        }
    }

    public void removeScoreboard(Player player) {
        FeatherBoardAPI.resetDefaultScoreboard(player);
    }
}


