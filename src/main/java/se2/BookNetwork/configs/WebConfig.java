package se2.BookNetwork.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        System.out.println("Registering resource handler for /uploads/**");
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}