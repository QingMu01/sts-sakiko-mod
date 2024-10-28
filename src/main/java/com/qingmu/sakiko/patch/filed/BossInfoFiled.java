package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.utils.DungeonHelper;

@SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
public class BossInfoFiled {

    public static SpireField<Boolean> canBattleWithDemonSakiko = new SpireField<>(() -> DungeonHelper.isSakiko() || SakikoModCore.SAKIKO_CONFIG.getBool("enableBoss"));

}
