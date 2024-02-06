package com.ginDriver.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author DueGin
 */
public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String SECRET = "jwtsecretdemo";
    private static final String ISS = "echisan";

    /**
     * 过期时间是3600秒，既是1个小时
     */
    private static final long EXPIRATION = 3600L;

    /**
     * 选择了记住我之后的过期时间为7天
     */
    public static final long EXPIRATION_REMEMBER = 604800L;


    /**
     * 添加角色的key
     */
    private static final String ROLE_LIST_CLAIMS = "role_list";

    private static final String SYS_ROLE_CLAIMS = "sys_role";

    private static final String ROLE_MAP_CLAIMS = "role_map";
    /**
     * 权限key
     */
    private static final String PERM_CLAIMS = "PERM";

    /**
     * 创建token
     * TODO 这里是存在问题，我将权限信封装到jwt中，实际应该存储到redis中。主要是安全问题
     *
     * @param userId  登录用户名和ID，格式：userAccount_userId
     * @param roleList     角色集合
     * @param roleMap      角色KV集合
     * @param isRememberMe 是否记住我
     * @return token
     */
    public static String createToken(String userId, List<String> roleList, Map<String, String> roleMap, boolean isRememberMe) {
        String token = null;
        try {
            long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
            HashMap<String, Object> map = new HashMap<>();
            map.put(ROLE_LIST_CLAIMS, roleList);
            map.put(ROLE_MAP_CLAIMS, roleMap);
            token = Jwts.builder()
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    // 额外信息，这里要早set一点，放到后面会覆盖别的字段
                    .setClaims(map)
                    .setIssuer(ISS)
                    .setSubject(userId)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                    .compact();
        } catch (ExpiredJwtException e) {
            e.getClaims();
        }
        return token;
    }

    public static String getToken(String tokenHeader) {
        return tokenHeader.replace(JwtTokenUtils.TOKEN_PREFIX, "").trim();
    }

    /**
     * 从token中获取登录用户名
     */
    public static String getUserId(String token) {
        return getTokenBody(token).getSubject();
    }

    /**
     * 从token中获取roles
     */
    public static List<String> getRoleList(String token) {
        return (List<String>) getTokenBody(token).get(ROLE_LIST_CLAIMS);
    }

    public static Map<String, String> getRoleMap(String token) {
        return (Map<String, String>) getTokenBody(token).get(ROLE_MAP_CLAIMS);
    }

    public static String getSysRole(String token) {
        Map<String, String> map = (Map<String, String>) getTokenBody(token).get(ROLE_MAP_CLAIMS);
        return map.get("sys");
    }

    /**
     * 从token中获取roles
     */
    public static Map<String, String> getGroupRoleMap(String token) {
        return (Map<String, String>) getTokenBody(token).get(ROLE_MAP_CLAIMS);
    }

    /**
     * 是否已过期
     *
     * @param token
     * @return
     */
    public static boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }


    private static Claims getTokenBody(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }


}
