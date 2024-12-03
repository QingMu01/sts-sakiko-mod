package com.qingmu.sakiko.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.inteface.ModifiedMusicNumber;
import com.qingmu.sakiko.powers.FukkenPower;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Band_AVEMUJICA extends AbstractSakikoRelic implements ModifiedMusicNumber {
    // 遗物ID
    public static final String ID = ModNameHelper.make(Band_AVEMUJICA.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/avemujica_logo.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;

    public Band_AVEMUJICA() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void triggerOnPlayMusicCard(AbstractMusic music) {
        if (CardsHelper.isMusic(music)) {
            this.counter++;
            if (this.counter >= 3) {
                this.counter = 0;
                this.flash();
                AbstractPlayer player = DungeonHelper.getPlayer();
                this.addToBot(new RelicAboveCreatureAction(player, this));
                int random = AbstractDungeon.cardRandomRng.random(3);
                switch (random) {
                    case 0:
                        this.addToBot(new ApplyPowerAction(player, player, new StrengthPower(player, 1), 1));
                        break;
                    case 1:
                        this.addToBot(new ApplyPowerAction(player, player, new DexterityPower(player, 1), 1));
                        break;
                    case 2:
                        this.addToBot(new ApplyPowerAction(player, player, new KirameiPower(player, 1), 1));
                        break;
                    default:
                        this.addToBot(new ApplyPowerAction(player, player, new FukkenPower(player, 1), 1));
                }

            }
        }
    }

    @Override
    public void onEquip() {
        this.counter = 0;
    }
}
