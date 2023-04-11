package shop.mtcoding.securityapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.securityapp.core.auth.MyUserDetails;
import shop.mtcoding.securityapp.dto.ResponseDTO;
import shop.mtcoding.securityapp.dto.UserRequest;
import shop.mtcoding.securityapp.dto.UserResponse;
import shop.mtcoding.securityapp.service.UserService;

@RequiredArgsConstructor
@Controller
public class HelloController {

    private final UserService userService;

    // 인증이 필요한 주소를 갓을 때, 인증이 되지 않으니 페이지를 가준다
    @GetMapping("/users/{id}")
    public ResponseEntity<?> userCheck(@PathVariable Long id, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        String username = myUserDetails.getUser().getUsername();
        String role = myUserDetails.getUser().getRole();
        return ResponseEntity.ok().body(username + " : " + role);
    }

    @GetMapping("/")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok().body("ok");
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
