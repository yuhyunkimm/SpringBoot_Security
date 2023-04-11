package shop.mtcoding.securityapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import shop.mtcoding.securityapp.core.auth.MyUserDetails;
import shop.mtcoding.securityapp.core.jwt.MyJwtProvider;
import shop.mtcoding.securityapp.dto.ResponseDTO;
import shop.mtcoding.securityapp.dto.UserRequest;
import shop.mtcoding.securityapp.dto.UserResponse;
import shop.mtcoding.securityapp.model.User;
import shop.mtcoding.securityapp.model.UserRepository;
import shop.mtcoding.securityapp.service.UserService;

/*
 * 로그 레벨 : trace, debug, info, warn, error
 * Sysout을 남기면 안되는 이유 : 노헙으로 돌릴 시 로그가 남기 때문에 debug로 실행하자
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class HelloController {

    @Value("${meta.name}")
    private String name;

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest.LoginDTO loginDTO) {
        String jwt = userService.로그인(loginDTO);
        return ResponseEntity.ok().header(MyJwtProvider.HEADER, jwt).body("로그인완료");

    }

    // 인증이 필요한 주소를 갓을 때, 인증이 되지 않으니 페이지를 가준다
    @GetMapping("/users/{id}")
    public ResponseEntity<?> userCheck(@PathVariable Long id, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long userId = myUserDetails.getUser().getId();
        String role = myUserDetails.getUser().getRole();
        return ResponseEntity.ok().body(userId + " : " + role);
    }

    // user data 직접 접근하는 법
    // 현재 stateless상태여서 찾을 수가 없다
    @GetMapping("/")
    public ResponseEntity<?> hello() {
        // UserDetails, password, authories 까지 authentication으로 만들어짐
        // Authentication authentication1 =
        // authenticationManager.authenticate(authenticationToken);

        // 2번째 방법
        // User user = userRepository.findByUsername("username").get();
        // MyUserDetails myUserDetails = new MyUserDetails(user);
        // Authentication authentication2 = new UsernamePasswordAuthenticationToken(
        // "ssar",
        // "1234");
        // SecurityContextHolder.getContext().setAuthentication(authentication2);

        return ResponseEntity.ok().body(name);
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(UserRequest.JoinDTO joinDTO) {
        // SELECT 되고
        UserResponse.JoinDto data = userService.회원가입(joinDTO);
        // SELECT 안되고 (서비스에 트렌젝션이 종료되면서)
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(data);
        return ResponseEntity.ok(responseDTO);
    }
}
