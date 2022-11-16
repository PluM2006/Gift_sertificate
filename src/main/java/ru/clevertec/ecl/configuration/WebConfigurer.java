package ru.clevertec.ecl.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.clevertec.ecl.interceptors.ClusterInterceptor;
import ru.clevertec.ecl.interceptors.CommonInterceptor;
import ru.clevertec.ecl.interceptors.ReplicaInterceptor;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class WebConfigurer implements WebMvcConfigurer {

  private final CommonInterceptor commonInterceptor;
  private final ClusterInterceptor clusterInterceptor;
  private final ReplicaInterceptor replicaInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(clusterInterceptor)
        .addPathPatterns("/**/orders*/**")
        .excludePathPatterns("/**/orders/sequence/**");
    registry.addInterceptor(commonInterceptor)
        .addPathPatterns("/**/certificates*/**", "/**/tags*/**", "/**/users*/**");
    registry.addInterceptor(replicaInterceptor)
        .excludePathPatterns("/**/orders/sequence/**");
  }
}
