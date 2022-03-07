package sapnisdev.sidepvptournament.kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sapnisdev.sidepvptournament.Utils.ItemBuilder;
import sapnisdev.sidepvptournament.config.Lang;

import java.util.ArrayList;
import java.util.HashMap;

import static sapnisdev.sidepvptournament.Utils.ChatUtil.color;

public class VoteGUI {

    private Inventory inventory;

    private HashMap<KitType, Integer> votes;
    private ArrayList<String> votedPlayers;

    private boolean voting;

    private KitType kitType;

    private KitType overrideKit;

    public static final String VOTE_GUI_NAME = "Vote For Kit";

    public VoteGUI() {

        inventory = Bukkit.createInventory(null, 9 * 3, ChatColor.translateAlternateColorCodes('&', VOTE_GUI_NAME));

        votes = new HashMap<>();
        votedPlayers = new ArrayList<>();
        voting = false;

        kitType = KitType.POTION;

        ItemStack glass = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("  ").build();

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }

        // 11, 13, 15

        ItemStack archer, gapple, pot;

        archer = new ItemBuilder(Material.BOW).lore("&7&o(( Click to Vote ))").name("&d&lArcher Kit").addGlow().build();
        pot = new ItemBuilder(Material.POTION).lore("&7&o(( Click to Vote ))").name("&a&lPotion Kit").addGlow().build();
        gapple = new ItemBuilder(Material.GOLDEN_APPLE).lore("&7&o(( Click to Vote ))").name("&6&lGapple Kit").addGlow().build();

        inventory.setItem(11, archer);
        inventory.setItem(13, pot);
        inventory.setItem(15, gapple);

    }

    public Inventory getInventory() {
        return inventory;
    }

    public void reset() {
        voting = false;
        overrideKit = null;
        kitType = KitType.POTION;
        votes.clear();
        votedPlayers.clear();
        votes.put(KitType.ARCHER, 0);
        votes.put(KitType.POTION, 0);
        votes.put(KitType.GAPPLE, 0);
    }

    public void setOverrideKit(KitType kitType) {
        this.overrideKit = kitType;
    }

    public boolean hasVoted(Player player) {

        return votedPlayers.contains(player.getName());
    }


    public void addVote(Player player, KitType kitType) {

        if (votedPlayers.contains(player.getName())) {
            player.sendMessage(color(Lang.ALREADY_VOTED.toString()));
            return;
        }

        votedPlayers.add(player.getName());

        if (!votes.containsKey(kitType)) {
            votes.put(kitType, 1);
        } else {
            votes.put(kitType, votes.get(kitType) + 1);
        }

        player.sendMessage(color(Lang.YOU_VOTED.toString()));


    }

    public KitType getMostVotedKit() {

        if (overrideKit != null) {
            return overrideKit;
        }

        KitType mostVotedKit = KitType.POTION;

        if (votedPlayers.isEmpty()) {
            return mostVotedKit;
        }

        for (KitType type : votes.keySet()) {

            if (!votes.containsKey(type)) continue;

            if (!votes.containsKey(mostVotedKit)) {
                mostVotedKit = type;
                continue;
            }

            if (votes.get(type) > votes.get(mostVotedKit)) {
                mostVotedKit = type;
            }

        }


        return mostVotedKit;

    }

    public boolean isVoting() {
        return voting;
    }

    public void setVoting(boolean voting) {
        this.voting = voting;
    }

    public HashMap<KitType, Integer> getVotes() {
        return votes;
    }
}
