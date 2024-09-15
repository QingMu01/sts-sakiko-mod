package com.qingmu.sakiko.constant;

public enum SoundHelper {
    ANON_INIT("SakikoModResources/sounds/anon_init.ogg"), // 千早爱音desu
    ANON_YEAH("SakikoModResources/sounds/anon_yeah.ogg"), // yeah!
    ANON_LAUGH("SakikoModResources/sounds/anon_laugh.ogg"),// 唐笑
    ANON_DEATH("SakikoModResources/sounds/anon_cry.ogg"),// 唐哭
    MUTSUMI_INIT("SakikoModResources/sounds/mutsumi_init.ogg"), // 祥子要坏掉了
    MUTSUMI_DEATH("SakikoModResources/sounds/mutsumi_death.ogg"), // 无畏死亡
    NYAMUCHI_INIT("SakikoModResources/sounds/nyamuchi_init.ogg"),// 扣你鸡喵母喵母
    NYAMUCHI_DEATH("SakikoModResources/sounds/nyamuchi_death.ogg"),// 无畏爱
    RANA_INIT("SakikoModResources/sounds/rana_init.ogg"),// 无聊的女人
    RANA_MAGIC("SakikoModResources/sounds/rana_magic.ogg"), // 抹茶
    RANA_DEATH("SakikoModResources/sounds/rana_death.ogg"), // 一起玩乐队
    SOYO_INIT("SakikoModResources/sounds/soyo_init.ogg"),// 小祥，你终于来了
    SOYO_MAGIC("SakikoModResources/sounds/soyo_magic.ogg"),// 哦捏该
    SOYO_DEATH("SakikoModResources/sounds/soyo_death.ogg"),// 我大概，一辈子也忘不掉crychic了
    TAKI_INIT("SakikoModResources/sounds/taki_init.ogg"),// 灯在等你啊
    TAKI_DEATH("SakikoModResources/sounds/taki_death.ogg"),// 赛啊苦
    TOMORI_INIT("SakikoModResources/sounds/tomori_init.ogg"),// SAKI酱。。。
    TOMORI_MAGIC("SakikoModResources/sounds/tomori_magic.ogg"), // 大家都迷路了
    TOMORI_DEATH("SakikoModResources/sounds/tomori_death.ogg"), // 组一辈子乐队
    UIKA_INIT("SakikoModResources/sounds/uika_init.ogg"),// 小祥？
    UIKA_DEATH("SakikoModResources/sounds/uika_death.ogg"),// 无惧悲伤
    UMIRI_INIT("SakikoModResources/sounds/umiri_init.ogg"),// 海铃desu
    UMIRI_DEATH("SakikoModResources/sounds/umiri_death.ogg"); // 无惧恐惧

    private final String path;

    SoundHelper(String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }
}
