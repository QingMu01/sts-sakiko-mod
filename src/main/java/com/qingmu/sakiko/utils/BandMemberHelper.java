package com.qingmu.sakiko.utils;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.monsters.*;
import com.qingmu.sakiko.relics.menbers.AbstractBandMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BandMemberHelper {

    public static List<String> memberList = new ArrayList<>();

    static {
        memberList.add(UikaMonster.ID);
        memberList.add(MutsumiMonster.ID);
        memberList.add(UmiriMonster.ID);
        memberList.add(NyamuchiMonster.ID);
        memberList.add(TomoriMonster.ID);
        memberList.add(AnonMonster.ID);
        memberList.add(SoyoMonster.ID);
        memberList.add(TakiMonster.ID);
        memberList.add(RanaMonster.ID);
    }

    public static int getBandMemberCount() {
        AbstractPlayer p = AbstractDungeon.player;
        int count = 0;
        for (AbstractRelic relic : p.relics) {
            if (relic instanceof AbstractBandMember) {
                count++;
            }
        }
        return count;
    }

    public static Map<String, Integer> getMember(int count) {
        Map<String, Integer> tmp = new HashMap<>();
            do {
                int i = AbstractDungeon.monsterRng.random(0, memberList.size() - 1);
                String s = memberList.get(i);
                if (!AbstractDungeon.player.hasRelic(s.replace("Monster","")))
                    tmp.put(s,i);
            } while (tmp.size() < count);
        return tmp;
    }
}
