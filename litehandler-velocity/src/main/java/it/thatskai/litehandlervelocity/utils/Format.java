package it.thatskai.litehandlervelocity.utils;

import net.kyori.adventure.text.Component;

public class Format {

    public static Component color(String s){
        return Component.text(s.replace("&","§"));
    }
}
