package sapnisdev.sidepvptournament;

import lombok.Getter;
import sapnisdev.sidepvptournament.api.TournamentAPI;
import sapnisdev.sidepvptournament.config.PluginConfig;
import sapnisdev.sidepvptournament.listeners.ListenerHandler;
import sapnisdev.sidepvptournament.managers.MainManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TournamentPlugin extends JavaPlugin {
    @Getter
    private static TournamentAPI tournamentAPI;

    @Getter
    private static MainManager mainManager;


    @Getter
    private ListenerHandler listenerHandler;

    @Getter
    private final PluginConfig rawConfig;

    public TournamentPlugin() {
        this.rawConfig = new PluginConfig(this, "settings.yml");
    }

    @Override
    public void onEnable() {
        saveDefault();

        mainManager = new MainManager(this);
        tournamentAPI = new TournamentAPI();

        loadCommands();
        loadListeners();
    }

    @Override
    public void onDisable() {
        if(mainManager != null) {
            mainManager.endTournament();
        }
    }

    private void loadCommands() {
        getCommand("tournament").setExecutor(new sapnisdev.sidepvptournament.CommandTournament(this));
    }

    private void loadListeners() {
        listenerHandler = new ListenerHandler(this);
    }

    private void saveDefault() {
        rawConfig.saveDefaultConfig();
    }

    @Override
    public YamlConfiguration getConfig() {
        return getRawConfig().getConfig();
    }

    public static TournamentPlugin getInstance() {
        return TournamentPlugin.getPlugin(TournamentPlugin.class);
    }
}
