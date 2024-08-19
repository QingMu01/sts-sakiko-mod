package com.qingmu.sakiko.patch.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.MummifiedHand;
import com.qingmu.sakiko.patch.SakikoEnum;

@SpirePatch(clz = MummifiedHand.class,method = "onUseCard")
public class MummifiedHandSupportMusicPatch {

    public static void Prefix(MummifiedHand __instance, AbstractCard card, UseCardAction action) {
        if (card.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
            card.type = AbstractCard.CardType.POWER;
        }
    }

    public static void Postfix(MummifiedHand __instance, AbstractCard card, UseCardAction action) {
        if (card.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
            card.type = SakikoEnum.CardTypeEnum.MUSIC;
        }
    }
}
