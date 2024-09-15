package com.qingmu.sakiko.constant;

public enum MusicHelper {

    HARUHIKAGE("SakikoModResources/music/mygo_02_stem.mp3");

    private final String path;

    MusicHelper(String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }

}
