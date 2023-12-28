package com.ginDriver.common.verifyCode.service.impl;


import com.ginDriver.common.verifyCode.service.IGenerateImageService;
import com.ginDriver.common.verifyCode.utils.VerifyCodeUtil;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 生成验证图片
 *
 * @author cuberxp
 * @since 1.0.0
 * Create time 2019/12/20 17:17
 */
@Service
public class GenerateImageServiceImpl implements IGenerateImageService {
    private final VerifyCodeUtil verifyCodeUtil;

    public GenerateImageServiceImpl(VerifyCodeUtil verifyCodeUtil) {
        this.verifyCodeUtil = verifyCodeUtil;
    }

    /**
     * 根据验证码生成图片
     *
     * @param verifyCode 验证码
     */
    @Override
    public Image imageWithDisturb(String verifyCode) {
        return imageWithDisturb(verifyCode, verifyCodeUtil.getWidth(), verifyCodeUtil.getHeight());
    }

    private Image imageWithDisturb(String verifyCode, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        // 填充背景
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 画干扰线
        g2d.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        verifyCodeUtil.drawBesselLine(1, g2d);

        FontMetrics fontMetrics = g2d.getFontMetrics();
        // 每一个字符所占的宽度
        int fW = width / verifyCode.length();
        // 字符的左右边距
        int fSp = (fW - (int) fontMetrics.getStringBounds("W", g2d).getWidth()) / 2;
        for (int i = 0; i < verifyCode.length(); i++) {
            g2d.setColor(verifyCodeUtil.color());
            // 文字的纵坐标
            int fY = height - ((height - (int) fontMetrics.getStringBounds(String.valueOf(verifyCode.charAt(i)), g2d).getHeight()) >> 1);
            g2d.drawString(String.valueOf(verifyCode.charAt(i)), i * fW + fSp + 3, fY - 3);
        }
        g2d.dispose();
        return bi;
    }
}
