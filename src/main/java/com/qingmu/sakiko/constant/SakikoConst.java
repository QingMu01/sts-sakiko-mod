package com.qingmu.sakiko.constant;

import com.qingmu.sakiko.monsters.member.*;
import com.qingmu.sakiko.relics.menbers.*;

import java.util.Arrays;
import java.util.List;

public class SakikoConst {

    public static final List<String> BAND_MEMBER_LIST = Arrays.asList(UikaMonster.ID, MutsumiMonster.ID, UmiriMonster.ID, NyamuchiMonster.ID, TomoriMonster.ID,AnonMonster.ID,SoyoMonster.ID,TakiMonster.ID,RanaMonster.ID);

    public static final List<String> AVE_MUJICA = Arrays.asList(Uika.ID, Mutsumi.ID, Nyamuchi.ID, Umiri.ID);

    public static final List<String> CRYCHIC = Arrays.asList(Tomori.ID, Taki.ID, Soyo.ID, Mutsumi.ID);


    // 姿态转换阈值
    public static final int STANCE_CHANGE_THRESHOLD = 3;
    public static int STANCE_CHANGE_THRESHOLD_USED = 3;

    // 心流替换阈值
    public static final int FLOW_THRESHOLD = 10;
    public static int FLOW_THRESHOLD_USED = 10;

    // 维持忘却的阈值
    public static final int OBLIVIOUS_STANCE_THRESHOLD = 4;
    public static int OBLIVIOUS_STANCE_THRESHOLD_USED = 4;

    // 音乐队列大小
    public static final int MUSIC_QUEUE_LIMIT = 3;
    public static int MUSIC_QUEUE_LIMIT_USED = 3;

    // 队友召唤ID后缀
    public static final String MEMBER_SUFFIX = "MonsterFriendly";

    public static final String KEYWORD_PREFIX = "sakikomod:";

    public static final String KEYWORD_KIRAMEI = KEYWORD_PREFIX + "KIRAMEI";
    public static final String KEYWORD_FUKKEN = KEYWORD_PREFIX + "FUKKEN";
    public static final String KEYWORD_KABE = KEYWORD_PREFIX + "KOKORONOKABE";

    public static final String KEYWORD_MEMBER = KEYWORD_PREFIX + "MEMBER";

    public static final String KEYWORD_CREATOR = KEYWORD_PREFIX + "CREATOR";
    public static final String KEYWORD_PLAYER = KEYWORD_PREFIX + "PLAYER";
    public static final String KEYWORD_FEVER = KEYWORD_PREFIX + "FEVER";
    public static final String KEYWORD_OBLIVIOUS = KEYWORD_PREFIX + "OBLIVIOUS";

    public static final String KEYWORD_FLOW = KEYWORD_PREFIX + "FLOW";
    public static final String KEYWORD_FLOW_BAR = KEYWORD_PREFIX + "FLOW_BAR";

    public static final String KEYWORD_FIRE = KEYWORD_PREFIX + "FIRE";
    public static final String KEYWORD_EARTH = KEYWORD_PREFIX + "EARTH";
    public static final String KEYWORD_ETHER = KEYWORD_PREFIX + "ETHER";

    public static final String KEYWORD_MYGO = KEYWORD_PREFIX + "MYGO";
}
