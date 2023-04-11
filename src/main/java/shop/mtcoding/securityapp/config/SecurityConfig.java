package shop.mtcoding.securityapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.access.AccessDeniedException;

import lombok.extern.slf4j.Slf4j;

//log남기기
@Slf4j
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

        // 2. ifram 거부
        http.headers().frameOptions().disable();

        // 3. cors 재설정
        http.cors().configurationSource(configurationSource());

        // 4. jSessionId 사용 거부
        // SetCookie에 담아서 설정하는 것을 사용 안한다
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 5. form 로그인 해제
        http.formLogin().disable();

        // 6. httpBasic 정책 해제
        http.httpBasic().disable();

        // 7. XSS (lucy필터)

        // 8. 커스텀 필터 적용 (시큐리티 필터 교환)
        // http.apply(null);

        // 9. 인증 실패 처리
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            // 블랙리스트처리 => 로비서버(디도스공격.해킹으로 관리하기 용이)
            // request.getRemoteAddr();
            // checkpoint -> 예외핸들러 처리
            log.debug("디버그 : 인증 실패 : " + authException.getMessage());
            log.info("인포 : 인증 실패 : " + authException.getMessage());
            log.warn("워닝 : 인증 실패 : " + authException.getMessage());
            log.error("에러 : 인증 실패 : " + authException.getMessage());
        });

        // 10. 권한 실패 처리
        http.exceptionHandling().accessDeniedHandler((request, response, accessDeniedException) -> {
            log.debug("디버그 : 인증 실패 : " + accessDeniedException.getMessage());
            log.info("인포 : 인증 실패 : " + accessDeniedException.getMessage());
            log.warn("워닝 : 인증 실패 : " + accessDeniedException.getMessage());
            log.error("에러 : 인증 실패 : " + accessDeniedException.getMessage());
        });

        // 11. 인증, 권한 필터 설정

        // http.authorizeHttpRequests().antMatchers(null);
        http.authorizeRequests(
                authroize -> authroize.antMatchers("/users/**").authenticated()
                        .antMatchers("/manager/**")
                        .access("hasRole('ADMIN') or hasRole('MANAGER')")
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll());

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (프론트 앤드 IP만 허용 react) , *를 걸면 안되고 프론트엔드IP만허용
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization"); // 옛날에는 디폴트 였다. 지금은 아닙니다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
