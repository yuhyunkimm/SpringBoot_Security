package shop.mtcoding.securityapp.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.securityapp.dto.UserRequest;
import shop.mtcoding.securityapp.model.User;
import shop.mtcoding.securityapp.model.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User 회원가입(UserRequest.joinDTO joinDTO) {
        String rawPassword = joinDTO.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);// 60byte
        joinDTO.setPassword(encPassword);
        return userRepository.save(joinDTO.toEntity());
    }
}
