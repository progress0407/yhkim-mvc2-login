package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class SwchoMDCFilter implements Filter {

    private static final String mdcRequestId = "mdcRequestId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("SwchoMDCFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("SwchoMDCFilter doFilter");
        // MDC 처리
        String mdcUuid = UUID.randomUUID().toString();
        MDC.put(mdcRequestId, mdcUuid);
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);
        chain.doFilter(request, responseWrapper);
        responseWrapper.setHeader(mdcRequestId, mdcUuid);
        responseWrapper.copyBodyToResponse();
        log.info("Response header is set with uuid {}", responseWrapper.getHeader(mdcUuid));
    }

    @Override
    public void destroy() {
        log.info("SwchoMDCFilter destroy");
    }
}
