package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.characters.TogawaSakiko;

@SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
public class BossInfoFiled {

    public static SpireField<Boolean> canBattleWithDemonSakiko = new SpireField<>(() -> AbstractDungeon.player instanceof TogawaSakiko || SakikoModCore.SAKIKO_CONFIG.getBool("enableBoss"));

}
