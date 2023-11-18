package it.thatskai.litehandlerbukkit;

import ac.grim.grimac.GrimUser;
import ac.grim.grimac.events.FlagEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class LiteHandlerBukkit extends JavaPlugin implements Listener {

    @Getter
    private static LiteHandlerBukkit instance;
    
    @Override
    public void onEnable(){
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "litehandler");
    }

    @Override
    public void onDisable(){}

    @EventHandler
    public void onFlag(FlagEvent e){
        GrimUser user = e.getPlayer();
        String check = e.getCheck().getCheckName();
        double violations = e.getCheck().getViolations();

        String format = "grim #flag# "+ user.getName() + " #flag# " + check + " #flag# " + violations;

        Player player = Bukkit.getPlayer(user.getName());
        if(player != null){
            player.sendPluginMessage(this, "litehandler", format.getBytes());
        }
    }
}
