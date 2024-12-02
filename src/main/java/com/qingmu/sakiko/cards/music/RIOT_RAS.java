package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class RIOT_RAS extends AbstractMusic {

    public static final String ID = ModNameHelper.make(RIOT_RAS.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/RIOT_RAS.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public RIOT_RAS() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(8, 4);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        this.amount = this.musicNumber;
    }

    @Override
    public void play() {
    }

    @Override
    public int onLoseHpLast(int damageAmount) {
        if (this.amount > 0 && damageAmount > 0) {
            AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(this.hb.cX, this.hb.cY));
            int tmp = this.amount - damageAmount;
            if (tmp > 0) {
                this.amount = tmp;
                return 0;
            } else {
                this.amount = 0;
                return -tmp;
            }
        } else {
            return damageAmount;
        }
    }
}
