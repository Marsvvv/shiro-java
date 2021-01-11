package org.shiro.demo.core.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

/**
 * 自定义session管理器
 *
 * @author Tobu
 */
@Getter
@Setter
public class ShiroWebSessionManager extends DefaultWebSessionManager {

    /**
     * 从请求中获得sessionId的key
     */
    private static final String AUTHORIZATION = "jwtToken";

    /**
     * 自定义注入的资源类型名称
     */
    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    private JwtTokenManager jwtTokenManager;

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //  获取请求头是否存在jwtToken
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwtToken = httpServletRequest.getHeader(AUTHORIZATION);

        //  判断jwtToken是否为空，为空则使用cookie方式
        if (StrUtil.isEmpty(jwtToken)) {
            return super.getSessionId(request, response);
        }

        //  jwtToken不为空，所以获取
        DecodedJWT decode = jwtTokenManager.decode(jwtToken);
        String jti = decode.getId();
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                ShiroHttpServletRequest.URL_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, jti);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, isSessionIdUrlRewritingEnabled());
        return jti;
    }
}
