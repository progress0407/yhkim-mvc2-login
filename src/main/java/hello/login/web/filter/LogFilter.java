package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
//@Component
public class LogFilter implements Filter {



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
//        Arrays.stream(cookies).forEach(e -> System.out.printf("name = %s, value = %s \n", e.getName(), e.getValue()));
        Cookie jsessionid =
                Arrays.stream(cookies)
                .filter(e -> e.getName().equals("JSESSIONID"))
                .findAny()
                .orElse(null);


        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            log.info("REQUEST COOKIE(JSESSIONID) [{}]", jsessionid.getValue());
            chain.doFilter(request, response); // 필터 있으면 다음 핉터 없으면, 없으면 서블릿 호출

        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
