package com.qingmu.sakiko.powers.monster;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.constant.MusicHelper;
import com.qingmu.sakiko.inteface.TriggerOnGameTimeChanged;
import com.qingmu.sakiko.powers.AbstractSakikoPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class FireBirdPower extends AbstractSakikoPower implements TriggerOnGameTimeChanged {

    public static final String POWER_ID = ModNameHelper.make(FireBirdPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/Kiramei48.png";
    private static final String path128 = "SakikoModResources/img/powers/Kiramei128.png";

    private boolean isStart = false;

    private int timer = 0;
    private int overtime = 0;

    private final int phase1 = 41;
    private final int phase2 = 266;
    private final int phase3 = 322;

    public FireBirdPower(AbstractCreature owner) {
        super(POWER_ID, NAME, -1, owner, PowerType.BUFF);

        this.loadRegion("time");
//        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
//        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        int nextTimer = 0;
        String desc = "";
        if (this.overtime == 0) {
            desc = DESCRIPTIONS[2];
            nextTimer = this.phase1;
        }
        if (this.overtime == 1) {
            desc = DESCRIPTIONS[3];
            nextTimer = this.phase2;
        }
        if (this.overtime == 2) {
            desc = DESCRIPTIONS[4];
            nextTimer = this.phase3;
        }
        this.amount = nextTimer - this.timer;
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + desc;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (!this.isStart){
            CardCrawlGame.music.silenceBGMInstantly();
            CardCrawlGame.music.silenceTempBgmInstantly();
            CardCrawlGame.music.playTempBgmInstantly(MusicHelper.FIREBIRD.name(), false);
        }
        this.isStart = true;
    }

    @Override
    public void everySecond() {
        if (!this.isStart) return;
        this.timer++;
        if (this.timer == this.phase1) {
            this.overtime++;
            System.out.println("召唤队友");
        }
        if (this.timer == this.phase2) {
            this.overtime++;
            System.out.println("强力负面");
        }
        if (this.timer == this.phase3) {
            this.overtime++;
            System.out.println("渎神");
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        this.updateDescription();
    }
}
