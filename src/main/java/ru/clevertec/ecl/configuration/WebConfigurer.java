package ru.clevertec.ecl.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.clevertec.ecl.interceptors.ClusterInterceptor;
import ru.clevertec.ecl.interceptors.CommonInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfigurer implements WebMvcConfigurer {

  private final CommonInterceptor commonInterceptor;
  private final ClusterInterceptor clusterInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(clusterInterceptor).addPathPatterns("/**/orders*/**");
    registry.addInterceptor(commonInterceptor).addPathPatterns("/**/certificates*/**");

  }
}
