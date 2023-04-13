package com.chen.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**尝试
 * @Author @Chenxc
 * @Date 2023/3/28 10:36
 */
@Component
public class SetSystemNameFilter implements Filter {
    @Value("${system.index}")
    private String index;

    @Value("${system.name}")
    private String systemName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setAttribute("systemName",systemName);
        request.setAttribute("index",index);
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
