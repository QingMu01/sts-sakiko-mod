package com.qingmu.sakiko.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class NoteBook extends AbstractSakikoRelic {

    // 遗物ID
    public static final String ID = ModNameHelper.make(NoteBook.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/Notebook.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.RARE;

    public NoteBook() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.addToBot(new RelicAboveCreatureAction(DungeonHelper.getPlayer(), this));
        this.addToBot(new ApplyPowerAction(DungeonHelper.getPlayer(), DungeonHelper.getPlayer(), new KirameiPower(DungeonHelper.getPlayer(), 3)));
    }

    @Override
    public boolean canSpawn() {
        return DungeonHelper.isSakiko();
    }
}
