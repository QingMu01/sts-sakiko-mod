package com.qingmu.sakiko.cards;

import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.qingmu.sakiko.action.common.ReadyToPlayMusicAction;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.inteface.ModifiedMusicNumber;
import com.qingmu.sakiko.modifier.AbstractMusicCardModifier;
import com.qingmu.sakiko.powers.FeverReadyPower;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public abstract class AbstractMusic extends AbstractSakikoCard {

    private static final String BG_SKILL_512 = "SakikoModResources/img/512/bg_skill_512.png";
    private static final String BG_SKILL_1024 = "SakikoModResources/img/1024/bg_skill.png";

    private static final CardType CARD_TYPE = SakikoEnum.CardTypeEnum.MUSIC;

    public AbstractCreature m_source;
    public AbstractCreature m_target;

    // 音乐牌专用变量
    public int amount = 0;

    public int baseMusicNumber = 0;
    public int musicNumber = -1;
    public boolean isModifiedMusicNumber = false;
    public boolean upgradedMusicNumber = false;

    public boolean cryChicSelect = false;

    protected int upgradeMusicNumber = 0;


    public AbstractMusic(String id, String img, CardRarity rarity, CardTarget target) {
        super(id, img, CARD_TYPE, rarity, target);
        super.setBackgroundTexture(BG_SKILL_512, BG_SKILL_1024);
        this.keywords.add(SakikoConst.KEYWORD_PREFIX + CARD_TYPE.toString());
    }

    public void initMusicAttr(int baseMusicNumber, int upgradeMusicNumber, AbstractCard... previewCard) {
        this.initBaseAttr(0, baseMusicNumber, baseMusicNumber, 0, previewCard);
        this.setUpgradeAttr(0, upgradeMusicNumber, upgradeMusicNumber, 0);
        this.baseMusicNumber = baseMusicNumber;
        this.musicNumber = baseMusicNumber;
        this.upgradeMusicNumber = upgradeMusicNumber;
    }

    public void initMusicAttr(int baseMusicNumber, int upgradeMusicNumber, int baseMagicNumber, int upgradeMagicNumber, AbstractCard... previewCard) {
        this.initBaseAttr(0, baseMusicNumber, baseMusicNumber, baseMagicNumber, previewCard);
        this.setUpgradeAttr(0, upgradeMusicNumber, upgradeMusicNumber, upgradeMagicNumber);
        this.baseMusicNumber = baseMusicNumber;
        this.musicNumber = baseMusicNumber;
        this.upgradeMusicNumber = upgradeMusicNumber;
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            if (this.upgradeMusicNumber != 0) {
                this.upgradedMusicNumber(this.upgradeMusicNumber);
            }
        }
        super.upgrade();
    }

    public void applyPowersToMusicNumber() {
        AbstractPlayer player = DungeonHelper.getPlayer();
        this.isModifiedMusicNumber = false;
        float tmp = this.baseMusicNumber;
        AbstractStance stance = player.stance;
        // 遗物修改数值
        for (AbstractRelic relic : player.relics) {
            if (relic instanceof ModifiedMusicNumber) {
                tmp = ((ModifiedMusicNumber) relic).modifyMusicNumber(this, tmp);
            }
        }
        // 能力修改数值
        for (AbstractPower power : player.powers) {
            if (power instanceof ModifiedMusicNumber) {
                tmp = ((ModifiedMusicNumber) power).modifyMusicNumber(this, tmp);
            }
        }
        for (AbstractPower power : player.powers) {
            if (power instanceof ModifiedMusicNumber) {
                tmp = ((ModifiedMusicNumber) power).finalModifyMusicNumber(this, tmp);
            }
        }
        // 姿态修改数值
        if (stance instanceof ModifiedMusicNumber) {
            tmp = ((ModifiedMusicNumber) stance).finalModifyMusicNumber(this, tmp);
        }
        if (tmp < 0.0F) {
            tmp = 0.0F;
        }
        if (this.baseMusicNumber != MathUtils.floor(tmp)) {
            this.isModifiedMusicNumber = true;
        }
        this.musicNumber = MathUtils.floor(tmp);
    }

    @Override
    protected void applyPowersToBlock() {
        this.applyPowersToMusicNumber();
        super.applyPowersToBlock();
    }

    public abstract void play();

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.m_source = p;
        this.m_target = m;
        this.addToBot(new ApplyPowerAction(p, p, new FeverReadyPower(p, 1)));
    }


    @Override
    public void onChoseThisOption() {
        this.use(DungeonHelper.getPlayer(), AbstractDungeon.getRandomMonster());
        CardsHelper.mq().addToBottom(this);
        this.addToBot(new ReadyToPlayMusicAction(1));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.applyAmount();
    }

    public void upgradedMusicNumber(int amount) {
        this.baseMusicNumber += amount;
        this.musicNumber = this.baseMusicNumber;
        this.upgradedMusicNumber = true;
    }

    @Override
    public void displayUpgrades() {
        if (this.upgradedMusicNumber) {
            this.musicNumber = this.baseMusicNumber;
            this.isModifiedMusicNumber = true;
        }
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        this.amount = 0;
        this.musicNumber = this.baseMusicNumber;
        this.isModifiedMusicNumber = false;
    }

    @Override
    public AbstractCard makeSameInstanceOf() {
        AbstractMusic card = (AbstractMusic) super.makeSameInstanceOf();
        card.baseMusicNumber = this.baseMusicNumber;
        card.isModifiedMusicNumber = this.isModifiedMusicNumber;
        card.cryChicSelect = this.cryChicSelect;
        return card;
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

    public boolean enchantedSupport() {
        return false;
    }

    public AbstractMusicCardModifier enchant() {
        return null;
    }

    @SpireOverride
    public void renderSkillPortrait(SpriteBatch sb, float x, float y) {
        SpireSuper.call(sb, x, y);
    }

    @SpireOverride
    public void renderHelper(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY) {
        SpireSuper.call(sb, color, img, drawX, drawY);
    }

    public static class MusicNumberVariable extends DynamicVariable {
        @Override
        public String key() {
            return ModNameHelper.make("MN");
        }

        @Override
        public boolean isModified(AbstractCard card) {
            if (card instanceof AbstractMusic) {
                AbstractMusic music = (AbstractMusic) card;
                return music.isModifiedMusicNumber;
            }
            return false;
        }

        @Override
        public int value(AbstractCard card) {
            if (card instanceof AbstractMusic) {
                AbstractMusic music = (AbstractMusic) card;
                return music.musicNumber;
            }
            return -1;
        }

        @Override
        public int baseValue(AbstractCard card) {
            if (card instanceof AbstractMusic) {
                AbstractMusic music = (AbstractMusic) card;
                return music.baseMusicNumber;
            }
            return -1;
        }

        @Override
        public boolean upgraded(AbstractCard card) {
            if (card instanceof AbstractMusic) {
                AbstractMusic music = (AbstractMusic) card;
                return music.upgradedMusicNumber;
            }
            return false;
        }
    }
}
