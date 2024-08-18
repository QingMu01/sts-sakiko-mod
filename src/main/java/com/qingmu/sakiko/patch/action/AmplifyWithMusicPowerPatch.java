package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AmplifyPower;
import com.qingmu.sakiko.patch.SakikoEnum;

@SpirePatch(clz= AmplifyPower.class,method = "onUseCard")
public class AmplifyWithMusicPowerPatch {

    /*
    * 让鸡煲的增幅 对音乐卡牌生效。
    * */
    public static void Prefix(AmplifyPower __instance, AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse && __instance.amount > 0 && card.type == SakikoEnum.CardTypeEnum.MUSIC && card.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)){
            __instance.flash();
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster)action.target;
            }

            AbstractCard tmp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float)Settings.HEIGHT / 2.0F;
            if (m != null) {
                tmp.calculateCardDamage(m);
            }

            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
            --__instance.amount;
            if (__instance.amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(__instance.owner, __instance.owner, "Amplify"));
            }

        }
    }

}
