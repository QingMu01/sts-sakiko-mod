package com.qingmu.sakiko.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.FontHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FontBitmapHelper {

    private static final int FONT_SIZE_128 = 84;
    private static final int TEXTURE_SIZE_128 = 128;

    private static final int FONT_SIZE_48 = 32;
    private static final int TEXTURE_SIZE_48 = 48;


    public static Texture getFontBitmap(char character, Size size) {
        BufferedImage image = new BufferedImage(size.getTextureSize(), size.getTextureSize(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        // 设置抗锯齿属性
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // 设置背景透明
        g2d.setBackground(new Color(0, 0, 0, 0));
        g2d.clearRect(0, 0, size.getTextureSize(), size.getTextureSize());
        g2d.setFont(new Font(FontHelper.charTitleFont.getData().getFontFile().path(), Font.BOLD, size.getFontSize()));
        // 获取字符的宽度和高度
        FontMetrics fm = g2d.getFontMetrics();
        int charWidth = fm.charWidth(character);
        int charHeight = fm.getHeight();
        // 计算字符绘制的位置，使其居中
        int x = (size.getTextureSize() - charWidth) / 2;
        int y = ((size.getTextureSize() - charHeight) / 2) + fm.getAscent();
        // 绘制字符
        g2d.setColor(Color.WHITE); // 设置字符颜色
        g2d.drawString(String.valueOf(character), x, y);
        // 释放资源
        g2d.dispose();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            Pixmap pixmap = new Pixmap(imageInByte, 0, imageInByte.length);
            return new Texture(pixmap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public enum Size {
        SMALL(TEXTURE_SIZE_48, FONT_SIZE_48),
        LARGE(TEXTURE_SIZE_128, FONT_SIZE_128);
        private final int textureSize;
        private final int fontSize;

        Size(int textureSize, int fontSize) {
            this.textureSize = textureSize;
            this.fontSize = fontSize;
        }

        public int getTextureSize() {
            return textureSize;
        }

        public int getFontSize() {
            return fontSize;
        }
    }

}
