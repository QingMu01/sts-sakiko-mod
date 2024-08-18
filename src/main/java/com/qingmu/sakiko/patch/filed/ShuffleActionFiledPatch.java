package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public class ShuffleActionFiledPatch {

    @SpirePatch(clz = ShuffleAllAction.class, method = SpirePatch.CLASS)
    public static class ShuffleAllActionFiled {
        public static SpireField<ArrayList<AbstractCard>> moon_light = new SpireField<>(ArrayList::new);
    }

    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = SpirePatch.CLASS)
    public static class EmptyDeckShuffleActionFiled {
        public static SpireField<ArrayList<AbstractCard>> moon_light = new SpireField<>(ArrayList::new);
    }
}
