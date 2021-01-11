package org.shiro.demo.core.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.shiro.demo.constant.ShiroConstant;
import org.shiro.demo.web.BaseResponse;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 自定义jwt角色校验过滤器
 *
 * @author Tobu
 */
public class JwtRolesFilter extends RolesAuthorizationFilter {

    /**
     * 拒绝访问时调用
     *
     * @param request  request
     * @param response response
     * @return boolean
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
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
}
