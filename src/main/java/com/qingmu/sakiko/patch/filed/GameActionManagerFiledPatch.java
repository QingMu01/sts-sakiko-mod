package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

@SpirePatch(clz = GameActionManager.class, method = SpirePatch.CLASS)
public class GameActionManagerFiledPatch {

    public static SpireField<ArrayList<AbstractCard>> musicPlayedThisCombat = new SpireField<>(ArrayList::new);

    public static SpireField<ArrayList<AbstractCard>> musicPlayedThisTurn = new SpireField<>(ArrayList::new);
}
