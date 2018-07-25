package com.jswym.spring.boot.starter.multidatasource.filter;

import com.jswym.spring.boot.starter.multidatasource.DynamicIdSelector;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author lxr
 * @create 2018-06-12 20:48
 **/
public class WymFilter extends OncePerRequestFilter {


    private final DynamicIdSelector dynamicIdSelector;

    public WymFilter(DynamicIdSelector dynamicIdSelector) {
        this.dynamicIdSelector = dynamicIdSelector;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String orgCode = obtainParameter(request);
        try {
            dynamicIdSelector.setOrgCode(Optional.ofNullable(orgCode)
                .orElseGet(() -> getOrgcode().orElse(null)));
            filterChain.doFilter(request, response);
        }
        finally {
            dynamicIdSelector.remove();
        }
    }

    private   Optional<String> getOrgcode(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof UserDetails) {
                        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                        String[] split = springSecurityUser.getUsername().split(":");
                        if(split.length>1){
                            return split[1];
                        }
                        return null;
                    } else if (authentication.getPrincipal() instanceof String) {
                        String[] split = ((String) authentication.getPrincipal()).split(":");
                        if(split.length>1){
                            return split[1];
                        }
                        return null;
                    }
                    return null;
                });
    }

    private String obtainParameter(HttpServletRequest request) {

       String orgcode = "";

        // String requestParameter = request.getParameter("orgcode");
        // Cookie cookie = WebUtils.getCookie(request, "orgcode");
        // String header = request.getHeader("orgcode");
        // if (StringUtils.isNotBlank(requestParameter)) {
        //     orgcode = requestParameter;
        // } else if (cookie != null && StringUtils.isNotBlank(cookie.toString())) {
        //     orgcode = cookie.toString();
        // }
        // if (StringUtils.isNotBlank(header)) {
        //     orgcode = header;
        // }
        orgcode = "testorg";
        return orgcode;
    }
}
