package com.myproject.leaderboard.enums;

public enum Badge {

    CODE_NINJA,
    CODE_CHAMP,
    CODE_MASTER;

    public String getBadgeName() {
        return this.name();
    }

}
