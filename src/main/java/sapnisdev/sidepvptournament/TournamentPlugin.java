package sapnisdev.sidepvptournament;

import lombok.Getter;
import org.bukkit.Bukkit;
import sapnisdev.sidepvptournament.Utils.Expansions;
import sapnisdev.sidepvptournament.api.TournamentAPI;
import sapnisdev.sidepvptournament.config.PluginConfig;
import sapnisdev.sidepvptournament.kits.VoteGUI;
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

    private static TournamentPlugin plugin;
    @Getter
    private final PluginConfig rawConfig;
    private static VoteGUI voteGUI;

    public TournamentPlugin() {
        this.rawConfig = new PluginConfig(this, "settings.yml");
    }

    @Override
    public void onEnable() {
        plugin = this;
        saveDefault();
        mainManager = new MainManager(this);
        tournamentAPI = new TournamentAPI();
        voteGUI = new VoteGUI();

        loadCommands();
        loadListeners();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Expansions().register();
        } else {
            getLogger().info("Could not find PlaceholderAPI! This plugin is required for placeholders.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (mainManager != null) {
            mainManager.endTournament();
        }
    }

    private void loadCommands() {
        getCommand("t").setExecutor(new sapnisdev.sidepvptournament.CommandTournament(this));
    }

    private void loadListeners() {
        listenerHandler = new ListenerHandler(this, mainManager);
    }

    private void saveDefault() {
        rawConfig.saveDefaultConfig();
    }
    public static TournamentPlugin getPlugin() {
        return plugin;
    }
    @Override
    public YamlConfiguration getConfig() {
        return getRawConfig().getConfig();
    }
    public static VoteGUI getVoteGUI() {
        return voteGUI;
    }

    public static TournamentPlugin getInstance() {
        return TournamentPlugin.getPlugin(TournamentPlugin.class);
    }
}
