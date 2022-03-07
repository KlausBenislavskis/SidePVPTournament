package sapnisdev.sidepvptournament;

import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitScheduler;
import sapnisdev.sidepvptournament.Utils.SerializationUtil;
import sapnisdev.sidepvptournament.config.Lang;
import sapnisdev.sidepvptournament.kits.KitType;
import sapnisdev.sidepvptournament.managers.MainManager;
import sapnisdev.sidepvptournament.objects.Match;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sapnisdev.sidepvptournament.objects.player.SavedPlayerState;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bukkit.Bukkit.getServer;
import static sapnisdev.sidepvptournament.Utils.ChatUtil.color;

public class CommandTournament implements CommandExecutor {
    private TournamentPlugin plugin;
    private MainManager mainManager;
    public final HashMap<Player, SavedPlayerState> playerStates;
    public static final HashMap<Player, Location> playerLocations = new HashMap<>();

    CommandTournament(TournamentPlugin instance) {
        this.plugin = instance;
        this.playerStates = new HashMap<>();
        this.mainManager = TournamentPlugin.getMainManager();
    }

    private class CommandException extends RuntimeException {
        CommandException(String msg) {
            super(msg);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            checkPerm(sender, "sidetournament.default");
            checkArgs(args, 1);
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
            return true;
        }

        try {
            run(sender, cmd, label, args[0], args);
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(Lang.SEVERE_ERROR.toString());
        }

        return true;
    }

    private boolean run(CommandSender sender, Command cmd, String label, String subCommand, String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            switch (args[0].toLowerCase()) {
                case "start":
                    if (mainManager.isTournamentRunning()) {
                        sender.sendMessage(Lang.TOURNAMENT_ALREADY_STARTED.toString());
                        return true;
                    }

                    if (plugin.getConfig().get("arenas") == null ||
                            plugin.getConfig().get("spectator") == null ||
                            plugin.getConfig().get("world-spawn") == null) {
                        sender.sendMessage(Lang.TOURNAMENT_AREAS_NOT_SET.toString());
                        return true;
                    }

                    final YamlConfiguration config = TournamentPlugin.getInstance().getConfig();
                    List<?> mesage = config.getList("messages.tournament-pre-start-broadcast");
                    for (int i = 0; i < mesage.size(); i++) {
                        Bukkit.broadcastMessage(color((String) mesage.get(i)));
                    }
                    sender.sendMessage(Lang.TOURNAMENT_START_SUCCESS.toString());

                    if (args.length > 1) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < args.length; i++) {
                            if (i > 0) sb.append(" ");
                            sb.append(args[i]);
                        }
                        TournamentPlugin.getPlugin().getRawConfig().getConfig().set("configuration.winner-rewards.reward-command", sb.toString().replace("start ", ""));
                        TournamentPlugin.getPlugin().getRawConfig().saveConfig();
                    }
                    TournamentPlugin.getVoteGUI().reset();
                    TournamentPlugin.getVoteGUI().setVoting(true);
                    mainManager.startTournament();
                    break;
                case "forcestart":
                    if (mainManager.isTournamentRunning()) {
                        mainManager.getTournament().getPreTournamentTask().setCountdown();
                        return true;
                    }
                    else {
                        sender.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    }
                    TournamentPlugin.getVoteGUI().reset();
                    break;
                case "end":
                    if (!mainManager.isTournamentRunning()) {
                        sender.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                        return true;
                    }

