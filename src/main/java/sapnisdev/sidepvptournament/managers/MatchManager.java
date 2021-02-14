package sapnisdev.sidepvptournament.managers;

import lombok.Getter;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.api.events.MatchEndEvent;
import sapnisdev.sidepvptournament.api.events.MatchStartEvent;
import sapnisdev.sidepvptournament.config.Lang;
import sapnisdev.sidepvptournament.objects.Match;
import sapnisdev.sidepvptournament.objects.player.NewPlayerState;
import sapnisdev.sidepvptournament.objects.player.SavedPlayerState;
import sapnisdev.sidepvptournament.tasks.MatchTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MatchManager {
    private final TournamentPlugin plugin;
    private final MainManager mainManager;

    @Getter
    private final NewPlayerState newPlayerState;

    @Getter
    private final List<Match> matches;

    @Getter
    private final List<UUID> matchWinners;

    @Getter
    private final HashMap<String, SavedPlayerState> playerStates;

    public MatchManager(MainManager mainManager) {
        this.plugin = TournamentPlugin.getInstance();
        this.mainManager = mainManager;
        this.newPlayerState = new NewPlayerState();
        this.matches = new ArrayList<>();
        this.matchWinners = new ArrayList<>();
        this.playerStates = new HashMap<>();
    }

    public void addMatchWinner(Player player) {
        matchWinners.add(player.getUniqueId());
    }

    public void clearMatchWinners() {
        matchWinners.clear();
    }

    public void clearMatches() {
        matches.clear();
    }

    public boolean isInMatch(Player player) {
        return matches.stream().anyMatch(match -> match.toSet().contains(player));
    }

    public Match getMatch(Player player) {
        return matches.stream().filter(match -> match.toSet().contains(player)).findAny().orElse(null);
    }

    public void teleportPlayers(Match match) {
//Pameginat settot location nevsis tp
        match.getInitiator().teleport(match.getArena().getFirstLocation());
        match.getOpponent().teleport(match.getArena().getSecondLocation());
    }

    public void startMatch(Match match) {
        plugin.getServer().getPluginManager().callEvent(new MatchStartEvent(match));
        Bukkit.broadcastMessage(Lang.MATCH_START_BROADCAST.toString().replace("{initiator}", match.getInitiator().getName()).replace("{opponent}", match.getOpponent().getName()));


        teleportPlayers(match);
        matches.add(match);

        mapStates(playerStates, match);
        match.toSet().forEach(newPlayerState::modifyPlayer);

        MatchTask task = new MatchTask(match);
        match.setMatchTask(task);

        task.runTaskLater(TournamentPlugin.getInstance(), match.getDuration());
    }

    public Match getMatchById(int matchId) throws NullPointerException {
        return matches.stream().filter(match -> match.getMatchTask().getTaskId() == matchId).findAny().orElse(null);
    }

    public void endMatch(Match match) {
        plugin.getServer().getPluginManager().callEvent(new MatchEndEvent(match));
        Bukkit.broadcastMessage(Lang.MATCH_WINNER_BROADCAST.toString().replace("{winner}", match.getWinner().getName()));

        //removeTag(match.getInitiator(), match.getOpponent());
        match.getWinner().teleport(mainManager.getSpectatorArea());

        matches.remove(match);
        unmapStates(playerStates, match);

        match.reset();
    }

    public void endMatchForcefully(Match match) {
        plugin.getServer().getPluginManager().callEvent(new MatchEndEvent(match));
        Bukkit.broadcastMessage(Lang.MATCH_FORCE_END_BROADCAST.toString().replace("{initiator}", match.getInitiator().getName()).replace("{opponent}", match.getOpponent().getName()));

        //removeTag(match.getInitiator(), match.getOpponent());
        match.toSet().forEach(player -> player.teleport(mainManager.getWorldSpawn()));

        matches.remove(match);
        unmapStates(playerStates, match);

        match.reset();
        mainManager.removeFromTournament(match.getInitiator(), match.getOpponent());

        if (mainManager.getMatchWinners().size() == 0 && mainManager.getParticipants().size() < 1 && mainManager.getMatches().size() < 1) {
            Bukkit.broadcastMessage(Lang.TOURNAMENT_NO_WINNER_BROADCAST.toString());
            mainManager.getTournament().end();
        }

    }

    public void endIdleMatch(Match match) {
        plugin.getServer().getPluginManager().callEvent(new MatchEndEvent(match));
        Bukkit.broadcastMessage(Lang.MATCH_IDLE_BROADCAST.toString().replace("{initiator}", match.getInitiator().getName()).replace("{opponent}", match.getOpponent().getName()));

        //removeTag(match.getInitiator(), match.getOpponent());
        match.toSet().forEach(player -> player.teleport(mainManager.getWorldSpawn()));

        matches.remove(match);
        unmapStates(playerStates, match);

        match.reset();
        mainManager.removeFromTournament(match.getInitiator(), match.getOpponent());

        if (mainManager.getMatchWinners().size() == 0 && mainManager.getParticipants().size() < 1 && mainManager.getMatches().size() < 1) {
            Bukkit.broadcastMessage(Lang.TOURNAMENT_NO_WINNER_BROADCAST.toString());
            mainManager.getTournament().end();
        }
    }


    private void mapStates(HashMap<String, SavedPlayerState> states, Match match) {
        states.put(match.getInitiator().getName(), new SavedPlayerState(match.getInitiator()));
        states.put(match.getOpponent().getName(), new SavedPlayerState(match.getOpponent()));
    }

    private void unmapStates(HashMap<String, SavedPlayerState> states, Match match) {
        if (states.containsKey(match.getInitiator().getName())) {
            SavedPlayerState init = states.get(match.getInitiator().getName());
            init.revert();
            states.remove(match.getInitiator().getName());
        }

        if (states.containsKey(match.getOpponent().getName())) {
            SavedPlayerState op = states.get(match.getOpponent().getName());
            op.revert();
            states.remove(match.getOpponent().getName());
        }
    }
}
