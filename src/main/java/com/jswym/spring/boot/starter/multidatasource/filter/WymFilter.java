package com.jswym.spring.boot.starter.multidatasource.filter;

import com.jswym.spring.boot.starter.multidatasource.DynamicIdSelector;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(WymFilter.class);


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

        String requestParameter = request.getParameter("orgcode");
        log.info("requestParameter: {}", requestParameter);
        Cookie cookie = WebUtils.getCookie(request, "orgcode");
        log.info("cookie: {}", cookie.toString());
        String header = request.getHeader("orgcode");
        log.info("header: {}", header);
        if (StringUtils.isNotBlank(requestParameter)) {
            orgcode = requestParameter;
        } else if (cookie != null && StringUtils.isNotBlank(cookie.toString())) {
            orgcode = cookie.toString();
        }else if (StringUtils.isNotBlank(header)) {
            orgcode = header;
        }else {
            orgcode = "123";
        }
        return orgcode;
    }
}