                    Bukkit.broadcastMessage(Lang.TOURNAMENT_END_BROADCAST.toString());
                    sender.sendMessage(Lang.TOURNAMENT_END_SUCCESS.toString());
                    TournamentPlugin.getMainManager().getScoreBoard().removeScoreboards();
                    TournamentPlugin.getVoteGUI().reset();
                    mainManager.endTournament();
                    break;
            }
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (plugin.getConfig().getBoolean("configuration.limit-plugin-to-specific-worlds")) {
                if (!plugin.getConfig().getStringList("configuration.limited-worlds").contains(player.getWorld().getName())) {
                    player.sendMessage(Lang.PLUGIN_DISABLED_IN_WORLD.toString());
                    return true;
                }
            }
        }

        Player player;

        switch (subCommand) {
            case "start":
                checkPerm(sender, "sidetournament.start");

                if (mainManager.isTournamentRunning()) {
                    sender.sendMessage(Lang.TOURNAMENT_ALREADY_STARTED.toString());
                    return true;
                }

                if (plugin.getConfig().get("arenas") == null ||
                        plugin.getConfig().get("spectator") == null ||
                        plugin.getConfig().get("world-spawn") == null) {
                    sender.sendMessage(Lang.TOURNAMENT_AREAS_NOT_SET.toString());
                    return true;
                }

                final YamlConfiguration config = TournamentPlugin.getInstance().getConfig();
                List<?> mesage = config.getList("messages.tournament-pre-start-broadcast");
                for (int i = 0; i < mesage.size(); i++) {
                    Bukkit.broadcastMessage(color((String) mesage.get(i)));
                }
                sender.sendMessage(Lang.TOURNAMENT_START_SUCCESS.toString());

                if (args.length > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        if (i > 0) sb.append(" ");
                        sb.append(args[i]);
                    }
                    TournamentPlugin.getPlugin().getRawConfig().getConfig().set("configuration.winner-rewards.reward-command", sb.toString().replace("start ", ""));
                    TournamentPlugin.getPlugin().getRawConfig().saveConfig();
                }
                TournamentPlugin.getVoteGUI().reset();
                TournamentPlugin.getVoteGUI().setVoting(true);
                mainManager.startTournament();
                break;
            case "join":
                checkPlayer(sender);
                player = (Player) sender;

                if (!mainManager.isTournamentRunning()) {
                    player.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    return true;
                }

                if (mainManager.getTournament().getStage() == TournamentStage.ACTIVE) {
                    player.sendMessage(Lang.TOURNAMENT_ALREADY_STARTED.toString());
                    return true;
                }

                if (mainManager.isInTournament(player)) {
                    player.sendMessage(Lang.ALREADY_IN_TOURNAMENT.toString());
                    return true;
                }

                int allowed = plugin.getConfig().getInt("configuration.maximum-players-allowed");

                if (mainManager.getParticipants().size() == allowed && allowed != -1) {
                    player.sendMessage(Lang.MAXIMUM_PLAYERS_REACHED.toString());
                    return true;
                }

                if (plugin.getConfig().getBoolean("configuration.force-player-clear-inventory")) {
                    if (!hasEmptyInventory(player)) {
                        player.sendMessage(Lang.REQUIRE_EMPTY_INVENTORY.toString());
                        return true;
                    }
                }

                playerLocations.put(player, player.getLocation());

                mainManager.addParticipant(player);


                if (!plugin.getConfig().getBoolean("configuration.disable-join/leave-tournament-message")) {
                    Bukkit.broadcastMessage(Lang.TOURNAMENT_JOINED_BROADCAST.toString().replace("{username}", player.getName()));
                }
                player.sendMessage(Lang.TOURNAMENT_JOINED_SUCCESS.toString());
                player.teleport(mainManager.getSpectatorArea());

                BukkitScheduler scheduler = getServer().getScheduler();
                scheduler.scheduleSyncDelayedTask(TournamentPlugin.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        if (!TournamentPlugin.getVoteGUI().hasVoted(player)) {
                            if (TournamentPlugin.getVoteGUI().isVoting()) {
                                player.openInventory(TournamentPlugin.getVoteGUI().getInventory());
                            } else {
                                player.sendMessage(color(Lang.CANT_VOTE.toString()));
                            }
                        }
                    }
                },5L);


                break;

            case "leave":
                checkPlayer(sender);
                player = (Player) sender;

                if (!mainManager.isTournamentRunning()) {
                    player.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    return true;
                }

                if (!mainManager.isInTournament(player)) {
                    player.sendMessage(Lang.NOT_IN_TOURNAMENT.toString());
                    return true;
                }

                player.sendMessage(Lang.TOURNAMENT_LEFT_SUCCESS.toString());
                if (!plugin.getConfig().getBoolean("configuration.disable-join/leave-tournament-message")) {
                    Bukkit.broadcastMessage(Lang.TOURNAMENT_LEFT_BROADCAST.toString().replace("{username}", player.getName()));
                }

                if (mainManager.isInMatch(player)) {
                    final Match match = mainManager.getMatch(player);
                    match.setWinner(player.getName().equalsIgnoreCase(match.getInitiator().getName()) ? match.getOpponent() : match.getInitiator());
                    match.setLoser(player.getName().equalsIgnoreCase(match.getInitiator().getName()) ? match.getInitiator() : match.getInitiator());
                    mainManager.addMatchWinner(match.getWinner());
                    mainManager.endMatch(mainManager.getMatch(player));
                }


                mainManager.removeFromTournament(player);
                if(playerLocations.containsKey(player)){
                    player.teleport(playerLocations.get(player));
                } else {
                    player.teleport(mainManager.getWorldSpawn());
                }
                playerLocations.remove(player);
                player.closeInventory();
                break;
            case "setkit": {
                checkPerm(sender, "side.admin");

                if (args.length < 2) {
                    sender.sendMessage(color("&4[!] &cUsage: /t setkit <kit_name>"));
                    sender.sendMessage(color("&7&oCurrent Kit Types: ARCHER, POTION, GAPPLE"));
                    return true;
                }

                String kitType = args[1].toUpperCase();
                KitType type = KitType.valueOf(kitType);


                if (type == null) {
                    sender.sendMessage(color("&4[!] &cInvalid kit."));
                    return true;
                }
                player = (Player) sender;

                TournamentPlugin.getPlugin().getRawConfig().getConfig().set("kits." + type.toString().toUpperCase() + ".armor", SerializationUtil.itemStackArrayToString(player.getInventory().getArmorContents()));
                TournamentPlugin.getPlugin().getRawConfig().getConfig().set("kits." + type.toString().toUpperCase() + ".inventory", SerializationUtil.itemStackArrayToString(player.getInventory().getContents()));
                TournamentPlugin.getPlugin().getRawConfig().saveConfig();

                sender.sendMessage(color("&4[!] &c" + type.toString().toUpperCase() + " &7Kit successfully set."));

                break;
            }
            case "vote": {
                player = (Player) sender;
                if (!TournamentPlugin.getMainManager().getParticipants().contains(player.getUniqueId())) {
                    player.sendMessage(color(Lang.NEED_TO_BE_IN_TOURNAMENT.toString()));
                    return true;
                }

                if (TournamentPlugin.getVoteGUI().isVoting()) {
                    player.openInventory(TournamentPlugin.getVoteGUI().getInventory());
                } else {
                    player.sendMessage(color(Lang.CANT_VOTE.toString()));
                }

                break;
            }
            case "forcestart":
                checkPerm(sender, "sidetournament.start");
                if (mainManager.isTournamentRunning()) {
                    mainManager.getTournament().getPreTournamentTask().setCountdown();
                    return true;
                }
                else {
                    sender.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                }
                break;
            case "end":
                checkPerm(sender, "sidetournament.end");

                if (!mainManager.isTournamentRunning()) {
                    sender.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    return true;
                }

                Bukkit.broadcastMessage(Lang.TOURNAMENT_END_BROADCAST.toString());
                sender.sendMessage(Lang.TOURNAMENT_END_SUCCESS.toString());
                TournamentPlugin.getMainManager().getScoreBoard().removeScoreboards();
                TournamentPlugin.getVoteGUI().reset();
                mainManager.endTournament();
                break;

            case "setspawn":
                checkPlayer(sender);
                player = (Player) sender;

                checkPerm(sender, "sidetournament.setspawn");
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("-spectator")) {
                        sender.sendMessage(Lang.SPECTATOR_AREA_SET.toString());
                        mainManager.setSpectatorArea(player);
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("-world")) {
                        sender.sendMessage(Lang.WORLD_SPAWN_SET.toString());
                        mainManager.setWorldSpawn(player);
                    }
                }

                if (args.length == 3) {
                    String arenaName = args[1];

                    checkNum(args[2]);
                    Integer num = Integer.parseInt(args[2]);

                    if (num < 1 || num > 2) {
                        player.sendMessage(Lang.ARENA_INVALID_POSITION.toString());
                        return true;
                    }

                    sender.sendMessage(Lang.ARENA_SET_SUCCESS.toString().replace("{arena}", arenaName).replace("{position}", num.toString()));
                    mainManager.setLocation(arenaName, player, num);
                    return true;
                }
                break;
            case "end-match":
                checkPerm(sender, "sidetournament.endmatch");
                if (args.length == 1) {
                    if (mainManager.getMatches().size() == 1) {
                        sender.sendMessage(Lang.MATCH_CURRENT_FORCE_END_SUCCESS.toString());
                        mainManager.getMatchManager().endMatchForcefully(mainManager.getMatches().get(0));
                    } else {
                        String matches = mainManager.getMatches()
                                .stream()
                                .map(match -> plugin.getConfig().getString("messages.match-force-end-select-format")
                                        .replace("{matchID}", String.valueOf(match.getMatchTask().getTaskId()))
                                        .replace("{initiator}", match.getInitiator().getName())
                                        .replace("{opponent}", match.getOpponent().getName()))
                                .collect(Collectors.joining(plugin.getConfig().getString("messages.match-force-end-select-separator")));
                        sender.sendMessage(Lang.MATCH_FORCE_END_SELECT.toString().replace("{matches}", color(matches)));
                    }
                    return true;
                }

                checkArgs(args, 2);

                Match match = mainManager.getMatchManager().getMatchById(Integer.parseInt(args[1]));
                mainManager.getMatchManager().endMatchForcefully(match);

                sender.sendMessage(Lang.MATCH_SPECIFIC_FORCE_END_SUCCESS.toString()
                        .replace("{initiator}", match.getInitiator().getName())
                        .replace("{opponent}", match.getOpponent().getName()));
                break;
            default:
                throw new CommandException(Lang.IMPROPER_USAGE.toString());
        }
        return false;
    }

    private boolean hasEmptyInventory(Player player) {
        final Stream<ItemStack> contents = Stream.concat(Arrays.stream(player.getInventory().getContents()), Arrays.stream(player.getInventory().getArmorContents()));
        return !contents.filter(itemStack -> itemStack != null).anyMatch(itemStack -> itemStack.getType() != Material.AIR);
    }

    private void checkNum(String number) {
        try {
            int num = Integer.parseInt(number);
        } catch (NumberFormatException ex) {
            throw new CommandException(Lang.INVALID_NUMBER.toString());
        }
    }

    private void checkPerm(CommandSender cs, String perm) {
        if (!cs.hasPermission(perm))
            throw new CommandException(Lang.NO_PERMISSION.toString());
    }

    private void checkArgs(String[] args, int min) {
        if (args.length < min)
            throw new CommandException(Lang.IMPROPER_USAGE.toString());
    }

    private void checkPlayer(CommandSender cs) {
        if (!(cs instanceof Player))
            throw new CommandException(Lang.INVALID_SENDER.toString());
    }
}
