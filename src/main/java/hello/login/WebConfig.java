package hello.login;

import ch.qos.logback.classic.turbo.MDCFilter;
import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.filter.SwchoMDCFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
// 필터만 등록하는 거면 WebMvcConfigurer 구현하지 않아도 된다

//    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1); // 필터 여러개이면 1, 2, 3 ..
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

//    @Bean
    public FilterRegistrationBean MDCFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new SwchoMDCFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    /**
     * 요런걸로 필터 하나 더 호출 시키는 것으로.. 성능이 걱정된다는 것은
     * 마치 바다에서 모래 알갱이 하나 없어지는 것을 걱정하는 것과 같다...
     * 실제로 app 실행하면 컨트롤러 메서드 하나당 수십개씩 호출됨..
     * 실제로는 DB와 네트워크에서 성능이 다 깎인다
     */
//    @Bean
    public FilterRegistrationBean LoginCheckFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**") // 하위 전부 다
                .excludePathPatterns("/css/**", "/*.ico", "/error"); // 그 중에서 이부분 만큼은 interceptor 호출 안하겠다

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error"); // 인증 체크하지 않는 것들
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
