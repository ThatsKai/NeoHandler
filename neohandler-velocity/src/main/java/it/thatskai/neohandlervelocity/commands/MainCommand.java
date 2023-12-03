package it.thatskai.neohandlervelocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import it.thatskai.neohandlervelocity.NeoHandlerVelocity;
import it.thatskai.neohandlervelocity.config.ConfigCache;
import it.thatskai.neohandlervelocity.utils.Format;

public class MainCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if(!source.hasPermission("neohandler.admin")){
            source.sendMessage(Format.color("&cRunning NeoHandler by @ThatsKai"));
            return;
        }

        if(args.length != 0){
            if(args[0].equalsIgnoreCase("reload")){
                NeoHandlerVelocity.getInstance().loadConfig();
                ConfigCache.load(NeoHandlerVelocity.getInstance().getConfig());
                source.sendMessage(Format.color("&aConfiguration reloaded!"));
                return;
            }
            if(args[0].equalsIgnoreCase("test") && source instanceof Player){
                Player player = (Player) source;
                String ac = args[1];
                if(ac.equalsIgnoreCase("grim")){
                    NeoHandlerVelocity.getInstance().getAlerts().handleGrimAlert("grim #flag# "+player.getUsername()+" #flag# Test #flag# 5");
                    return;
                }
                player.sendMessage(Format.color("&cUse: /neohandler test grim"));
                return;
            }
        }

        source.sendMessage(Format.color("&cUse: /neohandler <reload/test> [grim]"));

    }
}
