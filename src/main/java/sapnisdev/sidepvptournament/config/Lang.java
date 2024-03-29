package sapnisdev.sidepvptournament.config;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import sapnisdev.sidepvptournament.Utils.ChatUtil;
import sapnisdev.sidepvptournament.TournamentPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {
    NO_PERMISSION("messages.no-permission"),
    IMPROPER_USAGE("messages.improper-usage"),
    INVALID_SENDER("messages.invalid-sender"),
    INVALID_NUMBER("messages.invalid-number"),
    PLUGIN_DISABLED_IN_WORLD("messages.plugin-disabled-in-world"),
    SEVERE_ERROR("messages.severe-error"),
    NO_TOURNAMENTS_RUNNING("messages.no-tournaments-running"),
    NOT_ENOUGH_PLAYERS("messages.not-enough-players"),
    MAXIMUM_PLAYERS_REACHED("messages.maximum-players-reached"),
    ALREADY_IN_TOURNAMENT("messages.already-in-tournament"),
    NOT_IN_TOURNAMENT("messages.not-in-tournament"),
    MATCH_START_BROADCAST("messages.match-start-broadcast"),
    MATCH_WINNER_BROADCAST("messages.match-winner-broadcast"),
    MATCH_IDLE_BROADCAST("messages.match-idle-broadcast"),
    MATCH_CURRENT_FORCE_END_SUCCESS("messages.match-current-force-end-success"),
    MATCH_SPECIFIC_FORCE_END_SUCCESS("messages.match-specific-force-end-success"),
    MATCH_FORCE_END_SELECT("messages.match-force-end-select"),
    MATCH_FORCE_END_BROADCAST("messages.match-force-end-broadcast"),
    SPECTATOR_AREA_SET("messages.spectator-area-set"),
    ARENA_INVALID_POSITION("messages.arena-invalid-position"),
    ARENA_SET_SUCCESS("messages.arena-set-success"),
    REQUIRE_EMPTY_INVENTORY("messages.require-empty-inventory"),
    WORLD_SPAWN_SET("messages.world-spawn-set"),
    COMMAND_USE_DENIED("messages.command-use-denied"),
    TOURNAMENT_ALREADY_STARTED("messages.tournament-already-started"),
    TOURNAMENT_AREAS_NOT_SET("messages.tournament-areas-not-set"),
    TOURNAMENT_JOINED_SUCCESS("messages.tournament-joined-success"),
    TOURNAMENT_JOINED_BROADCAST("messages.tournament-joined-broadcast"),
    TOURNAMENT_LEFT_SUCCESS("messages.tournament-left-success"),
    TOURNAMENT_LEFT_BROADCAST("messages.tournament-left-broadcast"),
    TOURNAMENT_START_SUCCESS("messages.tournament-start-success"),
    TOURNAMENT_POST_START_BROADCAST("messages.tournament-post-start-broadcast"),
    TOURNAMENT_END_SUCCESS("messages.tournament-end-success"),
    TOURNAMENT_END_BROADCAST("messages.tournament-end-broadcast"),
    TOURNAMENT_NO_WINNER_BROADCAST("messages.tournament-no-winner-broadcast"),
    TOURNAMENT_WINNER_REWARD_MESSAGE("messages.tournament-winner-reward-message"),
    YOU_VOTED("messages.you-voted"),
    NEED_TO_BE_IN_TOURNAMENT("messages.must-be-in"),
    ALREADY_VOTED("messages.already-voted"),
    CANT_VOTE("messages.cant-vote");


    private final String path;
    private final YamlConfiguration config;

    Lang(String path) {
        this.path = path;
        this.config = TournamentPlugin.getInstance().getConfig();
    }

    public String toString() {
        return PlaceholderAPI.setPlaceholders((OfflinePlayer) Bukkit.getOnlinePlayers().toArray()[0],ChatUtil.color((config.getBoolean("configuration.prefix-enabled") ? config.getString("configuration.prefix") : "" ) + config.getString(path)));
    }
}
