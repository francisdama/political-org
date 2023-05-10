package com.xclusive.political_web_app.Login;

import com.xclusive.political_web_app.Security.RegistrationDetailsService;
import com.xclusive.political_web_app.Security.UserRegistrationDetails;
import com.xclusive.political_web_app.User.User;
import com.xclusive.political_web_app.User.UserService;
import com.xclusive.political_web_app.UserInfo.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody LoginModel loginModel){
        String hashed_pword = userService.checkUserPasswordByEmail(loginModel.getEmail());
        if (!BCrypt.checkpw(loginModel.getPassword(), hashed_pword)){
            return new ResponseEntity("Incorrect Username or password", HttpStatus.BAD_REQUEST);
        }

        User user = userService.getUserDetailsByEmail(loginModel.getEmail());

        return new ResponseEntity(user, HttpStatus.OK);
    }

}
