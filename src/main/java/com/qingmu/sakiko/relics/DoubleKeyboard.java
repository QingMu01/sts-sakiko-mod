package com.qingmu.sakiko.relics;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.stances.CreatorStance;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class DoubleKeyboard extends AbstractSakikoRelic {

    public static final String ID = ModNameHelper.make(DoubleKeyboard.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/relics/DoubleKeyboard.png";

    private static final RelicTier RELIC_TIER = RelicTier.BOSS;

    public DoubleKeyboard() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER);
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_FLOW),BaseMod.getKeywordDescription(SakikoConst.KEYWORD_FLOW)));
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_FEVER),BaseMod.getKeywordDescription(SakikoConst.KEYWORD_FEVER)));
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_OBLIVIOUS),BaseMod.getKeywordDescription(SakikoConst.KEYWORD_OBLIVIOUS)));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        if (DungeonHelper.getStance().ID.equals(NeutralStance.STANCE_ID)){
            this.addToBot(new ChangeStanceAction(CreatorStance.STANCE_ID));
        }
    }

    @Override
    public void obtain() {
        this.instantObtain(AbstractDungeon.player, 0, true);
        this.flash();
    }

    @Override
    public boolean canSpawn() {
        return DungeonHelper.getPlayer().hasRelic(ClassicPiano.ID);
    }

}
