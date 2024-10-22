package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.ModNameHelper;

public class StageMachine extends AbstractSakikoCard {
    public static final String ID = ModNameHelper.make(StageMachine.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/StageMachine.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public StageMachine() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 5, 3);
        this.setUpgradeAttr(1, 0, 3, 1);

    }

    @Override
    protected void applyPowersToBlock() {
        int realBaseBlock = this.baseBlock;
        this.baseBlock += MusicBattleFiledPatch.MusicQueue.musicQueue.get(AbstractDungeon.player).size() * this.magicNumber;
        super.applyPowersToBlock();
        this.baseBlock = realBaseBlock;
        this.isBlockModified = (this.block != this.baseBlock);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
    }
}
