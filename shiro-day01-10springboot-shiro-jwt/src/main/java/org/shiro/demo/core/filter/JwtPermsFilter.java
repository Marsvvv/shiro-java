package org.shiro.demo.core.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.shiro.demo.constant.ShiroConstant;
import org.shiro.demo.web.BaseResponse;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 自定义jwt资源过滤器
 */
public class JwtPermsFilter extends PermissionsAuthorizationFilter {

    /**
     * 拒绝访问时调用
     *
     * @param request  request
     * @param response response
     * @return boolean
     * @throws Exception Exception
     */
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        //  从请求头中获得jwtToken
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwtToken = httpServletRequest.getHeader("jwtToken");
        //  判断jwtToken是否不为空，走jwt调用方式
        if (StrUtil.isNotEmpty(jwtToken)) {
            BaseResponse baseResponse = new BaseResponse(ShiroConstant.NO_AUTH_CODE, ShiroConstant.NO_AUTH_MESSAGE);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSONObject.toJSONString(baseResponse));
        }
        //  如果为空，原始方式调用
        return super.onAccessDenied(request, response);
    }
}
