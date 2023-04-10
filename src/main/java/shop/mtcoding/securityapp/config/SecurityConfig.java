package shop.mtcoding.securityapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf 잘못된 경로에서 오는 것을 막는다
        // 1. CSRF 해제
        http.csrf().disable(); // postman 접근 해야 함 - CSR할때 사용한다

        // 2. Form 로그인 설정
        http.formLogin()
                .loginPage("/loginForm")
                // 커스텀 가능
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login") // Post + X-www-formUrlEndcoded
                // 인증페이지를 기억 해놨다가 성공하면 가는 url
                .defaultSuccessUrl("/")
                // 여기서 다 써버리면 너무 복잡하니 클래스를 따로 빼서 사용 해주자
                .successHandler((req, resp, authentioncation) -> {
                    System.out.println("디버그 : 로그인이 완료되었습니다");
                    resp.sendRedirect("/");
                    // 404로 가면 인증 완료
                    // 인증이 안되면 다시 로그인 페이지로
                })
                .failureHandler((req, resp, ex) -> {
                    System.out.println("디버그 : 로그인 실패 -> " + ex.getMessage());

                });

        // 3. 인증, 권한 필터 설정
        // http.authorizeHttpRequests().antMatchers(null);
        http.authorizeRequests(
                authroize -> authroize.antMatchers("/users/**").authenticated()
                        .antMatchers("/manager/**")
                        .access("hasRole('ADMIN') or hasRole('MANAGER')")
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll());

        return http.build();
    }
}
