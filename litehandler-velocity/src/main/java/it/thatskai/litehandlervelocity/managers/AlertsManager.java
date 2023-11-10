package it.thatskai.litehandlervelocity.managers;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.Player;
import it.thatskai.litehandlervelocity.LiteHandlerVelocity;
import it.thatskai.litehandlervelocity.config.ConfigCache;
import it.thatskai.litehandlervelocity.utils.AutoBanCheck;
import it.thatskai.litehandlervelocity.utils.Format;
import it.thatskai.litehandlervelocity.utils.StaffUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class AlertsManager {

    public void sendAlert(String s){
        for(Player player : LiteHandlerVelocity.getInstance().getProxyServer().getAllPlayers()){
            if(player.hasPermission("litehandler.alerts") && StaffUtils.playerAlerts.contains(player.getUniqueId())){
                player.sendMessage(Format.color(s));
            }
        }
    }

    public void handleGrimAlert(String var1){
        String[] parts = var1.split("#flag#");

        String player = parts[1].trim();
        String check = parts[2].trim();
        String violations = parts[3].trim();
        String server = "//";

        Optional<Player> p = LiteHandlerVelocity.getInstance().getProxyServer().getPlayer(player);
        if(p.isPresent()){
            server = p.get().getCurrentServer().get().getServerInfo().getName();
        }
        for(String s : ConfigCache.ALERTS_GRIM_DISABLED_CHECKS){
            if(check.toLowerCase().startsWith(s.toLowerCase())) return;
        }

        double vl = Double.parseDouble(violations);
        if(vl == 0.0) return;
        if(ConfigCache.ALERTS_GRIM_FLAG_ALERTS != -1){
            if(vl % ConfigCache.ALERTS_GRIM_FLAG_ALERTS != 0) return;
        }

        sendAlert(ConfigCache.ALERTS_GRIM_ALERT
                .replace("%server%",server)
                .replace("%player%",player)
                .replace("%check%",check)
                .replace("%vl%",vl+""));

        if(LiteHandlerVelocity.getInstance().isDatabaseConnect()){
            LiteHandlerVelocity.getInstance().getSqlProvider().getLogsTable().addLog(player,ConfigCache.ACLOGS_GRIM_FORMAT
                    .replace("%server%",server)
                    .replace("%player%",player)
                    .replace("%vl%",vl+"")
                    .replace("%check%",check)
                    .replace("%date%",getDate()));
        }


        for(String c : ConfigCache.ALERTS_GRIM_AUTO_BAN.keySet()){
            if(check.toLowerCase().startsWith(c)){
                AutoBanCheck autoBanCheck = ConfigCache.ALERTS_GRIM_AUTO_BAN.get(c);
                if(vl == autoBanCheck.getFlags()){
                    handleBan(player, check, vl);
                }
                break;
            }
        }
    }

    public void handleVulcanAlert(ByteArrayDataInput var1){
        String[] parts = var1.readUTF().split("#VULCAN#");
        String checkName = parts[0];
        String checkType = parts[1];
        String violations = parts[2];
        String player = parts[3];
        String maxVl = parts[4];
        String server = "//";

        Optional<Player> p = LiteHandlerVelocity.getInstance().getProxyServer().getPlayer(player);
        if(p.isPresent()){
            server = p.get().getCurrentServer().get().getServerInfo().getName();
        }
        for(String s : ConfigCache.ALERTS_VULCAN_DISABLED_CHECKS){
            if(checkName.equalsIgnoreCase(s)) return;
        }

        int vl = Integer.parseInt(violations);
        if(ConfigCache.ALERTS_VULCAN_FLAG_ALERTS != -1){
            if(vl % ConfigCache.ALERTS_VULCAN_FLAG_ALERTS != 0) return;
        }

        sendAlert(ConfigCache.ALERTS_VULCAN_ALERT
                .replace("%server%",server)
                .replace("%player%",player)
                .replace("%check%",checkName)
                .replace("%type%",checkType)
                .replace("%maxvl%",maxVl)
                .replace("%vl%",vl+""));

        if(LiteHandlerVelocity.getInstance().isDatabaseConnect()){
            LiteHandlerVelocity.getInstance().getSqlProvider().getLogsTable().addLog(player,ConfigCache.ACLOGS_VULCAN_FORMAT
                    .replace("%server%",server)
                    .replace("%player%",player)
                    .replace("%vl%",vl+"")
                    .replace("%check%",checkName)
                    .replace("%type%",checkType)
                    .replace("%maxvl%",maxVl)
                    .replace("%date%",getDate()));
        }


        for(String c : ConfigCache.ALERTS_VULCAN_AUTO_BAN.keySet()){
            if(checkName.equalsIgnoreCase(c)){
                AutoBanCheck autoBanCheck = ConfigCache.ALERTS_VULCAN_AUTO_BAN.get(c);
                if(vl == autoBanCheck.getFlags() && checkType.equalsIgnoreCase(autoBanCheck.getType())){
                    handleBan(player, checkName, vl);
                }
                break;
            }
        }
    }

    public void handleBan(String player, String check, double vl){
        for(Player p : LiteHandlerVelocity.getInstance().getProxyServer().getAllPlayers()){
            if(p.hasPermission("litehandler.showban")){
                p.sendMessage(Format.color(ConfigCache.ALERTS_BAN_FORMAT.replace("%player%",player).replace("%check%",check).replace("%vl%",vl+"")));
            }
        }
        LiteHandlerVelocity.getInstance().getProxyServer().getCommandManager().executeAsync(
                LiteHandlerVelocity.getInstance().getProxyServer().getConsoleCommandSource(), ConfigCache.ALERTS_BAN_COMMAND);
    }

    public String getDate(){
        return new SimpleDateFormat(ConfigCache.DATE_FORMAT).format(new Date());
    }
}
