package sapnisdev.sidepvptournament.tasks;

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.event.block.BlockBreakEvent;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.zip.GZIPOutputStream;

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
            Bukkit.broadcastMessage(Lang.TOURNAMENT_WINNER_BROADCAST.toString().replace("{winner}", Bukkit.getPlayer(matchWinners.get(0)).getName()));

            final YamlConfiguration config = TournamentPlugin.getInstance().getConfig();
            final Player winner = Bukkit.getPlayer(matchWinners.get(0));

            if (config.getBoolean("configuration.winner-rewards.message-enabled")) {
                winner.sendMessage(Lang.TOURNAMENT_WINNER_REWARD_MESSAGE.toString());
            }

            config.getStringList("configuration.winner-rewards.reward-commands")
                    .forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("{username}", winner.getName())));
            if (config.getBoolean("configuration.winner-rewards.leaderboard-points")) {
                winner.setStatistic(Statistic.CRAFT_ITEM, Material.HOPPER, winner.getStatistic(Statistic.CRAFT_ITEM, Material.HOPPER) + 1);
            }
            tournament.end();
        }
    }
}
