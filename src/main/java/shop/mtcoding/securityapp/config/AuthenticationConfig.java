package shop.mtcoding.securityapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import lombok.Getter;

@Getter
@Configuration
public class AuthenticationConfig extends AbstractHttpConfigurer<AuthenticationConfig, HttpSecurity> {

    private AuthenticationManager authenticationManager;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        authenticationManager = builder.getSharedObject(AuthenticationManager.class);
        super.configure(builder);
    }
}
