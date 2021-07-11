package holidayPlannerNotificationService.config;

import holidayPlannerNotificationService.interceptor.NotificationServiceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class NotificationServiceInterceptorAppConfig implements WebMvcConfigurer {

    private NotificationServiceInterceptor interceptor;

    @Autowired
    public NotificationServiceInterceptorAppConfig(NotificationServiceInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
