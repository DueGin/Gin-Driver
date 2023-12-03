package duegin.ginDriver.common.utils;

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
    private static final long EXPIRATION_REMEMBER = 604800L;


    /**
     * 添加角色的key
     */
    private static final String ROLE_CLAIMS = "Role";

    private static final String ROLE_GROUP_CLAIMS = "Role_Group";
    /**
     * 权限key
     */
    private static final String PERM_CLAIMS = "PERM";

    /**
     * 创建token
     * TODO 这里是存在问题，我将权限信封装到jwt中，实际应该存储到redis中。主要是安全问题
     *
     * @param username 用户名
     * @param roles 系统角色
     * @param isRememberMe 是否记住我
     * @return token
     */
    public static String createToken(String username, List<String> roles, Map<String, String> groupRoleMap, boolean isRememberMe) {
        String token = null;
        try {
            long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
            HashMap<String, Object> map = new HashMap<>();
            map.put(ROLE_CLAIMS, roles);
            map.put(ROLE_GROUP_CLAIMS, groupRoleMap);
            token = Jwts.builder()
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    // 额外信息，这里要早set一点，放到后面会覆盖别的字段
                    .setClaims(map)
                    .setIssuer(ISS)
                    .setSubject(username)
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
     * 从token中获取用户名
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    /**
     * 从token中获取roles
     */
    public static List<String> getRoles(String token) {
        return (List<String>) getTokenBody(token).get(ROLE_CLAIMS);
    }

    /**
     * 从token中获取roles
     */
    public static Map<String, String> getGroupRoles(String token) {
        return (Map<String, String>) getTokenBody(token).get(ROLE_GROUP_CLAIMS);
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
