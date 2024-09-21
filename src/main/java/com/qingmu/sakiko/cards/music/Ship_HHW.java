package com.qingmu.sakiko.cards.music;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Ship_HHW extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Ship_HHW.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Ship_HHW.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Ship_HHW() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 0);

        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDescription();
        }

    }

    @Override
    public void play() {
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (monster.type != AbstractMonster.EnemyType.BOSS || this.upgraded) {
                this.addToTop(new StunMonsterAction(monster, this.music_source, 1));
            }
        }
    }

}
