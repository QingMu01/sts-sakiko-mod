package com.qingmu.sakiko.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.utils.ModNameHelper;

public class AncientMask extends AbstractSakikoRelic {

    // 遗物ID
    public static final String ID = ModNameHelper.make(AncientMask.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/AncientMask.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;

    public AncientMask() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void onMonsterDeath(AbstractMonster m) {
        if ((m.isDying || m.currentHealth <= 0) && !m.halfDead && !m.hasPower("Minion")) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(m, this));
            AbstractDungeon.player.heal(8);
        }
    }
}
