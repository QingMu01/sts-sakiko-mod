package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.qingmu.sakiko.action.common.SummonFriendlyMonsterAction;
import com.qingmu.sakiko.cards.music.AveMujica;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.monsters.friendly.LinkedAnon;
import com.qingmu.sakiko.relics.menbers.AbstractBandMember;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.MemberHelper;

import java.util.ArrayList;

public class SummonedByRelicAction extends AbstractGameAction {

    private final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(AveMujica.ID);


    public SummonedByRelicAction(int hp) {
        this.amount = (int) Math.max(1.0f, DungeonHelper.getPlayer().currentHealth * (hp / 100.0f));
    }

    @Override
    public void update() {
        int count = MemberHelper.getCount();
        if (count == 0) {
            AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0f, CARD_STRINGS.EXTENDED_DESCRIPTION[0], true));
        } else if (count == getSummonedCount()) {
            AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0f, CARD_STRINGS.EXTENDED_DESCRIPTION[1], true));
        } else {
            String id = this.getFirstEffectFriendlyMonsterId();
            if (id != null) {
                this.addToTop(new SummonFriendlyMonsterAction(id, this.amount));
            } else {
                AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0f, CARD_STRINGS.EXTENDED_DESCRIPTION[1], true));
            }
        }
        this.isDone = true;
    }

    private String getFirstEffectFriendlyMonsterId() {
        ArrayList<String> canSummoned = new ArrayList<>();
        for (AbstractRelic relic : DungeonHelper.getPlayer().relics) {
            if (relic instanceof AbstractBandMember) {
                canSummoned.add(relic.relicId + SakikoConst.MEMBER_SUFFIX);
            }
        }
        MonsterGroup friendlyMonsterGroup = DungeonHelper.getFriendlyMonsterGroup();
        if (friendlyMonsterGroup != null) {
            for (AbstractMonster monster : friendlyMonsterGroup.monsters) {
                canSummoned.remove(monster.id);
            }
        }
        if (canSummoned.isEmpty())
            return null;
        else
            return canSummoned.get(0);
    }

    private int getSummonedCount() {
        MonsterGroup friendlyMonsterGroup = DungeonHelper.getFriendlyMonsterGroup();
        int count = 0;
        if (friendlyMonsterGroup != null) {
            for (AbstractMonster monster : friendlyMonsterGroup.monsters) {
                if (!monster.id.equals(LinkedAnon.ID)) {
                    count++;
                }
            }
        }
        return count;
    }

}
