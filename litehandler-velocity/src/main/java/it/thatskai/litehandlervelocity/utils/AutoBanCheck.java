package it.thatskai.litehandlervelocity.utils;

import lombok.Getter;

@Getter
public class AutoBanCheck {

    private final String check, type;

    private final int flags;


    public AutoBanCheck(String check, int flags){
        this.check = check;
        this.flags = flags;
        this.type = null;
    }

    public AutoBanCheck(String check, int flags, String type){
        this.check = check;
        this.flags = flags;
        this.type = type;
    }
}
