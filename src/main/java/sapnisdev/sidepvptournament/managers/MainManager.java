package sapnisdev.sidepvptournament.managers;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.util.io.BukkitObjectInputStream;
import sapnisdev.sidepvptournament.FeatherBoard.ScoreBoard;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.objects.Arena;
import sapnisdev.sidepvptournament.objects.Match;
import sapnisdev.sidepvptournament.objects.Tournament;
import sapnisdev.sidepvptournament.objects.player.SavedPlayerState;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import be.maximvdw.featherboard.api.FeatherBoardAPI;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainManager {
    @Getter
    private final TournamentManager tournamentManager;

    @Getter
    private final MatchManager matchManager;

    @Getter
    private final ArenaManager arenaManager;
    @Getter
    private ScoreBoard scoreBoard;

    public MainManager(TournamentPlugin plugin) {
        this.arenaManager = new ArenaManager(plugin);
        this.matchManager = new MatchManager(this);
        this.tournamentManager = new TournamentManager(this);
    }

    public void clearArenas() {
        arenaManager.clearArenas();
    }

    public boolean isOccupied(Arena arena) {
        return arenaManager.isOccupied(arena);
    }

    public Arena getAvailableArena() {
        return arenaManager.getAvailableArena();
    }

    public void setLocation(String arenaName, Player player, int position) {
        arenaManager.setLocation(arenaName, player, position);
    }

    public List<Arena> getArenas() {
        return arenaManager.getArenas();
    }

    public void loadArenas() {
        arenaManager.loadArenas();
    }

    public List<Match> getMatches() {
        return matchManager.getMatches();
    }

    public List<UUID> getMatchWinners() {
        return matchManager.getMatchWinners();
    }

    public void clearMatches() {
        matchManager.clearMatches();
    }

    public void clearMatchWinners() {
        matchManager.clearMatchWinners();
    }

    public boolean isInMatch(Player player) {
        return matchManager.isInMatch(player);
    }

    public Match getMatch(Player player) {
        return matchManager.getMatch(player);
    }

    public HashMap<String, SavedPlayerState> getPlayerStates() {
        return matchManager.getPlayerStates();
    }

    public void addMatchWinner(Player player) {
        matchManager.addMatchWinner(player);
    }

    public void teleportPlayers(Match match) {
        matchManager.teleportPlayers(match);
    }

    public void startMatch(Match match) {
        matchManager.startMatch(match);
    }

    public void endMatch(Match match) {
        matchManager.endMatch(match);
    }

    public void endIdleMatch(Match match) {
        matchManager.endIdleMatch(match);
    }


    public Tournament getTournament() {
        return tournamentManager.getTournament();
    }

    public List<UUID> getParticipants() {
        return tournamentManager.getParticipants();
    }

    public void clearParticipants() {
        tournamentManager.clearParticipants();
    }

    public boolean isInTournament(Player player) {
        return tournamentManager.isInTournament(player);
    }

    public void addParticipant(Player player) {
        tournamentManager.addParticipant(player);
    }

    public void removeFromTournament(Player... participants) {
        tournamentManager.removeFromTournament(participants);
    }

    public void startTournament() {
        scoreBoard = new ScoreBoard(this);
        scoreBoard.setScoreboardAll();
        tournamentManager.startTournament();
    }

    public void endTournament() {
        tournamentManager.endTournament();
    }

    public boolean isTournamentRunning() {
        return tournamentManager.isTournamentRunning();
    }

    public void setWorldSpawn(Player player) {
        tournamentManager.setWorldSpawn(player);
    }

    public Location getWorldSpawn() {
        return tournamentManager.getWorldSpawn();
    }

    public Location getSpectatorArea() {
        return tournamentManager.getSpectatorArea();
    }

    public void setSpectatorArea(Player player) {
        tournamentManager.setSpectatorArea(player.getLocation());
    }

    public void setSpectatorArea(Location location) {
        tournamentManager.setSpectatorArea(location);
    }

    public void setDefaultState(Player player) {
        matchManager.getNewPlayerState().setDefaultState(player);
    }

}