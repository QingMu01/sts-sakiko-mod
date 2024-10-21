package com.qingmu.sakiko.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Speaker extends AbstractSakikoRelic {

    // 遗物ID
    public static final String ID = ModNameHelper.make(Speaker.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/Speaker.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;

    public Speaker() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        CardGroup cardGroup = MusicBattleFiled.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player);
        if (!cardGroup.isEmpty()) {
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new CardSelectorAction("", 1, false, card -> CardGroup.CardGroupType.UNSPECIFIED, action -> {
                for (AbstractCard card : action.selected) {
                    AbstractDungeon.player.limbo.group.add(card);
                    card.current_y = -200.0F * Settings.scale;
                    card.target_x = Settings.WIDTH / 2.0F;
                    card.target_y = Settings.HEIGHT / 2.0F;
                    card.targetAngle = 0.0F;
                    card.lighten(false);
                    card.drawScale = 0.12F;
                    card.targetDrawScale = 0.75F;
                    this.addToBot(new NewQueueCardAction(card, AbstractDungeon.getRandomMonster(), false, true));
                }
            }, SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE));
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player instanceof TogawaSakiko;
    }
}
