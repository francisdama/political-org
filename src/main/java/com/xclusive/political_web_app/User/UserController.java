package com.xclusive.political_web_app.User;

import com.xclusive.political_web_app.Login.AllUserModel;
import com.xclusive.political_web_app.Security.RegistrationDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RegistrationDetailsService userDetailService;

    @GetMapping
    public UsersResponsePojo getData(@RequestBody AllUserModel model){

        List<AppUser> allAppUsers = new ArrayList<>();
        UsersResponsePojo response = new UsersResponsePojo ();
        UserDetails details = userDetailService.loadUserByUsername(model.getEmail());
        if (details != null && details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN")) && details.isEnabled()) {
            allAppUsers = userService.getUsers();
            response.setSuccess(true);
            response.setData(allAppUsers);
        }
        return response;
    }
//    public List<AppUser> getUsers(@RequestBody AllUserModel model){
//        List<AppUser> allAppUsers = new ArrayList<>();
//        UserDetails details = userDetailService.loadUserByUsername(model.getEmail());
//        if (details != null && details.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ADMIN")) && details.isEnabled()) {
//            allAppUsers = userService.getUsers();
//        }
//        return allAppUsers;
//    }
}
