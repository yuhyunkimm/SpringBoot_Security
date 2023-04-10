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
        http.csrf().disable(); // postman 접근 해야 함 - CSR할때 사용한다
        http.formLogin()
                .loginPage("/loginPage")
                .loginProcessingUrl("/login") // Post + X-www-formUrlEndcoded
                .defaultSuccessUrl("/");
        return http.build();
    }
}
