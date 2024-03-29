package com.ginDriver.main.controller;


import com.ginDriver.common.verifyCode.entity.ImgTypeConstant;
import com.ginDriver.common.verifyCode.entity.VerifyVO;
import com.ginDriver.common.verifyCode.service.IVerifyCodeService;
import com.ginDriver.core.domain.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;


/**
 * @author DueGin
 */
@Api(tags = "验证码")
@RestController
@RequestMapping("/verifyImage")
public class VerificationCodeController {


    @Resource
    private IVerifyCodeService iVerifyCodeService;


    /**
     * 获取验证码图片,
     * 验证码存储在redis中,key为uuid
     */
    @ApiOperation("获取验证码")
    @GetMapping("")
    public ResultVO<VerifyVO> getVerifyImage() throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            String verifyUuid = UUID.randomUUID().toString().replaceAll("-", "");

            Image image = iVerifyCodeService.getVerifyCodeImage(verifyUuid);

            ImageIO.write((RenderedImage) image, ImgTypeConstant.PNG.getSuffix(), byteArrayOutputStream);

            Base64 base64 = new Base64();
            String verifyBase64 = base64.encodeToString(byteArrayOutputStream.toByteArray());

            verifyBase64 = "data:image/png;base64," + verifyBase64;

            return ResultVO.ok(new VerifyVO()
                    .setImg(verifyBase64)
                    .setUuid(verifyUuid)
            );
        }
    }

}
