package it.thatskai.neohandlervelocity.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import it.thatskai.neohandlervelocity.NeoHandlerVelocity;
import it.thatskai.neohandlervelocity.utils.AutoBanCheck;

import java.util.HashMap;
import java.util.List;

public class ConfigCache {


    public static String
            ACLOGS_GRIM_FORMAT, ACLOGS_VULCAN_FORMAT, ALERTS_GRIM_ALERT, ALERTS_VULCAN_ALERT,
            ALERTS_BAN_COMMAND, ALERTS_BAN_FORMAT, ALERTS_ENABLED, ALERTS_DISABLED,
            ON_JOIN_MESSAGE, NO_PERMISSIONS, ACLOGS_DONT_FOUND, ACLOGS_FOUND, ACLOGS_FORMAT, DATE_FORMAT;

    public static int ALERTS_GRIM_FLAG_ALERTS, ALERTS_VULCAN_FLAG_ALERTS;
    public static boolean ON_JOIN_ALERTS_MESSAGE_ENABLED;

    public static List<String> ALERTS_GRIM_DISABLED_CHECKS, ALERTS_VULCAN_DISABLED_CHECKS;

    public static HashMap<String, AutoBanCheck> ALERTS_GRIM_AUTO_BAN, ALERTS_VULCAN_AUTO_BAN;

    public static void load(YamlDocument config){
        NO_PERMISSIONS = config.getString("messages.no-permissions");

        ON_JOIN_ALERTS_MESSAGE_ENABLED = config.getBoolean("messages.join.enable");
        ON_JOIN_MESSAGE = config.getString("messages.join.message");

        ALERTS_ENABLED = config.getString("messages.alerts-enabled");
        ALERTS_DISABLED = config.getString("messages.alerts-disabled");

        ACLOGS_DONT_FOUND = config.getString("aclogs.messages.dont-found");
        ACLOGS_FOUND = config.getString("aclogs.messages.found");
        ACLOGS_FORMAT = config.getString("aclogs.messages.format");

        DATE_FORMAT = config.getString("aclogs.messages.date-format");

        ACLOGS_GRIM_FORMAT = config.getString("aclogs.grim.format");
        ACLOGS_VULCAN_FORMAT = config.getString("aclogs.vulcan.format");

        ALERTS_GRIM_ALERT = config.getString("alerts.grim.alert");
        ALERTS_VULCAN_ALERT = config.getString("alerts.vulcan.alert");

        ALERTS_GRIM_FLAG_ALERTS = config.getInt("alerts.grim.flag-alerts");
        ALERTS_VULCAN_FLAG_ALERTS = config.getInt("alerts.vulcan.flag-alerts");

        ALERTS_GRIM_DISABLED_CHECKS = config.getStringList("alerts.grim.disabled-checks");
        ALERTS_VULCAN_DISABLED_CHECKS = config.getStringList("alerts.vulcan.disabled-checks");

        ALERTS_GRIM_AUTO_BAN = new HashMap<>();
        ALERTS_VULCAN_AUTO_BAN = new HashMap<>();

        for(Object object : config.getSection("alerts.grim.auto-ban-checks").getStoredValue().keySet()){
            String s = String.valueOf(object);
            ALERTS_GRIM_AUTO_BAN.put(s,new AutoBanCheck(s,config.getInt("alerts.grim.auto-ban-checks."+s+".flags")));
        }

        for(Object object : config.getSection("alerts.vulcan.auto-ban-checks").getStoredValue().keySet()){
            String s = String.valueOf(object);
            ALERTS_VULCAN_AUTO_BAN.put(s,new AutoBanCheck(s,config.getInt("alerts.vulcan.auto-ban-checks."+s+".flags"),config.getString("alerts.vulcan.auto-ban-checks."+s+".type")));
        }

        ALERTS_BAN_COMMAND = config.getString("alerts.ban.command");
        ALERTS_BAN_FORMAT = config.getString("alerts.ban.format");

        NeoHandlerVelocity.getInstance().getLogger().info("Loaded all config values");
    }
}
