package it.thatskai.neohandlervelocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import it.thatskai.neohandlervelocity.NeoHandlerVelocity;
import it.thatskai.neohandlervelocity.config.ConfigCache;
import it.thatskai.neohandlervelocity.utils.Format;

public class AcLogsCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if(!source.hasPermission("neohandler.aclogs")){
            source.sendMessage(Format.color(ConfigCache.NO_PERMISSIONS));
            return;
        }

        if(args.length == 0){
            source.sendMessage(Format.color("&cUse: /aclogs <player>"));
            return;
        }
        String player = args[0];

        long start = System.currentTimeMillis();

        NeoHandlerVelocity.getInstance().getSqlProvider().getLogsTable().getAllLogs(player).whenComplete((logs, throwable)->{
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
