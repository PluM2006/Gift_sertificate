package ru.clevertec.ecl.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.clevertec.ecl.interceptors.ClusterPaginatorInterceptor;
import ru.clevertec.ecl.interceptors.ClusterInterceptor;
import ru.clevertec.ecl.interceptors.CommonInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfigurer implements WebMvcConfigurer {

  private final CommonInterceptor commonInterceptor;
  private final ClusterInterceptor clusterInterceptor;
  private final ClusterPaginatorInterceptor clusterPaginatorInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(clusterInterceptor)
        .order(Ordered.HIGHEST_PRECEDENCE)
        .addPathPatterns("/**/orders*/**");
    registry.addInterceptor(clusterPaginatorInterceptor)
        .order(Ordered.LOWEST_PRECEDENCE)
        .addPathPatterns("/**/orders*/**")
        .excludePathPatterns("/**/orders/sequence/**");
    registry.addInterceptor(commonInterceptor).addPathPatterns("/**/certificates*/**", "/**/tags*/**", "/**/users*/**");

  }
}
