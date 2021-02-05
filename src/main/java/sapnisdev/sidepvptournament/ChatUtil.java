package sapnisdev.sidepvptournament;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChatUtil {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
