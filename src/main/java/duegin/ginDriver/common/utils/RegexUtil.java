package duegin.ginDriver.common.utils;


/**
 * @author DueGin
 */
public class RegexUtil {

    /**
     * 校验手机号
     * @param phone 手机号
     * @return {@code true}：通过校验；{@code false}：不通过
     */
    public static Boolean checkPhone(String phone){
        String reg = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
        return phone.matches(reg);
    }

    /**
     * 校验邮箱
     * @param email 邮箱
     * @return {@code true}：通过校验；{@code false}：不通过
     */
    public static Boolean checkEmail(String email){
        String reg = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        return email.matches(reg);
    }

    /**
     * 校验用户名
     * @param username 用户名
     * @return {@code true}：通过校验；{@code false}：不通过
     */
    public static Boolean checkUsername(String username){
        String reg = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
        return username.matches(reg);
    }

    /**
     * 校验密码
     * @param passwd 密码
     * @return {@code true}：通过校验；{@code false}：不通过
     */
    public static Boolean checkPassword(String passwd){
        String reg = "^[a-zA-Z]\\w{6,17}$";
        return passwd.matches(reg);
    }

}