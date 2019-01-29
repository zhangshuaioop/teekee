package com.teekee.blueoceanservice.entity.syscompany;

import com.teekee.blueoceanservice.entity.sysconsole.SysConsoleUsers;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by echisan on 2018/6/23
 */
public class JwtUser implements UserDetails {

    private Integer id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    @ApiModelProperty(value = "头像的图片地址", example = "1", required = true)
    private String headPortraitUrl;


    @ApiModelProperty(value = "是否删除", example = "1", required = true)
    private Boolean flagDeleted;

    @ApiModelProperty(value = "账号类型：PLATFORM;VENDOR;DEMAND", example = "1", required = true)
    private String actType;

    @ApiModelProperty(value = "0关闭1打开", example = "1", required = true)
    private Boolean flagOpenStatus;

    @ApiModelProperty(value = "邮箱", example = "1", required = true)
    private String email;

    @ApiModelProperty(value = "是否有效1有效0无效", example = "1", required = true)
    private Boolean flagAvailable;

    @ApiModelProperty(value = "昵称", example = "1", required = true)
    private String nickname;

    public JwtUser() {
    }

    // 写一个能直接使用user创建jwtUser的构造器
    public JwtUser(SysConsoleUsers sysConsoleUsers) {
        id = sysConsoleUsers.getId();
        username = sysConsoleUsers.getUsername();
        password = sysConsoleUsers.getPassword();
        headPortraitUrl = sysConsoleUsers.getHeadPortraitUrl();
        flagDeleted = sysConsoleUsers.getFlagDeleted();
        actType = sysConsoleUsers.getActType();
        flagOpenStatus = sysConsoleUsers.getFlagOpenStatus();
        email = sysConsoleUsers.getEmail();
        nickname = sysConsoleUsers.getNickname();
        flagAvailable = sysConsoleUsers.getFlagAvailable();
        authorities = sysConsoleUsers.getAuthorities();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getHeadPortraitUrl() {
        return headPortraitUrl;
    }

    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl;
    }

    public Boolean getFlagDeleted() {
        return flagDeleted;
    }

    public void setFlagDeleted(Boolean flagDeleted) {
        this.flagDeleted = flagDeleted;
    }

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public Boolean getFlagOpenStatus() {
        return flagOpenStatus;
    }

    public void setFlagOpenStatus(Boolean flagOpenStatus) {
        this.flagOpenStatus = flagOpenStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getFlagAvailable() {
        return flagAvailable;
    }

    public void setFlagAvailable(Boolean flagAvailable) {
        this.flagAvailable = flagAvailable;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "JwtUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                ", headPortraitUrl='" + headPortraitUrl + '\'' +
                ", flagDeleted=" + flagDeleted +
                ", actType='" + actType + '\'' +
                ", flagOpenStatus=" + flagOpenStatus +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", flagAvailable=" + flagAvailable +
                '}';
    }
}
