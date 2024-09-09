package com.qingmu.sakiko.patch.anonmod;

import Mod.AnonMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.FeverReadyPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import power.musicStart;
import power.songs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpirePatch(requiredModId = "AnonMod", optional = true, clz = AnonMod.class, method = "loadCardsToAdd")
public class AnonMusicCardPatch {

    public static final List<String> ANON_SONGS = Arrays.asList("UtakotobaSongs",
            "HekitenbansouSongs", "NamonakiSongs", "HaruhikageSongs",
            "OtoichieSongs", "SenzaihyoumeiSongs",
            "HitoshizukuSongs", "KiritorisenSongs", "SilhouetteSongs");

    public static void Postfix(AnonMod __instance, @ByRef ArrayList<AbstractCard>[] ___cardsToAdd) {
        ArrayList<AbstractCard> tmpList = new ArrayList<>();
        for (AbstractCard card : ___cardsToAdd[0]) {
            if (ANON_SONGS.contains(card.getClass().getSimpleName())) {
                tmpList.add(new AbstractAnonMusic((CustomCard) card.makeCopy()));
            }
        }
        ___cardsToAdd[0].addAll(tmpList);
    }

    public static class AbstractAnonMusic extends AbstractMusic {

        private final AbstractCard anonCard;

        public AbstractAnonMusic(CustomCard anonCard) {
            super(ModNameHelper.make(anonCard.cardID), anonCard.name, ReflectionHacks.getPrivateStatic(anonCard.getClass(), "IMG_PATH"), anonCard.rawDescription, anonCard.rarity == CardRarity.RARE ? SakikoEnum.CardRarityEnum.MUSIC_RARE : SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON, anonCard.target);
            this.portrait = anonCard.portrait;
            this.jokePortrait = anonCard.jokePortrait;
            this.anonCard = anonCard;
            this.tags.add(SakikoEnum.CardTagEnum.ANON_MOD);
            this.cost = this.anonCard.cost;
            this.costForTurn = this.anonCard.cost;
            this.baseBlock = this.anonCard.baseBlock;
            this.baseDamage = this.anonCard.baseDamage;
            this.baseMagicNumber = this.anonCard.baseMagicNumber;
            this.setBackgroundTexture("img/pink/512/bg_skill.png", "img/pink/1024/bg_skill.png");
            this.setOrbTexture("img/UI/star.png", "img/UI/star_164.png");

        }

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            this.anonCard.use(p, m);
            this.usedTurn = GameActionManager.turn;
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FeverReadyPower(AbstractDungeon.player, 1)));

        }

        @Override
        public void play() {
            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    new musicStart(AbstractDungeon.player).musicEffect(AbstractDungeon.player, 0);
                    songs.SongsList[0] = "";
                    for (int i = 1; i < songs.SongsList.length; i++) {
                        songs.SongsList[i - 1] = songs.SongsList[i];
                    }
                    songs.SongsList[songs.SongsList.length - 1] = "";
                    if (AbstractDungeon.player.hasPower(songs.POWER_ID)) {
                        AbstractDungeon.player.getPower(songs.POWER_ID).updateDescription();
                    }
                    this.isDone = true;
                }
            });
        }

        @Override
        public void upgrade() {
            if (!this.upgraded) {
                this.anonCard.upgrade();
                this.name = this.anonCard.name;
                this.target = this.anonCard.target;
                this.upgraded = this.anonCard.upgraded;
                this.timesUpgraded = this.anonCard.timesUpgraded;
                this.baseDamage = this.anonCard.baseDamage;
                this.baseBlock = this.anonCard.baseBlock;
                this.baseMagicNumber = this.anonCard.baseMagicNumber;
                this.cost = this.anonCard.cost;
                this.costForTurn = this.anonCard.costForTurn;
                this.isCostModified = this.anonCard.isCostModified;
                this.isCostModifiedForTurn = this.anonCard.isCostModifiedForTurn;
                this.inBottleLightning = this.anonCard.inBottleLightning;
                this.inBottleFlame = this.anonCard.inBottleFlame;
                this.inBottleTornado = this.anonCard.inBottleTornado;
                this.isSeen = this.anonCard.isSeen;
                this.isLocked = this.anonCard.isLocked;
                this.misc = this.anonCard.misc;
                this.freeToPlayOnce = this.anonCard.freeToPlayOnce;
            }
        }

        @Override
        public void applyPowers() {
            this.anonCard.applyPowers();
        }

        @Override
        public void onChoseThisOption() {
            if (songs.SongsList[0].isEmpty()) {
                super.onChoseThisOption();
            } else {
                String[] shiftedArray = new String[songs.SongsList.length];
                shiftedArray[0] = "";
                System.arraycopy(songs.SongsList, 0, shiftedArray, 1, songs.SongsList.length - 1);
                songs.SongsList = shiftedArray;
                super.onChoseThisOption();
            }
        }

        @Override
        public AbstractCard makeCopy() {
            return new AbstractAnonMusic((CustomCard) this.anonCard);
        }
    }
}
