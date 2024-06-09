package lu.cnw.ranking.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class FrontendConfig implements WebMvcConfigurer {
    protected static final String[] RESOURCE_LOCATIONS = new String[]{
            "classpath:/frontend/",
            //"classpath:/frontend/content/",
            //"classpath:/frontend/i18n/",
            //"classpath:/frontend/assets/",
    };
    protected static final String[] RESOURCE_PATHS = new String[]{
            "/*.js",
            "/*.css",
            //"/*.svg",
            //"/*.png",
            "*.ico",
            //"/content/**",
            //"/assets/**",
            //"/i18n/*",
    };
    long cacheDuration=0;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseApiPath = "/api/v1";
        CacheControl cacheControl = CacheControl.maxAge(cacheDuration, TimeUnit.DAYS).cachePublic();
        registry.addResourceHandler(RESOURCE_PATHS)
                .addResourceLocations(RESOURCE_LOCATIONS)
                .setCacheControl(cacheControl);
        registry.addResourceHandler("/", "/**")
                .setCachePeriod(0)
                .addResourceLocations("classpath:/frontend/index.html")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        if (resourcePath.startsWith(baseApiPath) || resourcePath.startsWith(baseApiPath.substring(1))) {
                            return null;
                        }
                        return location.exists() && location.isReadable() ? location : null;
                    }
                });
        logger.info("Static resource handler initialized");
    }
}
