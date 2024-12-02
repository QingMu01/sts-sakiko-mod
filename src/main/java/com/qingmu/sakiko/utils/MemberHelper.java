package com.qingmu.sakiko.utils;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.powers.FallApartPower;
import com.qingmu.sakiko.relics.*;
import com.qingmu.sakiko.relics.menbers.AbstractBandMember;

import java.util.HashMap;
import java.util.Map;


public class MemberHelper {

    public static int getCount() {
        int count = 0;
        AbstractPlayer p = DungeonHelper.getPlayer();
        if (p.hasPower(FallApartPower.POWER_ID)) {
            p.getPower(FallApartPower.POWER_ID).flash();
            return count;
        }
        for (AbstractRelic relic : p.relics) {
            if (relic instanceof AbstractBandMember) {
                count++;
            }
        }
        return count;
    }

    public static int getAveMujicaCount() {
        int count = 0;
        AbstractPlayer p = DungeonHelper.getPlayer();
        if (p.hasPower(FallApartPower.POWER_ID)) {
            p.getPower(FallApartPower.POWER_ID).flash();
            return count;
        }
        for (AbstractRelic relic : p.relics) {
            if (SakikoConst.AVE_MUJICA.contains(relic.relicId)) {
                count++;
            }
        }
        return count;
    }

    public static int getCrychicCount() {
        int count = 0;
        AbstractPlayer p = DungeonHelper.getPlayer();
        if (p.hasPower(FallApartPower.POWER_ID)) {
            p.getPower(FallApartPower.POWER_ID).flash();
            return count;
        }
        for (AbstractRelic relic : p.relics) {
            if (SakikoConst.CRYCHIC.contains(relic.relicId)) {
                count++;
            }
        }
        return count;
    }

    public static boolean hasBandRelic() {
        for (AbstractRelic relic : DungeonHelper.getPlayer().relics) {
            if (relic.relicId.equals(Band_AVEMUJICA.ID) || relic.relicId.equals(Band_CRYCHIC.ID) || relic.relicId.equals(Combination_ANSK.ID) || relic.relicId.equals(Combination_TMSK.ID) || relic.relicId.equals(Combination_UKSK.ID))
                return true;
        }
        return false;
    }

    public static Map<String, Integer> initSelectedMembers(int count) {
        Map<String, Integer> tmp = new HashMap<>();
        do {
            int i = AbstractDungeon.miscRng.random(0, SakikoConst.BAND_MEMBER_LIST.size() - 1);
            String s = SakikoConst.BAND_MEMBER_LIST.get(i);
            if (!DungeonHelper.getPlayer().hasRelic(s.replace("Monster", "")))
                tmp.put(s, i);
        } while (tmp.size() < count);
        return tmp;
    }
}
