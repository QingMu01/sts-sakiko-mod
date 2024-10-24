package com.qingmu.sakiko.powers.monster;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.qingmu.sakiko.monsters.boss.InnerDemonSakiko;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AveMujicaDictatorshipPower extends AbstractPower {

    public static final String POWER_ID = ModNameHelper.make(AveMujicaDictatorshipPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/AveMujicaPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/AveMujicaPower128.png";


    public AveMujicaDictatorshipPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = -1;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.THORNS || this.owner.isPlayer || damageAmount <= 0) {
            return damageAmount;
        } else {
            ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
            int powerCount = (int) monsters.stream().filter(m -> m.hasPower(MinionPower.POWER_ID)).count();
            if (powerCount == 0) {
                return damageAmount;
            } else {
                if (damageAmount < powerCount) {
                    return damageAmount;
                } else {
                    int[] parts = new int[powerCount];
                    // 计算每份的基本值
                    int baseValue = damageAmount / powerCount;
                    // 计算余数
                    int remainder = damageAmount % powerCount;
                    // 分配值
                    for (int i = 0; i < powerCount; i++) {
                        parts[i] = baseValue + (i < remainder ? 1 : 0);
                    }
                    List<AbstractMonster> collect = monsters.stream().filter(m -> !m.id.equals(InnerDemonSakiko.ID)).collect(Collectors.toList());
                    for (int i = 0; i < collect.size(); i++) {
                        if (collect.get(i).hasPower(MinionPower.POWER_ID)){
                            this.addToBot(new DamageAction(collect.get(i), new DamageInfo(this.owner, parts[i], DamageInfo.DamageType.THORNS), true));
                        }
                    }
                    return 0;
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

}
