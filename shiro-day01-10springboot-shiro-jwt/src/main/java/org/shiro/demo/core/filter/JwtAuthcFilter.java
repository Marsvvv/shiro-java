package org.shiro.demo.core.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.shiro.demo.constant.ShiroConstant;
import org.shiro.demo.core.impl.JwtTokenManager;
import org.shiro.demo.web.BaseResponse;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 自定义 登录 jwt过滤器
 *
 * @author Tobu
 */
public class JwtAuthcFilter extends FormAuthenticationFilter {

    private JwtTokenManager jwtTokenManager;

    public JwtAuthcFilter(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    /**
     * 拒绝访问时调用
     *
     * @param request  request
     * @param response response
     * @return boolean
     * @throws Exception Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //  从请求头中获得jwtToken
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwtToken = httpServletRequest.getHeader("jwtToken");
        //  判断jwtToken是否不为空，走jwt调用方式
        if (StrUtil.isNotEmpty(jwtToken)) {
            BaseResponse baseResponse = new BaseResponse(ShiroConstant.NO_LOGIN_CODE, ShiroConstant.NO_LOGIN_MESSAGE);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSONObject.toJSONString(baseResponse));
        }
        //  如果为空，原始方式调用
        return super.onAccessDenied(request, response);
    }

    /**
     * 是否拒绝访问
     *
     * @param request     request
     * @param response    response
     * @param mappedValue mappedValue
     * @return boolean
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //  从请求头中获得jwtToken
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwtToken = httpServletRequest.getHeader("jwtToken");
        //  判断jwtToken是否不为空，走jwt校验
        if (StrUtil.isNotEmpty(jwtToken)) {
            Boolean verify = jwtTokenManager.verify(jwtToken);
            if (verify) {
                //  即使校验成功，也需要调用父类的方法
                return super.isAccessAllowed(request, response, mappedValue);
            } else {
                return false;
            }
        }
        //  为空走原始校验
        return super.isAccessAllowed(request, response, mappedValue);
    }
}
