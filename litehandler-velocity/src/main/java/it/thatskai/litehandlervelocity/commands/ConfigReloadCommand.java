package it.thatskai.litehandlervelocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import it.thatskai.litehandlervelocity.LiteHandlerVelocity;
import it.thatskai.litehandlervelocity.config.ConfigCache;
import it.thatskai.litehandlervelocity.utils.Format;

public class ConfigReloadCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if(!source.hasPermission("litehandler.reload")){
            source.sendMessage(Format.color(ConfigCache.NO_PERMISSIONS));
            return;
        }
        LiteHandlerVelocity.getInstance().loadConfig();
        ConfigCache.load(LiteHandlerVelocity.getInstance().getConfig());
        source.sendMessage(Format.color("&aConfiguration reloaded!"));
    }
}
