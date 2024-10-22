package com.qingmu.sakiko.cards.music.monster;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.monsters.boss.InnerDemonSakiko;
import com.qingmu.sakiko.powers.monster.IdealFukkenPower;
import com.qingmu.sakiko.utils.ModNameHelper;

@SakikoModEnable(enable = false)
public class AbolitionCase extends AbstractMusic {

    public static final String ID = ModNameHelper.make(AbolitionCase.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Haruhikage_CryChic.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public AbolitionCase() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(0, 0, 1, 0);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void triggerWhenDrawn() {
        this.addToTop(new NewQueueCardAction(this, false, true, true));
    }

    @Override
    public void play() {
        AbstractMonster monster = AbstractDungeon.getCurrRoom().monsters.getMonster(InnerDemonSakiko.ID);
        if (monster != null) {
            this.addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new IdealFukkenPower(monster, this.magicNumber), this.magicNumber));
        }
    }
}
