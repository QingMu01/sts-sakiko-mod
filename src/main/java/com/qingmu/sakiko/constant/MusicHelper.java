package com.qingmu.sakiko.constant;

public enum MusicHelper {

    HARUHIKAGE("SakikoModResources/music/mygo_02_stem.mp3"),
    SHIORI("SakikoModResources/music/bgm513.mp3"),
    NINGENUTA("SakikoModResources/music/30. 人間になりたいうた.mp3"),
    AME("SakikoModResources/music/01. ショパン「雨だれ」.mp3"),
    AVEMUJICA("SakikoModResources/music/06. Ave Mujica.mp3"),
    MOONLIGHT("SakikoModResources/music/04. ぺートーヴェン「月光第3楽章」.mp3");

    private final String path;

    MusicHelper(String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }

}
