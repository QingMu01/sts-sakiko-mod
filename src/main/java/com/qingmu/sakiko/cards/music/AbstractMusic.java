package com.qingmu.sakiko.cards.music;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.ReadyToPlayMusicAction;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.powers.FeverReadyPower;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public abstract class AbstractMusic extends CustomCard {

    private static final String BG_SKILL_512 = "SakikoModResources/img/512/bg_skill_512.png";
    private static final String BG_SKILL_1024 = "SakikoModResources/img/1024/bg_skill.png";

    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardType CARD_TYPE = SakikoEnum.CardTypeEnum.MUSIC;

    public AbstractCreature music_source;
    public AbstractCreature music_target;

    // 计数器
    public int amount = 0;
    public int count = 0;

    // 打出时的回合数
    public int usedTurn = 0;

    public AbstractMusic(String id, String name, String img, String rawDescription, CardRarity rarity, CardTarget target) {
        this(id, name, img, rawDescription, COLOR, rarity, target);
    }

    public AbstractMusic(String id, String name, String img, String rawDescription, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, 0, rawDescription, CARD_TYPE, color, rarity, target);
        super.setBackgroundTexture(BG_SKILL_512, BG_SKILL_1024);
    }


    // 实现的时候最好使用addToTop()方法，否则会导致所有被演奏卡牌的演奏动画播放完毕才生效
    public abstract void play();

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.music_source = p == null ? AbstractDungeon.player : p;
        this.music_target = m == null ? AbstractDungeon.getRandomMonster() : m;
        this.usedTurn = GameActionManager.turn;
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FeverReadyPower(AbstractDungeon.player, 1)));
    }


    @Override
    public void onChoseThisOption() {
        this.use(null, null);
        MusicBattleFiledPatch.MusicQueue.musicQueue.get(AbstractDungeon.player).addToBottom(this);
        this.addToTop(new ReadyToPlayMusicAction(1));
    }

    // 更新计数
    public void applyAmount() {
    }

    // 存在待演奏区时，有卡牌被打出时触发的钩子
    public void triggerInBufferPlayCard(AbstractCard card) {
        if (this.hasTag(SakikoEnum.CardTagEnum.COUNTER)) {
            this.count++;
            this.amount = this.calculateCardAmount(this.count, Math.min(this.magicNumber, this.baseMagicNumber));
        }
    }

    public int calculateCardAmount(int count, int countdown) {
        return count / countdown;
    }


    @SpireOverride
    public void renderSkillPortrait(SpriteBatch sb, float x, float y) {
        SpireSuper.call(sb, x, y);
    }

    @SpireOverride
    public void renderHelper(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY) {
        SpireSuper.call(sb, color, img, drawX, drawY);
    }
}
