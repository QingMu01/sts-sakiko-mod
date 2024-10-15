package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
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
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);
        this.tags.add(SakikoEnum.CardTagEnum.ENCORE);

        this.initMusicAttr(8, 5);
    }

    @Override
    public void triggerInBufferPlayedMusic(AbstractMusic music) {
        this.amount++;
    }

    @Override
    public void applyAmount() {
        this.appendDescription(this.amount + 1);
    }

    @Override
    public void play() {
        AbstractGameAction[] actions = new AbstractGameAction[this.amount + 1];
        actions[0] = new DamageAction(this.m_target, new DamageInfo(this.m_source, this.musicNumber, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        for (int i = 1; i < actions.length; i++) {
            actions[i] = new DamageAction(this.m_target, new DamageInfo(this.m_source, this.musicNumber, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        }
        this.submitActionsToTop(actions);
    }
}
