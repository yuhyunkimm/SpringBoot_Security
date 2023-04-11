package shop.mtcoding.securityapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import shop.mtcoding.securityapp.core.auth.MyUserDetails;
import shop.mtcoding.securityapp.dto.ResponseDTO;
import shop.mtcoding.securityapp.dto.UserRequest;
import shop.mtcoding.securityapp.dto.UserResponse;
import shop.mtcoding.securityapp.dto.UserRequest.LoginDTO;
import shop.mtcoding.securityapp.service.UserService;

/*
 * 로그 레벨 : trace, debug, info, warn, error
 * Sysout을 남기면 안되는 이유 : 노헙으로 돌릴 시 로그가 남기 때문에 debug로 실행하자
 */

@RequiredArgsConstructor
@Controller
public class HelloController {

    @Value("${meta.name}")
    private String name;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest.LoginDTO loginDTO) {
        userService.로그인(loginDTO);
        return ResponseEntity.ok().body("로그인완료");

    }

    // 인증이 필요한 주소를 갓을 때, 인증이 되지 않으니 페이지를 가준다
    @GetMapping("/users/{id}")
    public ResponseEntity<?> userCheck(@PathVariable Long id, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        String username = myUserDetails.getUser().getUsername();
        String role = myUserDetails.getUser().getRole();
        return ResponseEntity.ok().body(username + " : " + role);
    }

    @GetMapping("/")
    public ResponseEntity<?> hello() {

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
