package it.thatskai.neohandlervelocity.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import it.thatskai.neohandlervelocity.NeoHandlerVelocity;
import it.thatskai.neohandlervelocity.config.ConfigCache;
import it.thatskai.neohandlervelocity.utils.Format;
import it.thatskai.neohandlervelocity.utils.StaffUtils;

public class PlayerListener {

    @Subscribe
    public void onLogin(LoginEvent e){
        Player player = e.getPlayer();

        if(!StaffUtils.playerAlerts.contains(player.getUniqueId()) && player.hasPermission("litehandler.alerts")){
            if(ConfigCache.ON_JOIN_ALERTS_MESSAGE_ENABLED){
                player.sendMessage(Format.color(ConfigCache.ON_JOIN_MESSAGE));
            }

            StaffUtils.playerAlerts.add(player.getUniqueId());
        }

        if(NeoHandlerVelocity.getInstance().isDatabaseConnect()){
            if(NeoHandlerVelocity.getInstance().getSqlProvider().connect()){
                NeoHandlerVelocity.getInstance().getSqlProvider().getLogsTable().addPlayer(player.getUsername()).whenComplete((b, throwable)->{
                    if(throwable != null) return;
                    if(b){
                        NeoHandlerVelocity.getInstance().getLogger().info(player.getUsername()+" è stato aggiunto al database.");
                    }
                });
            }
        }
    }

    @Subscribe
    public void onMessage(PluginMessageEvent e){
        byte[] data = e.getData();
        String message = new String(data);
        if(e.getIdentifier().equals(NeoHandlerVelocity.getInstance().getChannel())){
            if(message.startsWith("grim")){
                e.setResult(PluginMessageEvent.ForwardResult.handled());
                NeoHandlerVelocity.getInstance().getAlerts().handleGrimAlert(message);
            }
        }

        if (e.getIdentifier().getId().equals("vulcan:bungee")) {
            e.setResult(PluginMessageEvent.ForwardResult.handled());
            ByteArrayDataInput input = ByteStreams.newDataInput(e.getData());
            if(input.readUTF().equals("alert")){
                NeoHandlerVelocity.getInstance().getAlerts().handleVulcanAlert(input);
            }
        }
    }
}
