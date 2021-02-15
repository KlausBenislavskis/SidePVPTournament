package sapnisdev.sidepvptournament.PlaceHolders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.objects.Match;
import sapnisdev.sidepvptournament.objects.Tournament;

import java.text.DecimalFormat;

import static org.bukkit.Statistic.*;

public class Expansions extends PlaceholderExpansion {
    private final DecimalFormat timeFormat = new DecimalFormat("#0.0");

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return "Latvish";
    }

    @Override
    public String getIdentifier() {
        return "t";
    }

    @Override
    public String getVersion() {
        return "1.0.3";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

        if (identifier.equals("players_joined")) {

            int joinedPlayers = 0;
            joinedPlayers = TournamentPlugin.getMainManager().getParticipants().size() + TournamentPlugin.getMainManager().getMatchWinners().size();
            int activeMatchPlayers = 0;
            for (Match e : TournamentPlugin.getMainManager().getMatchManager().getMatches()) {
                if (e.isRunning()) {
                    activeMatchPlayers += 2;
                }
            }
            if (activeMatchPlayers != 0) {
                activeMatchPlayers = activeMatchPlayers - 2;
            }
            return String.valueOf(joinedPlayers + activeMatchPlayers);
        }
        if (identifier.equals("max_players")) {
            final YamlConfiguration config = TournamentPlugin.getInstance().getConfig();
            return String.valueOf(config.getInt("configuration.maximum-players-allowed"));

        }
        if (identifier.equals("arena1_player1")) {
            if (TournamentPlugin.getMainManager().getMatches().size() > 0) {
                if (TournamentPlugin.getMainManager().getMatches().get(0).isRunning()) {
                    return TournamentPlugin.getMainManager().getMatches().get(0).getInitiator().getName();
                }
            }
            return "none";

        }
        if (identifier.equals("arena1_player2")) {
            if (TournamentPlugin.getMainManager().getMatches().size() > 0) {
                if (TournamentPlugin.getMainManager().getMatches().get(0).isRunning()) {
                    return TournamentPlugin.getMainManager().getMatches().get(0).getOpponent().getName();
                }
            }
            return "none";

        }
        if (identifier.equals("arena2_player1")) {
            if (TournamentPlugin.getMainManager().getMatches().size() > 1) {
                if (TournamentPlugin.getMainManager().getMatches().get(1).isRunning()) {
                    return TournamentPlugin.getMainManager().getMatches().get(1).getInitiator().getName();
                }
            }
            return "none";

        }
        if (identifier.equals("arena2_player2")) {
            if (TournamentPlugin.getMainManager().getMatches().size() > 1) {
                if (TournamentPlugin.getMainManager().getMatches().get(1).isRunning()) {
                    return TournamentPlugin.getMainManager().getMatches().get(1).getOpponent().getName();
                }
            }
            return "none";

        }
        return null;
    }

}

