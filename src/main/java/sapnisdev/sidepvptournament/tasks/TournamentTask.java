package sapnisdev.sidepvptournament.tasks;

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.Statistic;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.TournamentStage;
import sapnisdev.sidepvptournament.config.Lang;
import sapnisdev.sidepvptournament.managers.MainManager;
import sapnisdev.sidepvptournament.objects.Arena;
import sapnisdev.sidepvptournament.objects.Match;
import sapnisdev.sidepvptournament.objects.Tournament;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static sapnisdev.sidepvptournament.Utils.ChatUtil.color;

public class TournamentTask extends BukkitRunnable {
    private final MainManager mainManager;
    private final Tournament tournament;
    private final List<UUID> matchWinners, participants;

    public TournamentTask(Tournament tournament) {
        this.mainManager = TournamentPlugin.getMainManager();
        this.tournament = tournament;
        this.matchWinners = mainManager.getMatchWinners();
        this.participants = mainManager.getParticipants();
        Collections.shuffle(participants);
        mainManager.getTournamentManager().getOriginalParticipants().addAll(participants);
        tournament.setStage(TournamentStage.ACTIVE);
    }

    @SneakyThrows
    public void run() {
        if (mainManager.getAvailableArena() == null) {
            return;
        }

        Arena arena = mainManager.getAvailableArena();

        if (participants.size() > 1) {
            Match match = new Match(Bukkit.getPlayer(participants.remove(0)), Bukkit.getPlayer(participants.remove(0)));
            match.setArena(arena);

            arena.setOccupied(true);
            mainManager.startMatch(match);

        } else if (participants.size() == 1) {
            matchWinners.add(participants.remove(0));
        } else if (matchWinners.size() > 1 && participants.size() < 1) {

            participants.addAll(matchWinners);
            matchWinners.clear();
        } else if (matchWinners.size() == 1 && participants.size() < 1 && mainManager.getMatches().size() == 0) {
            final YamlConfiguration config = TournamentPlugin.getInstance().getConfig();
            List<?> mesage = config.getList("messages.tournament-winner-broadcast");
            for (Object o : mesage) {
                Bukkit.broadcastMessage(color((String) o).replace("{winner}", Bukkit.getPlayer(matchWinners.get(0)).getName()));
            }


            final Player winner = Bukkit.getPlayer(matchWinners.get(0));
            TournamentPlugin.getMainManager().getScoreBoard().removeScoreboards();
            if (config.getBoolean("configuration.winner-rewards.message-enabled")) {

                winner.sendMessage(Lang.TOURNAMENT_WINNER_REWARD_MESSAGE.toString());
            }

            String s = String.valueOf(config.get("configuration.winner-rewards.reward-command"));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("{username}", winner.getName()));
            if (config.getBoolean("configuration.winner-rewards.leaderboard-points")) {
                winner.setStatistic(Statistic.CRAFT_ITEM, Material.HOPPER, winner.getStatistic(Statistic.CRAFT_ITEM, Material.HOPPER) + 5);
            }
            tournament.end();
        }
    }
}