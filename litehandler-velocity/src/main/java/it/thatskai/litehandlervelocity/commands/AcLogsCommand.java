package it.thatskai.litehandlervelocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import it.thatskai.litehandlervelocity.LiteHandlerVelocity;
import it.thatskai.litehandlervelocity.config.ConfigCache;
import it.thatskai.litehandlervelocity.utils.Format;

public class AcLogsCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if(!source.hasPermission("litehandler.aclogs")){
            source.sendMessage(Format.color(ConfigCache.NO_PERMISSIONS));
            return;
        }

        if(args.length == 0){
            source.sendMessage(Format.color("&cUse: /aclogs <player>"));
            return;
        }
        String player = args[0];

        long start = System.currentTimeMillis();

        LiteHandlerVelocity.getInstance().getSqlProvider().getLogsTable().getAllLogs(player).whenComplete((logs, throwable)->{
            if(throwable != null || logs.isEmpty()){
                source.sendMessage(Format.color(ConfigCache.ACLOGS_DONT_FOUND.replace("%player%",player)));
                return;
            }

            String ms = String.valueOf(System.currentTimeMillis() - start);

            source.sendMessage(Format.color(ConfigCache.ACLOGS_FOUND
                    .replace("%player%",player)
                    .replace("%ms%",ms)));

            int index = Math.max(logs.size() - 20, 0);

            for (int i = index; i < logs.size(); i++) {
                String log = logs.get(i);
                if(log.isEmpty()) continue;
                source.sendMessage(Format.color(ConfigCache.ACLOGS_FORMAT
                        .replace("%log%",log)));
            }

        });


    }
}
