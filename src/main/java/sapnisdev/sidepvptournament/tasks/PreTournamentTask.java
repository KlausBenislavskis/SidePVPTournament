package sapnisdev.sidepvptournament.tasks;

import org.bukkit.configuration.file.YamlConfiguration;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.TournamentStage;
import sapnisdev.sidepvptournament.config.Lang;
import sapnisdev.sidepvptournament.objects.Tournament;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

import static sapnisdev.sidepvptournament.Utils.ChatUtil.color;

public class PreTournamentTask extends BukkitRunnable {
    private final TournamentPlugin plugin;
    private final Tournament tournament;
    private int countdown;

    public PreTournamentTask(Tournament tournament) {
        this.plugin = TournamentPlugin.getInstance();
        this.tournament = tournament;
        this.countdown = plugin.getConfig().getInt("configuration.tournament-start-delay");
        tournament.setStage(TournamentStage.WAITING);
    }

    public void run() {
        if(plugin.getConfig().getBoolean("configuration.start-when-max-players-reached")) {
            if(plugin.getConfig().getInt("configuration.maximum-players-allowed") == TournamentPlugin.getMainManager().getParticipants().size()) {
                Bukkit.broadcastMessage(Lang.TOURNAMENT_POST_START_BROADCAST.toString());
                TournamentTask task = new TournamentTask(tournament);
                tournament.setTournamentTask(task);
                task.runTaskTimer(TournamentPlugin.getInstance(), 0L, 40L);
                cancel();
            }
        }
        if(countdown == 0) {
            if(TournamentPlugin.getMainManager().getParticipants().size() < plugin.getConfig().getInt("configuration.minimum-players-to-start")) {
                tournament.end();
                Bukkit.broadcastMessage(Lang.NOT_ENOUGH_PLAYERS.toString());
            } else {
                Bukkit.broadcastMessage(Lang.TOURNAMENT_POST_START_BROADCAST.toString());
                TournamentTask task = new TournamentTask(tournament);
                tournament.setTournamentTask(task);
                for (UUID uuid : TournamentPlugin.getMainManager().getParticipants()){
                    TournamentPlugin.getMainManager().getScoreBoard().setScoreBoard(Bukkit.getPlayer(uuid));
                }
                task.runTaskTimer(TournamentPlugin.getInstance(), 0L, 40L);
            }
            cancel();
        }

        else if(plugin.getConfig().getIntegerList("configuration.countdown-values").contains(countdown)) {
            int min = countdown / 60;
            int sec = countdown % 60;
            String str = (min > 0 ? min + " minutes" : "") + (sec > 0 ? sec + " seconds" : "");
            final YamlConfiguration config = TournamentPlugin.getInstance().getConfig();
            List<?> mesage = config.getList("messages.tournament-countdown-broadcast");
            for (int i = 0; i < mesage.size(); i++) {
                Bukkit.broadcastMessage(color((String) mesage.get(i)).replace("{countdown}", str));
            }
        }

        countdown--;
    }


    public  void setCountdown(){
        countdown = 10;
    }
    public  String  getCountdown(){
        return String.valueOf(countdown);
    }
}
