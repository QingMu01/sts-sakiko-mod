package com.qingmu.sakiko.cards.music;

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
import com.qingmu.sakiko.action.common.ReadyToPlayMusicAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.powers.FeverReadyPower;

import static com.qingmu.sakiko.constant.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public abstract class AbstractMusic extends AbstractSakikoCard {

    private static final String BG_SKILL_512 = "SakikoModResources/img/512/bg_skill_512.png";
    private static final String BG_SKILL_1024 = "SakikoModResources/img/1024/bg_skill.png";

    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardType CARD_TYPE = SakikoEnum.CardTypeEnum.MUSIC;

    public AbstractCreature music_source;
    public AbstractCreature music_target;

    // 额外加成
    public int extraNumber = 0;

    // 计数器
    public int amount = 0;

    // 打出时的回合数
    public int usedTurn = 0;


    public AbstractMusic(String id, String img, CardRarity rarity, CardTarget target) {
        super(id, img, CARD_TYPE, rarity, target);
        super.setBackgroundTexture(BG_SKILL_512, BG_SKILL_1024);
    }

    @Override
    public void initializeDescription() {
        super.initializeDescription();
        this.keywords.add("sakikomod:" + CARD_TYPE.toString());
    }

    // 实现的时候最好使用addToTop()方法，否则会导致所有被演奏卡牌的演奏动画播放完毕才生效
    public abstract void play();

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.music_source = p;
        this.music_target = m == null ? AbstractDungeon.getRandomMonster() : m;
        this.usedTurn = GameActionManager.turn;
        this.addToBot(new ApplyPowerAction(p, p, new FeverReadyPower(p, 1)));
    }


    @Override
    public void onChoseThisOption() {
        this.use(AbstractDungeon.player, AbstractDungeon.getRandomMonster());
        MusicBattleFiled.MusicQueue.musicQueue.get(AbstractDungeon.player).addToBottom(this);
        this.addToBot(new ReadyToPlayMusicAction(1));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.applyAmount();
    }

    // 更新计数
    public void applyAmount() {
    }

    // 存在待演奏区时，有卡牌被打出时触发的钩子
    public void triggerInBufferUsedCard(AbstractCard card) {
    }

    // 存在待演奏区时，演奏时触发的钩子
    public void triggerInBufferPlayedMusic(AbstractMusic music) {
    }

    public void resetCount() {
        this.amount = 0;
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
