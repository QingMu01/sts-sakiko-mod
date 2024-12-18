package com.qingmu.sakiko.relics;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.qingmu.sakiko.action.common.SummonFriendlyMonsterAction;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.monsters.friendly.LinkedAnon;
import com.qingmu.sakiko.stances.CreatorStance;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ClassicPiano extends AbstractSakikoRelic {

    // 遗物ID
    public static final String ID = ModNameHelper.make(ClassicPiano.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/ClassicPiano.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;

    public ClassicPiano() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER);
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_CREATOR), BaseMod.getKeywordDescription(SakikoConst.KEYWORD_CREATOR)));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(DungeonHelper.getPlayer(), this));
        this.addToBot(new ChangeStanceAction(CreatorStance.STANCE_ID));
    }

    @Override
    public void onRightClick() {
        if (Settings.isDebug) {
//            LinkedAnon linkedAnon = new LinkedAnon(0, 0);
//            linkedAnon.drawX -= (DungeonHelper.getPlayer().drawX + (Settings.WIDTH - linkedAnon.drawX) - DungeonHelper.getPlayer().hb_w - 20) / Settings.xScale;
//            this.addToBot(new SummonFriendlyMonsterAction(linkedAnon, true, -Settings.WIDTH));
            this.addToBot(new SummonFriendlyMonsterAction(LinkedAnon.ID, true));
        }
    }
}
