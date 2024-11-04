package com.qingmu.sakiko.powers.monster;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.List;
import java.util.stream.Collectors;

public class CrychicKizunaPower extends AbstractPower {

    public static final String POWER_ID = ModNameHelper.make(CrychicKizunaPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/CrychicPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/CrychicPower128.png";


    public CrychicKizunaPower(AbstractCreature owner) {
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
        if (AbstractDungeon.actionManager.currentAction instanceof DamageAllEnemiesAction || info.type == DamageInfo.DamageType.THORNS || this.owner.isPlayer || damageAmount <= 0){
            return damageAmount;
        } else {
            List<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters.stream().filter(m -> !m.isDead && m.hasPower(POWER_ID)).collect(Collectors.toList());
            int powerCount = monsters.size();
            if (powerCount <= 1) {
                return damageAmount;
            } else {
                if (damageAmount < powerCount) {
                    return damageAmount;
                } else {
                    // 计算每份的基本值
                    int baseValue = damageAmount / powerCount;
                    // 计算余数
                    int remainder = damageAmount % powerCount;
                    // 剩余的伤害，不通过action重新分配，而是直接返回
                    int selfDamage = 0;
                    // 分配值
                    for (int i = 0; i < powerCount; i++) {
                        AbstractMonster monster = monsters.get(i);
                        if (monster == this.owner){
                            selfDamage = baseValue + (i < remainder ? 1 : 0);
                        }else {
                            this.addToBot(new DamageAction(monsters.get(i), new DamageInfo(info.owner, baseValue + (i < remainder ? 1 : 0), DamageInfo.DamageType.THORNS), true));
                        }
                    }
                    return selfDamage;
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

}
