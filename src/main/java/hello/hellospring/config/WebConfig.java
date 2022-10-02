package hello.hellospring.config;

import java.util.List;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer, TomcatConnectorCustomizer {
    /**
     * http request body를 통해서 데이터를 전달 받을 수 있도록 세팅 추가
     *
     * @param connector
     */
    @Override
    public void customize(org.apache.catalina.connector.Connector connector) {
        connector.setParseBodyMethods("POST,PUT,DELETE");
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc/v1/**").addResourceLocations("classpath:/doc/v1/");
        registry.addResourceHandler("/doc/v1/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 특정 자료형의 경우 JSON serialize가 되지 않아서 세팅 추가
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        WebMvcConfigurer.super.configureMessageConverters(converters);
        converters.add(0, new MappingJackson2HttpMessageConverter());
    }

    /**
     * CORS 회피 대상 세팅
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // TODO: proxy 서버 구현 필요
        registry.addMapping("/**")
                .allowedMethods("GET", "DELETE", "PUT", "PATCH", "POST")
                .allowedOrigins("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
