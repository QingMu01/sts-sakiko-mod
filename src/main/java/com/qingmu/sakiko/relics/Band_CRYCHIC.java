package com.qingmu.sakiko.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.ImmediatelyPlayModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Band_CRYCHIC extends AbstractSakikoRelic implements CustomBottleRelic, CustomSavable<Integer> {
    // 遗物ID
    public static final String ID = ModNameHelper.make(Band_CRYCHIC.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/crychic_logo.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;

    private boolean cardSelected = true;
    private Integer cardIndex;
    private AbstractMusic card;

    public Band_CRYCHIC() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return c -> c instanceof AbstractMusic && ((AbstractMusic) c).cryChicSelect;
    }

    public void onEquip() {
        if (AbstractDungeon.player.masterDeck.getPurgeableCards().group.stream().anyMatch(c -> c instanceof AbstractMusic)) {
            this.cardSelected = false;
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard abstractCard : AbstractDungeon.player.masterDeck.getPurgeableCards().group.stream().filter(c -> c instanceof AbstractMusic).collect(Collectors.toList())) {
                cardGroup.addToBottom(abstractCard);
            }
            AbstractDungeon.gridSelectScreen.open(cardGroup, 1, this.DESCRIPTIONS[1] + this.name + LocalizedStrings.PERIOD, false, false, false, false);
        }

    }

    public void onUnequip() {
        if (this.card != null) {
            AbstractMusic cardInDeck = (AbstractMusic) AbstractDungeon.player.masterDeck.getSpecificCard(this.card);
            if (cardInDeck != null) {
                cardInDeck.cryChicSelect = false;
            }
        }
    }

    public void atBattleStart() {
        this.flash();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public Integer onSave() {
        return AbstractDungeon.player.masterDeck.group.indexOf(this.card);
    }

    @Override
    public void onLoad(Integer integer) {
        this.cardIndex = integer;
        if (this.cardIndex >= 0 && this.cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            this.card = (AbstractMusic) AbstractDungeon.player.masterDeck.group.get(this.cardIndex);
            if (this.card != null) {
                this.card.cryChicSelect = true;
                if (!this.card.hasTag(SakikoEnum.CardTagEnum.ENCORE)){
                    CardModifierManager.addModifier(this.card, new ImmediatelyPlayModifier());
                }
                setDescriptionAfterLoading();
            }
        }
    }

    public void update() {
        super.update();
        if (!this.cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            this.cardSelected = true;
            this.card = (AbstractMusic) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            this.card.cryChicSelect = true;
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.description = this.DESCRIPTIONS[2] + FontHelper.colorString(this.card.name, "y") + this.DESCRIPTIONS[3];
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();
        }
    }

    public void setDescriptionAfterLoading() {
        this.description = this.DESCRIPTIONS[2] + FontHelper.colorString(this.card.name, "y") + this.DESCRIPTIONS[3];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }
}
