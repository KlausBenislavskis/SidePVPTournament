package sapnisdev.sidepvptournament.Utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.kits.KitType;
import sapnisdev.sidepvptournament.objects.Match;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Expansions extends PlaceholderExpansion {
    private final DecimalFormat timeFormat = new DecimalFormat("#0.0");
    private static String playerone;
    private static String playertwo;

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
        return "SideNetwork";
    }

    @Override
    public String getIdentifier() {
        return "t";
    }

    @Override
    public String getVersion() {
        return "1.4";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

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
        if (identifier.equals("arena1")) {
            if (TournamentPlugin.getMainManager().getMatches().size() > 0) {
                if (TournamentPlugin.getMainManager().getMatches().get(0).isRunning()) {
                    if (TournamentPlugin.getMainManager().getMatches().get(0).getInitiator().getName().length() > 9) {
                        String a1 = TournamentPlugin.getMainManager().getMatches().get(0).getInitiator().getName();
                        StringBuilder sb = new StringBuilder(a1);
                        for (int i = 0; i < a1.length()-9; i++) {
                            sb.deleteCharAt(sb.length() -1);
                        }
                        return ChatUtil.color("&6" + sb + " &evs &6" + TournamentPlugin.getMainManager().getMatches().get(0).getOpponent().getName());
                    } else {
                        return ChatUtil.color("&6" + TournamentPlugin.getMainManager().getMatches().get(0).getInitiator().getName() + " &evs &6" + TournamentPlugin.getMainManager().getMatches().get(0).getOpponent().getName());
                    }
                }
                return ChatUtil.color("&eWaiting for players");

            }
        }
        if (identifier.equals("arena2")) {
            if (TournamentPlugin.getMainManager().getMatches().size() > 1) {
                if (TournamentPlugin.getMainManager().getMatches().get(1).isRunning()) {
                    return ChatUtil.color("&6" + TournamentPlugin.getMainManager().getMatches().get(1).getInitiator().getName() + " &evs &6" + TournamentPlugin.getMainManager().getMatches().get(1).getOpponent().getName());
                }
            }
            return ChatUtil.color("&eWaiting for players");

        }
        if (identifier.equals("arena3")) {
            if (TournamentPlugin.getMainManager().getMatches().size() > 2) {
                if (TournamentPlugin.getMainManager().getMatches().get(2).isRunning()) {
                    return ChatUtil.color("&6" + TournamentPlugin.getMainManager().getMatches().get(2).getInitiator().getName() + " &evs &6" + TournamentPlugin.getMainManager().getMatches().get(2).getOpponent().getName());
                }
            }
            return ChatUtil.color("&eWaiting for players");

        }
        if (identifier.equals("arena4")) {
            if (TournamentPlugin.getMainManager().getMatches().size() > 4) {
                if (TournamentPlugin.getMainManager().getMatches().get(4).isRunning()) {
                    return ChatUtil.color("&6" + TournamentPlugin.getMainManager().getMatches().get(4).getInitiator().getName() + " &evs &6" + TournamentPlugin.getMainManager().getMatches().get(4).getOpponent().getName());
                }
            }
            return ChatUtil.color("&eWaiting for players");

        }
        if (identifier.equals("till_start")) {
            if (TournamentPlugin.getMainManager().isTournamentRunning()) {
                String formatted = LocalTime.MIDNIGHT.plus(Duration.ofSeconds(Long.parseLong(TournamentPlugin.getMainManager().getTournament().getPreTournamentTask().getCountdown()))).format(DateTimeFormatter.ofPattern("mm:ss"));
                return formatted;
            }
            return "Inactive";

        }

        if (identifier.equals("votes_archer")) {
            int archer = 0;
            if (!(TournamentPlugin.getVoteGUI().getVotes().get(KitType.ARCHER) == null)) {
                archer = TournamentPlugin.getVoteGUI().getVotes().get(KitType.ARCHER);
            }
            return String.valueOf(archer);
        }
        if (identifier.equals("votes_potion")) {
            int potion = 0;
            if (!(TournamentPlugin.getVoteGUI().getVotes().get(KitType.POTION) == null)) {
                potion = TournamentPlugin.getVoteGUI().getVotes().get(KitType.POTION);
            }
            return String.valueOf(potion);
        }
        if (identifier.equals("votes_gapple")) {
            int gapple = 0;
            if (!(TournamentPlugin.getVoteGUI().getVotes().get(KitType.GAPPLE) == null)) {
                gapple = TournamentPlugin.getVoteGUI().getVotes().get(KitType.GAPPLE);
            }
            return String.valueOf(gapple);
        }
        return null;
    }

}

