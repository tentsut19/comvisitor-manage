package th.co.infinitait.comvisitor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import th.co.infinitait.comvisitor.component.CabsatPayloadInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private CabsatPayloadInterceptor cabsatPayloadInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(cabsatPayloadInterceptor);
    }
}
