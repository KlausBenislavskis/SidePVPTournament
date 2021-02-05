package sapnisdev.sidepvptournament.api.events;

import sapnisdev.sidepvptournament.objects.Match;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MatchStartEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Match match;

    public MatchStartEvent(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
