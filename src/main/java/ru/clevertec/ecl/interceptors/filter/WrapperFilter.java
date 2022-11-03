package ru.clevertec.ecl.interceptors.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ru.clevertec.ecl.utils.cache.CachedBodyHttpServletRequest;

@Component
@WebFilter(urlPatterns = {"/*"})
public class WrapperFilter extends GenericFilterBean {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    CachedBodyHttpServletRequest cachedBodyHttpServletRequest =
        new CachedBodyHttpServletRequest((HttpServletRequest) request);
    chain.doFilter(cachedBodyHttpServletRequest, response);
  }
}
