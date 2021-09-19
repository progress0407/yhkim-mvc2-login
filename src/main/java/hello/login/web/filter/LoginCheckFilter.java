package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class LoginCheckFilter implements Filter {

    public static final String[] whiteList = {"/", "/members/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작 {}", requestURI);
            
            // whiteList 이외의 곳에 진입하고자 할 시
            if(isLoginPathCheck(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    // 로그인으로 redirect
                    // 요청자가 가고자 했던 곳으로 리다이렉트하겠다
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return; // 이후 필터, 서블릿 호출 x
                }
            }
            chain.doFilter(request, response);

        } catch (Exception e) {
            throw e; // 예외 로깅 가능 하지만, 톰캣까지 예외를 보내주어야 함
        } finally { // return 문 있더라도 항상 호출
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크 X
     */

    private boolean isLoginPathCheck(String requestURI) {
        // 내 코드
//        return Arrays.stream(whiteList).anyMatch(e -> e.equals(requestURI));
        /**
         * whiteList에 속하는 것이 true
         * 로그인이 안되는 것 false
         */
//        return !PatternMatchUtils.simpleMatch(whiteList, requestURI); // 강의 영상
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}
