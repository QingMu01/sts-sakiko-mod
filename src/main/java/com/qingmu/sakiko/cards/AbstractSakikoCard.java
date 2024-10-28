package com.qingmu.sakiko.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.CardStringsMiniTitleField;
import com.qingmu.sakiko.utils.ActionHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class AbstractSakikoCard extends CustomCard {

    private final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(this.cardID);

    public final String NAME = CARD_STRINGS.NAME;
    public final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public final String UPGRADE_DESCRIPTION = CARD_STRINGS.UPGRADE_DESCRIPTION;
    public final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;

    protected String MINI_TITLE = CardStringsMiniTitleField.miniTitle.get(CARD_STRINGS);

    protected int upgradeCost = -2;
    protected int upgradeDamage = 0;
    protected int upgradeBlock = 0;
    protected int upgradeMagicNumber = 0;

    protected boolean baseExhaust = false;
    protected boolean upgExhaust = false;
    protected boolean baseEthereal = false;
    protected boolean upgEthereal = false;
    protected boolean baseInnate = false;
    protected boolean upgInnate = false;
    protected boolean baseRetain = false;
    protected boolean upgRetain = false;

    protected boolean upgradePreview = false;
    protected List<AbstractCard> previewCardList = new ArrayList<>();

    private float timer = 0.0f;

    public AbstractSakikoCard(String id, String img, AbstractCard.CardType type, CardRarity rarity, CardTarget target) {
        this(id, "", img, -2, "", type, SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD, rarity, target);
        this.originalName = NAME;
        this.name = NAME;
        this.rawDescription = DESCRIPTION;
        this.initializeTitle();
        this.initializeDescription();
    }

    public AbstractSakikoCard(String id, String img, AbstractCard.CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(id, "", img, -2, "", type, color, rarity, target);
        this.originalName = NAME;
        this.name = NAME;
        this.rawDescription = DESCRIPTION;
        this.initializeTitle();
        this.initializeDescription();
    }

    public AbstractSakikoCard(String id, String name, String img, int cost, String rawDescription, AbstractCard.CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    protected void initBaseAttr(int cost, int baseDamage, int baseBlock, int baseMagicNumber, AbstractCard... previewCard) {
        this.cost = cost;
        this.costForTurn = cost;

        this.baseDamage = baseDamage;
        this.baseBlock = baseBlock;
        this.baseMagicNumber = baseMagicNumber;
        this.magicNumber = baseMagicNumber;

        if (previewCard != null && previewCard.length > 0) {
            this.cardsToPreview = previewCard[0];
            this.previewCardList.addAll(Arrays.asList(previewCard));
        }
    }


    protected void submitActionsToBot(AbstractGameAction... actions) {
        if (actions != null && actions.length > 0) {
            if (actions.length > 1) {
                ActionHelper.actionListToBot(actions);
            } else {
                this.addToBot(actions[0]);
            }
        }

    }

    protected void submitActionsToTop(AbstractGameAction... actions) {
        if (actions != null && actions.length > 0) {
            if (actions.length > 1) {
                ActionHelper.actionListToTop(actions);
            } else {
                this.addToTop(actions[0]);
            }
        }
    }

    protected void setUpgradeAttr(int cost, int upgradeDamage, int upgradeBlock, int upgradeMagicNumber) {
        this.setUpgradeAttr(cost, upgradeDamage, upgradeBlock, upgradeMagicNumber, false);
    }

    protected void setUpgradeAttr(int cost, int upgradeDamage, int upgradeBlock, int upgradeMagicNumber, boolean upgradePreview) {
        this.upgradeCost = cost;
        this.upgradeDamage = upgradeDamage;
        this.upgradeBlock = upgradeBlock;
        this.upgradeMagicNumber = upgradeMagicNumber;
        this.upgradePreview = upgradePreview;
    }

    protected final void setExhaust(boolean baseExhaust, boolean upgExhaust) {
        this.baseExhaust = baseExhaust;
        this.upgExhaust = upgExhaust;
        this.exhaust = baseExhaust;
    }

    protected final void setEthereal(boolean baseEthereal, boolean upgEthereal) {
        this.baseEthereal = baseEthereal;
        this.upgEthereal = upgEthereal;
        this.isEthereal = baseEthereal;
    }

    protected void setInnate(boolean baseInnate, boolean upgInnate) {
        this.baseInnate = baseInnate;
        this.upgInnate = upgInnate;
        this.isInnate = baseInnate;
    }

    protected void setSelfRetain(boolean baseRetain, boolean upgRetain) {
        this.baseRetain = baseRetain;
        this.upgRetain = upgRetain;
        this.selfRetain = baseRetain;
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDescription();

            if (this.upgradeCost != this.cost)
                this.upgradeBaseCost(this.upgradeCost);

            if (this.upgradeDamage > 0)
                this.upgradeDamage(this.upgradeDamage);

            if (this.upgradeBlock > 0)
                this.upgradeBlock(this.upgradeBlock);

            if (this.upgradeMagicNumber != 0)
                this.upgradeMagicNumber(this.upgradeMagicNumber);

            if (!this.previewCardList.isEmpty() && this.upgradePreview)
                this.previewCardList.forEach(AbstractCard::upgrade);

            if (baseExhaust ^ upgExhaust)
                this.exhaust = upgExhaust;

            if (baseInnate ^ upgInnate)
                this.isInnate = upgInnate;

            if (baseEthereal ^ upgEthereal)
                this.isEthereal = upgEthereal;

            if (baseRetain ^ upgRetain)
                this.selfRetain = upgRetain;
        }
    }

    // 升级卡牌时自动更新说明
    protected void upgradeDescription() {
        this.rawDescription = UPGRADE_DESCRIPTION == null ? DESCRIPTION : UPGRADE_DESCRIPTION.isEmpty() ? DESCRIPTION : UPGRADE_DESCRIPTION;
        this.initializeDescription();
    }

    // 添加额外说明
    protected void appendDescription(Object... strings) {
        if (this.upgraded) {
            this.rawDescription = (Objects.nonNull(UPGRADE_DESCRIPTION) && !UPGRADE_DESCRIPTION.isEmpty()) ? UPGRADE_DESCRIPTION : DESCRIPTION;
            this.rawDescription += String.format(EXTENDED_DESCRIPTION[0], strings);
        } else {
            this.rawDescription = DESCRIPTION + String.format(EXTENDED_DESCRIPTION[0], strings);
        }
        this.initializeDescription();
    }

    // 轮播关联卡牌
    @Override
    public void update() {
        super.update();
        if (this.hb.hovered) {
            if (this.previewCardList.size() > 1) {
                this.timer += Gdx.graphics.getDeltaTime();
                int index = (int) Math.floor(this.timer % this.previewCardList.size());
                this.cardsToPreview = this.previewCardList.get(index);
            }
        } else {
            this.timer = 0.0f;
        }
    }

    @Override
    public void renderCardPreviewInSingleView(SpriteBatch sb) {
        if (this.previewCardList.size() > 1) {
            this.timer += Gdx.graphics.getDeltaTime();
            int index = (int) Math.floor(this.timer % this.previewCardList.size());
            this.cardsToPreview = this.previewCardList.get(index);
        }
        super.renderCardPreviewInSingleView(sb);
    }

    // 渲染小标题
    @Override
    public void renderTitle(SpriteBatch sb) {
        super.renderTitle(sb);
        if (this.MINI_TITLE.isEmpty()) {
            return;
        }
        float offsetY = 410.0F * Settings.scale * this.drawScale / 2.0F;
        BitmapFont.BitmapFontData fontData = FontHelper.cardTitleFont.getData();
        float originalScale = fontData.scaleX;
        float scaleMultiplier = 0.8F;
        fontData.setScale(scaleMultiplier * this.drawScale * 0.85F);
        Color color = Settings.CREAM_COLOR.cpy();
        color.a = this.transparency;
        FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, this.MINI_TITLE, this.current_x, this.current_y, 0.0F, offsetY, this.angle, true, color);
        fontData.setScale(originalScale);
    }


    // 演奏时触发的钩子
    public void triggerOnPlayMusic(AbstractMusic music) {
    }
}
