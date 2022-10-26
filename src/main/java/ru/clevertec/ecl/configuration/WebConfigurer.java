package ru.clevertec.ecl.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.clevertec.ecl.interceptors.ClusterGetAllInterceptor;
import ru.clevertec.ecl.interceptors.ClusterInterceptor;
import ru.clevertec.ecl.interceptors.CommonInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfigurer implements WebMvcConfigurer {

  private final CommonInterceptor commonInterceptor;
  private final ClusterInterceptor clusterInterceptor;
  private final ClusterGetAllInterceptor clusterGetAllInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(clusterInterceptor).addPathPatterns("/**/orders*/**").order(1);
    registry.addInterceptor(clusterGetAllInterceptor).addPathPatterns("/**/orders*/**").order(2);
    registry.addInterceptor(commonInterceptor).addPathPatterns("/**/certificates*/**", "/**/tags*/**", "/**/users*/**");

  }
}
