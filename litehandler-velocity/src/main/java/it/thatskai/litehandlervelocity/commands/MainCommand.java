package it.thatskai.litehandlervelocity.commands;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import it.thatskai.litehandlervelocity.LiteHandlerVelocity;
import it.thatskai.litehandlervelocity.config.ConfigCache;
import it.thatskai.litehandlervelocity.utils.Format;

public class MainCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if(!source.hasPermission("litehandler.admin")){
            source.sendMessage(Format.color("&cRunning LiteHandler by @ThatsKai"));
            return;
        }

        if(args.length != 0){
            if(args[0].equalsIgnoreCase("reload")){
                LiteHandlerVelocity.getInstance().loadConfig();
                ConfigCache.load(LiteHandlerVelocity.getInstance().getConfig());
                source.sendMessage(Format.color("&aConfiguration reloaded!"));
                return;
            }
            if(args[0].equalsIgnoreCase("test") && source instanceof Player){
                Player player = (Player) source;
                String ac = args[1];
                if(ac.equalsIgnoreCase("grim")){
                    LiteHandlerVelocity.getInstance().getAlerts().handleGrimAlert("grim #flag# "+player.getUsername()+" #flag# Test #flag# 5");
                    return;
                }
                player.sendMessage(Format.color("&c/litehandler test grim"));
                return;
            }
        }

        source.sendMessage(Format.color("&c/litehandler <reload/test> [grim]"));

    }
}
