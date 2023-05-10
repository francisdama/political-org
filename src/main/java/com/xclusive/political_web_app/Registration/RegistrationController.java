package com.xclusive.political_web_app.Registration;

import com.xclusive.political_web_app.Event.RegistrationCompleteEvent;
import com.xclusive.political_web_app.Registration.Token.VerificationToken;
import com.xclusive.political_web_app.Registration.Token.VerificationTokenRepository;
import com.xclusive.political_web_app.Security.RegistrationDetailsService;
import com.xclusive.political_web_app.User.User;
import com.xclusive.political_web_app.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    private final ApplicationEventPublisher publisher;

    private final VerificationTokenRepository verificationTokenRepository;

    private final RegistrationDetailsService userDetailService;

    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
        User user = userService.registerUser(registrationRequest);
            //publish registration event
            publisher.publishEvent(new RegistrationCompleteEvent(user,appUrl(request)));
            return "Registration Successful, Please check your email to complete registration";

    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {

        VerificationToken theToken = verificationTokenRepository.findByToken(token);
        if(theToken.getUser().isEnabled()) {
            return "Account already verified please login";
        }
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")) {
            return "Email Verified Successfully, now you can log into your account";
        }
        return "Bad User";
    }

    public String appUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
