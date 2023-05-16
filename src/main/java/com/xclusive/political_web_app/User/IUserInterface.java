package com.xclusive.political_web_app.User;

import com.xclusive.political_web_app.Registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserInterface {
    List<AppUser> getUsers();

    AppUser registerUser(RegistrationRequest request);

    Optional<AppUser> findByEmail(String email);

    void saveVerificationTokenForUser(String verificationToken, AppUser appUser);

    String validateVerificationToken(String token);
}
