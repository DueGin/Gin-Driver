package com.ginDriver.common.verifyCode.service.impl;


import com.ginDriver.common.verifyCode.service.IGenerateImageService;
import com.ginDriver.common.verifyCode.service.IVerifyCodeService;
import com.ginDriver.common.verifyCode.utils.VerifyCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * 验证码服务
 */
@Slf4j
@Service
public class VerifyCodeServiceImpl implements IVerifyCodeService {
    private final static String VERIFY_CODE = "VERIFY_CODE";

    private final VerifyCodeUtil verifyCodeUtil;
    private final IGenerateImageService iGenerateImageService;
    private final IMemberVerifyCodeRepository iMemberVerifyCodeRepository;

    public VerifyCodeServiceImpl(VerifyCodeUtil verifyCodeUtil,
                                 IGenerateImageService iGenerateImageService,
                                 IMemberVerifyCodeRepository iMemberVerifyCodeRepository) {
        this.verifyCodeUtil = verifyCodeUtil;
        this.iGenerateImageService = iGenerateImageService;
        this.iMemberVerifyCodeRepository = iMemberVerifyCodeRepository;
    }

    @Override
    public void verify(String key, String code) throws LoginException {
        String memberVerifyCode = iMemberVerifyCodeRepository.get(key);

        if (!Objects.equals(code, memberVerifyCode)) {
            throw new LoginException("验证码错误");
        }
    }

    @Override
    public Image getVerifyCodeImage(String key) {
        if (iMemberVerifyCodeRepository.hasKey(key)) {
            this.deleteVerifyCode(key);
        }

        //获取验证码
        String verifyCode = randomVerifyString(verifyCodeUtil.getLen());
        log.info("uuid: {}, 验证码：{}", key, verifyCode);

        //得到生成的验证码
        Image image = iGenerateImageService.imageWithDisturb(verifyCode);

        //写入内存，方便验证
        iMemberVerifyCodeRepository.save(key, verifyCode);
        return image;
    }

    private String randomVerifyString(int len) {
        StringBuilder verifyString = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < len; i++) {
            verifyString.append(secureRandom.nextInt(10));
        }

        return verifyString.toString();
    }

    @Override
    public void deleteVerifyCode(String key) {
        iMemberVerifyCodeRepository.delete(key);
    }
}
