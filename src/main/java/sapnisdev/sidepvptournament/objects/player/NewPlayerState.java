package sapnisdev.sidepvptournament.objects.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sapnisdev.sidepvptournament.TournamentPlugin;

import java.util.Set;

public class NewPlayerState {
    private final TournamentPlugin plugin;

    public NewPlayerState() {
        this.plugin = TournamentPlugin.getInstance();
    }

    public void modifyPlayer(Player player) {
        player.getInventory().clear();
        setDefaultState(player);
        addEffects(player);
        player.getInventory().setArmorContents(TournamentPlugin.getMainManager().getKitType().getArmor());
        player.getInventory().setContents(TournamentPlugin.getMainManager().getKitType().getInventory());
    }

    public void setDefaultState(Player player) {
        if(plugin.getConfig().getBoolean("configuration.when-fighting.ensure-max-health")) {
            player.setHealth(20.0);
        }

        if(plugin.getConfig().getBoolean("configuration.when-fighting.ensure-max-hunger")) {
            player.setFoodLevel(20);
        }

        if(plugin.getConfig().getBoolean("configuration.when-fighting.ensure-min-exhaustion")) {
            player.setExhaustion(0);
        }

        if(plugin.getConfig().getBoolean("configuration.when-fighting.ensure-max-saturation")) {
            player.setSaturation(20);
        }

        if(plugin.getConfig().getBoolean("configuration.when-fighting.ensure-survival-gamemode")) {
            player.setGameMode(GameMode.SURVIVAL);
        }

        for(PotionEffect effect: player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    private void addEffects(Player player) {
        Set<String> effects = plugin.getConfig().getConfigurationSection("kits." + TournamentPlugin.getMainManager().getKitType() + ".effects").getKeys(false);

        for(String effect: effects) {
            String path = "kits."+TournamentPlugin.getMainManager().getKitType() + ".effects." + effect;


            PotionEffectType type = PotionEffectType.getByName(effect.toUpperCase());
            int level = plugin.getConfig().getInt(path)-1;
            int duration = plugin.getConfig().getInt("kits." + TournamentPlugin.getVoteGUI().getMostVotedKit().toString().toUpperCase() + ".fight-duration");

            player.addPotionEffect(new PotionEffect(type, 20 * duration, level));
        }
    }
}
