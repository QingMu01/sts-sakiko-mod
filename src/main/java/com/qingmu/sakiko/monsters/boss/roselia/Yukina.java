package com.qingmu.sakiko.monsters.boss.roselia;

import com.qingmu.sakiko.monsters.AbstractSakikoMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;

import java.util.Collections;
import java.util.List;

/*
 * 火鸟，播放bgm，每个高潮节点触发特殊效果，播放结束时对玩家造成99999点生命流失，转阶段后强制取消
 * neo-aspect,播放bgm
 * */
public class Yukina extends AbstractSakikoMonster {

    public Yukina(String name, String id, String img, float x, float y) {
        super(name, id, img, x, y);
    }

    @Override
    protected List<IntentAction> initIntent() {
        return Collections.emptyList();
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        return Collections.emptyList();
    }

    @Override
    protected boolean canPhaseSwitch() {
        return false;
    }

    @Override
    protected void phaseSwitchLogic() {

    }

    @Override
    protected List<IntentAction> updateIntent() {
        return Collections.emptyList();
    }
}
