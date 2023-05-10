package com.xclusive.political_web_app.User;

import com.xclusive.political_web_app.Login.AllUserModel;
import com.xclusive.political_web_app.Security.RegistrationDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RegistrationDetailsService userDetailService;

    @GetMapping
    public List<User> getUsers(@RequestBody AllUserModel model){
        List<User> allUsers = new ArrayList<>();
        UserDetails details = userDetailService.loadUserByUsername(model.getEmail());
        if (details != null && details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN")) && details.isEnabled()) {
            allUsers = userService.getUsers();
        }
        return allUsers;
    }
}
