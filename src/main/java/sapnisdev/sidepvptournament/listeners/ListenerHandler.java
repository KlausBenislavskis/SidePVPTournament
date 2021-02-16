package sapnisdev.sidepvptournament.listeners;

import lombok.Getter;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.managers.MainManager;

public class ListenerHandler {


    @Getter
    private CommandListener commandListener;

    @Getter
    private ItemDropListener itemDropListener;

    @Getter
    private PlayerDamageListener playerDamageListener;

    @Getter
    private PlayerQuitListener playerQuitListener;

    @Getter
    private BlockBreakListener blockBreakListener;


    @Getter
    private PlayerJoinListener playerJoinListener;

    public ListenerHandler(TournamentPlugin plugin, MainManager mainManager) {
        this.commandListener = new CommandListener(plugin);
        this.itemDropListener = new ItemDropListener(plugin);
        this.playerDamageListener = new PlayerDamageListener(plugin);
        this.playerQuitListener = new PlayerQuitListener(plugin);
        this.blockBreakListener = new BlockBreakListener(plugin);
        this.playerJoinListener = new PlayerJoinListener(plugin,mainManager);
    }
}
