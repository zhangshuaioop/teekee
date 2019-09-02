package com.example.identification.utils;

import com.example.identification.entity.JwtUser;
import com.example.identification.entity.sys.SysUsersAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by zhangshuai on 2019/1/31
 */
public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String SECRET = "jwtsecretdemo";
    private static final String ISS = "echisan";

    // 角色的key
    private static final String ROLE_CLAIMS = "rol";

    // 过期时间是3600秒，既是1个小时
    private static final long EXPIRATION = 604800L;

    // 选择了记住我之后的过期时间为7天
    private static final long EXPIRATION_REMEMBER = 604800L;

    // 创建token
    public static String createToken(String username, String role, JwtUser jwtUser, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        map.put("id", jwtUser.getId());
        map.put("account", jwtUser.getUsername());
        map.put("nickName", jwtUser.getNickName());
        map.put("realName", jwtUser.getRealName());
        map.put("mobile", jwtUser.getMobile());
        map.put("headPortraitUrl", jwtUser.getHeadPortraitUrl());
        map.put("email", jwtUser.getEmail());
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    // 从token中获取用户名
    public static String getUsername(String token){

        String userName = "";
        Claims claims = getTokenBody(token);
        if(claims != null){
            userName = claims.getSubject();
        }
        return userName;
    }

    // 获取用户角色
    public static String getUserRole(String token){

        String userName = "";
        Claims claims = getTokenBody(token);
        if(claims != null){
            userName = (String)claims.get(ROLE_CLAIMS);
        }
        return userName;
    }


    // 获取用户信息
    public static SysUsersAccount getUser(String token){

        SysUsersAccount sysUsersAccount = null;
        Claims claims = getTokenBody(token);
        if(claims != null){
            sysUsersAccount = new SysUsersAccount();
            Integer id = (Integer) claims.get("id");
            sysUsersAccount.setId(id);
            sysUsersAccount.setAccount((String)claims.get("account"));
            sysUsersAccount.setNickName((String) claims.get("nickName"));
            sysUsersAccount.setRealName((String) claims.get("realName"));
            sysUsersAccount.setMobile((String) claims.get("mobile"));
            sysUsersAccount.setHeadPortraitUrl((String) claims.get("headPortraitUrl"));
            sysUsersAccount.setEmail((String) claims.get("email"));
        }
        return sysUsersAccount;
    }

    // 是否已过期
    public static boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private static Claims getTokenBody(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){

        }
        return claims;
    }
}
