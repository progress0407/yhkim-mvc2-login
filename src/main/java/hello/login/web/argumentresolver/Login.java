package hello.login.web.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 인자에 적용
 */
@Target(ElementType.PARAMETER)
/**
 * RUNTIME 까지 유효
 * 거의 이 정책을 사용하게 된다
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}
