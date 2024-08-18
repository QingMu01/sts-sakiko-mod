package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.ui.panels.AbstractPanel;
import com.qingmu.sakiko.ui.MusicDrawPilePanel;

@SpirePatch(clz = OverlayMenu.class, method = SpirePatch.CLASS)
public class MusicDrawPilePanelFiledPatch {
    /*
    * 添加自定义的音乐抽牌堆UI
    * */
    public static SpireField<AbstractPanel> musicDrawPile = new SpireField<>(MusicDrawPilePanel::new);
}
