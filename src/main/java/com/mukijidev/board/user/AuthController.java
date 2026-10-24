package com.mukijidev.board.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(request));
    }

    //로그인한 사용자의 정보. 로그인 안 했으면 Security가 먼저 막아서 401
    // @AuthenticationPrincipal: 세션에 저장된 LoginUser를 매개변수로 넣어준다
    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal LoginUser loginUser) {
        return userService.findById(loginUser.getId());
    }

    //CSRF 토큰을 쿠키(XSRF-TOKEN)으로 받기 위한 API. 화면이 처음 뜰 때 와 로그인, 로그아웃 뒤에 부른다
    //토큰은 꺼내 쓸 때 만들어지기떄문에 (지연생성), getTOken()을 불러서 쿠키가 만들어지게한다.
    @GetMapping("/csrf")
    public ResponseEntity<Void> csrf(CsrfToken csrfToken) {
        csrfToken.getToken();
        return ResponseEntity.noContent().build();
    }

}
