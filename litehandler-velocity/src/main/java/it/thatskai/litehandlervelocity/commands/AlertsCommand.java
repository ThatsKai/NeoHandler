package it.thatskai.litehandlervelocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import it.thatskai.litehandlervelocity.config.ConfigCache;
import it.thatskai.litehandlervelocity.utils.Format;
import it.thatskai.litehandlervelocity.utils.StaffUtils;

public class AlertsCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if(!(source instanceof Player)) return;
        Player player = (Player) source;
        if(!player.hasPermission("litehandler.alerts")){
            player.sendMessage(Format.color(ConfigCache.NO_PERMISSIONS));
            return;
        }

        if(StaffUtils.playerAlerts.contains(player.getUniqueId())){
            StaffUtils.playerAlerts.remove(player.getUniqueId());
            player.sendMessage(Format.color(ConfigCache.ALERTS_DISABLED));
        }else{
            StaffUtils.playerAlerts.add(player.getUniqueId());
            player.sendMessage(Format.color(ConfigCache.ALERTS_ENABLED));
        }

    }
}
