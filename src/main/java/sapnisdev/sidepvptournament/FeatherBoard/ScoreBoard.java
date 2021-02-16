package sapnisdev.sidepvptournament.FeatherBoard;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sapnisdev.sidepvptournament.managers.MainManager;

public class ScoreBoard {
    private MainManager manager;

    public ScoreBoard(MainManager manager) {
        this.manager = manager;
    }

    public void setScoreboardAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Integer.parseInt(manager.getTournament().getPreTournamentTask().getCountdown()) > 0) {
                FeatherBoardAPI.showScoreboard(player, "preTournament");
            } else FeatherBoardAPI.showScoreboard(player, "tournament");
        }
    }

    public void setScoreBoard(Player player) {
        if (Integer.parseInt(manager.getTournament().getPreTournamentTask().getCountdown()) > 0) {
            FeatherBoardAPI.showScoreboard(player, "preTournament");
        } else FeatherBoardAPI.showScoreboard(player, "tournament");

    }

    public void removeScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            FeatherBoardAPI.resetDefaultScoreboard(player);
        }
    }
}

