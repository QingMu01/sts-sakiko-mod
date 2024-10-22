package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(clz = AbstractDungeon.class, method = SpirePatch.CLASS)
public class BossInfoFiled {

    public static SpireField<Boolean> canBattleWithDemonSakiko = new SpireField<>(() -> true);

}
