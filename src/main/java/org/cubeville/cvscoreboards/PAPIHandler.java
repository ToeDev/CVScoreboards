package org.cubeville.cvscoreboards;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PAPIHandler {

    public PAPIHandler() {
        ///papi parse ToeMan_ %betonquest_testpackage:globalpoint.testglobalpoint.amount%
    }

    public String parsePAPI(Player player, String var) {
        return PlaceholderAPI.setPlaceholders(player, var);
    }
}
