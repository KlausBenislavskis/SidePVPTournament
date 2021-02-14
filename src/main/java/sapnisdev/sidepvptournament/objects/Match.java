package sapnisdev.sidepvptournament.objects;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;

import sapnisdev.sidepvptournament.tasks.MatchTask;
import org.bukkit.entity.Player;
import sapnisdev.sidepvptournament.TournamentPlugin;

import java.util.Set;

public class Match {
    @Getter
    private final Player initiator, opponent;

    @Getter
    private final long duration;

    @Getter @Setter
    private Arena arena;

    @Getter @Setter
    private Player winner;

    @Getter @Setter
    private Player loser;
    @Getter @Setter
    private boolean isRunning;

    @Getter @Setter
    private MatchTask matchTask;

    public Match(Player initiator, Player opponent) {
        this.initiator = initiator;
        this.opponent = opponent;
        this.duration = 20L /*ticks*/ * TournamentPlugin.getInstance().getConfig().getInt("configuration.match-duration") /*seconds*/;
        this.isRunning = true;
    }

    public void reset() {
        arena.setOccupied(false);
        setArena(null);
        setRunning(false);
        setWinner(null);
        setLoser(null);
        matchTask.cancel();
    }
    public Set<Player> toSet() {
        return Sets.newHashSet(initiator, opponent);
    }
}
