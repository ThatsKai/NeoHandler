package it.thatskai.neohandlerbukkit;

import ac.grim.grimac.GrimUser;
import ac.grim.grimac.events.FlagEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class NeoHandlerBukkit extends JavaPlugin implements Listener {
    
    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "neohandler");
    }

    @EventHandler
    public void onFlag(FlagEvent e){
        GrimUser user = e.getPlayer();
        String check = e.getCheck().getCheckName();
        double violations = e.getCheck().getViolations();

        String format = "grim #flag# "+ user.getName() + " #flag# " + check + " #flag# " + violations;

        Player player = Bukkit.getPlayer(user.getName());
        if(player != null){
            player.sendPluginMessage(this, "neohandler", format.getBytes());
        }
    }
}
