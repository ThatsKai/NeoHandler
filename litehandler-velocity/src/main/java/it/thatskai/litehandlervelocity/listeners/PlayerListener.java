package it.thatskai.litehandlervelocity.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import it.thatskai.litehandlervelocity.LiteHandlerVelocity;
import it.thatskai.litehandlervelocity.utils.StaffUtils;

import java.util.concurrent.CompletableFuture;

public class PlayerListener {

    @Subscribe
    public void onLogin(LoginEvent e){
        Player player = e.getPlayer();
        if(!StaffUtils.playerAlerts.contains(player.getUniqueId()) && player.hasPermission("litehandler.alerts")){
            StaffUtils.playerAlerts.add(player.getUniqueId());
        }

        if(LiteHandlerVelocity.getInstance().isDatabaseConnect()){
            if(LiteHandlerVelocity.getInstance().getSqlProvider().connect()){
                LiteHandlerVelocity.getInstance().getSqlProvider().getLogsTable().addPlayer(player.getUsername()).whenComplete((b,throwable)->{
                    if(throwable != null) return;
                    if(b){
                        LiteHandlerVelocity.getInstance().getLogger().info(player.getUsername()+" è stato aggiunto al database.");
                    }
                });
            }
        }
    }

    @Subscribe
    public void onMessage(PluginMessageEvent e){
        byte[] data = e.getData();
        String message = new String(data);
        if(e.getIdentifier().equals(LiteHandlerVelocity.getInstance().getChannel())){
            if(message.startsWith("grim")){
                e.setResult(PluginMessageEvent.ForwardResult.handled());
                LiteHandlerVelocity.getInstance().getAlerts().handleGrimAlert(message);
            }
        }

        if (e.getIdentifier().getId().equals("vulcan:bungee")) {
            e.setResult(PluginMessageEvent.ForwardResult.handled());
            ByteArrayDataInput input = ByteStreams.newDataInput(e.getData());
            if(input.readUTF().equals("alert")){
                LiteHandlerVelocity.getInstance().getAlerts().handleVulcanAlert(input);
            }
        }
    }
}
