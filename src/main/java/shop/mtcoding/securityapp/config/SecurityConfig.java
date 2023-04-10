package shop.mtcoding.securityapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf 잘못된 경로에서 오는 것을 막는다
        // 1. CSRF 해제
        http.csrf().disable(); // postman 접근 해야 함 - CSR할때 사용한다

        // 2. Form 로그인 설정
        http.formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // Post + X-www-formUrlEndcoded
                // 인증페이지를 기억 해놨다가 성공하면 가는 url
                .defaultSuccessUrl("/")
                // 여기서 다 써버리면 너무 복잡하니 클래스를 따로 빼서 사용 해주자
                .successHandler((req, resp, authentioncation) -> {
                    System.out.println("디버그 : 로그인이 완료되었습니다");

                })
                .failureHandler((req, resp, ex) -> {
                    System.out.println("디버그 : 로그인 실패 -> " + ex.getMessage());

                });
        return http.build();
    }
}
