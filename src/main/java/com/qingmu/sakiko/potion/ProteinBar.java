package com.qingmu.sakiko.potion;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.action.StoryAction;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;


public class ProteinBar extends AbstractSakikoPotion {

    public static final String ID = ModNameHelper.make(ProteinBar.class.getSimpleName());

    private static final PotionStrings POTION_STRINGS = CardCrawlGame.languagePack.getPotionString(ID);
    private static final String NAME = POTION_STRINGS.NAME;
    private static final String[] DESCRIPTIONS = POTION_STRINGS.DESCRIPTIONS;

    private static final String POTION_IMG = "images/potion/potion_h_glass.png";

    private static final boolean THROWABLE = false;
    private static final boolean TARGET_REQUEST = false;

    public ProteinBar() {
        super(NAME, ID, PotionRarity.PLACEHOLDER, POTION_IMG, "images/blank.png", THROWABLE, TARGET_REQUEST);
    }

    public void initializeData() {
        this.potency = this.getPotency();
        this.description = DESCRIPTIONS[0] + this.potency + DESCRIPTIONS[1] + this.potency * 2 + DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToBot(new StoryAction(this.potency, true));
            this.addToBot(new ApplyPowerAction(DungeonHelper.getPlayer(), DungeonHelper.getPlayer(), new StrengthPower(DungeonHelper.getPlayer(), this.potency * 2), this.potency * 2));
        }
    }

    @Override
    public int getPotency(int i) {
        return 1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new ProteinBar();
    }
}
