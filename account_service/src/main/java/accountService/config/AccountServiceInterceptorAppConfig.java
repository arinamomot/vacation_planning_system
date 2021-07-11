package accountService.config;

import accountService.interceptor.AccountServiceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AccountServiceInterceptorAppConfig implements WebMvcConfigurer {

    private AccountServiceInterceptor interceptor;

    @Autowired
    public AccountServiceInterceptorAppConfig(AccountServiceInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
