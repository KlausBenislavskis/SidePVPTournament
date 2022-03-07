package sapnisdev.sidepvptournament.kits;

import org.bukkit.inventory.ItemStack;
import sapnisdev.sidepvptournament.Utils.SerializationUtil;
import sapnisdev.sidepvptournament.TournamentPlugin;

import java.io.IOException;

public enum KitType {

    ARCHER, POTION, GAPPLE;

    public ItemStack[] getInventory() {
        try {
            return SerializationUtil.stringToItemStackArray(TournamentPlugin.getInstance().getConfig().getString("kits." + toString().toUpperCase() + ".inventory"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public ItemStack[] getArmor() {
        try {
            return SerializationUtil.stringToItemStackArray(TournamentPlugin.getInstance().getConfig().getString("kits." + toString().toUpperCase() + ".armor"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
