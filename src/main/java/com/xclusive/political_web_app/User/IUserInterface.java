package com.xclusive.political_web_app.User;

import com.xclusive.political_web_app.Registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserInterface {
    List<User> getUsers();

    User registerUser(RegistrationRequest request);

    Optional<User> findByEmail(String email);

    void saveVerificationTokenForUser(String verificationToken, User user);

    String validateVerificationToken(String token);
}
