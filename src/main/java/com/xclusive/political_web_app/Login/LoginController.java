package com.xclusive.political_web_app.Login;

import com.xclusive.political_web_app.Security.RegistrationDetailsService;
import com.xclusive.political_web_app.User.AppUser;
import com.xclusive.political_web_app.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    private final RegistrationDetailsService userDetailService;

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody LoginModel loginModel){
        String hashed_pword = userService.checkUserPasswordByEmail(loginModel.getEmail());
        if (!BCrypt.checkpw(loginModel.getPassword(), hashed_pword)){
            return new ResponseEntity("Incorrect Username or password", HttpStatus.BAD_REQUEST);
        }
        else {
            UserDetails details = userDetailService.loadUserByUsername(loginModel.getEmail());
            if (details != null && details.isEnabled()) {
                AppUser appUser = userService.getUserDetailsByEmail(loginModel.getEmail());

                return new ResponseEntity(appUser, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity("User not Enabled", HttpStatus.BAD_REQUEST);
            }
        }

    }

}
