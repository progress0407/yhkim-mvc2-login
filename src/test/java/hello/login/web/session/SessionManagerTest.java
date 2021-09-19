package hello.login.web.session;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    public void sessionTest() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();
        sessionManager.createSession(member, response);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();
    }

    @Test
    public void Http세션_테스트() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = request.getSession(false); // true와 동일, 세션 없으면 생성
        session.setAttribute(SessionConst.LOGIN_MEMBER, new Member()); // false면 test fail
    }

}