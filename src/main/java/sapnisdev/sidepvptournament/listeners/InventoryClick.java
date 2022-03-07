package sapnisdev.sidepvptournament.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import sapnisdev.sidepvptournament.TournamentPlugin;
import sapnisdev.sidepvptournament.kits.KitType;
import sapnisdev.sidepvptournament.managers.MainManager;

public class InventoryClick implements Listener {
    private TournamentPlugin plugin;
    private final MainManager mainManager;

    InventoryClick(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getWhoClicked().getOpenInventory().getTitle().contains("Vote For Kit")) return;


        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (item == null || item.getType() == Material.AIR) return;
        if (item.getType() == Material.BLACK_STAINED_GLASS_PANE) return;

        player.closeInventory();

        switch (item.getType()) {

            case BOW:

                TournamentPlugin.getVoteGUI().addVote(player, KitType.ARCHER);

                break;

            case POTION:

                TournamentPlugin.getVoteGUI().addVote(player, KitType.POTION);

                break;

            case GOLDEN_APPLE:

                TournamentPlugin.getVoteGUI().addVote(player, KitType.GAPPLE);

                break;
        }


    }
}
