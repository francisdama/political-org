package com.xclusive.political_web_app.User;

import com.xclusive.political_web_app.Exception.UserAlreadyExistException;
import com.xclusive.political_web_app.Registration.RegistrationRequest;
import com.xclusive.political_web_app.Registration.Token.VerificationToken;
import com.xclusive.political_web_app.Registration.Token.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserInterface{
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenRepository verificationTokenRepository;
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public  String checkUserPasswordByEmail(String email){
        return userRepository.checkUserPasswordByEmail(email);
    }

    public  User getUserDetailsByEmail(String email){
        return userRepository.getUserDetailsByEmail(email);
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> user = userRepository.findByEmail(request.email());
        if (user.isPresent()){
            throw new UserAlreadyExistException("User with email " + request.email() + " Already exist");
        }
        var newUser = new User();
        newUser.setFirstname(request.firstname());
        newUser.setLastname(request.lastname());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());
        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveVerificationTokenForUser(String verificationToken, User user) {
        VerificationToken token
                = new VerificationToken(user, verificationToken);

        verificationTokenRepository.save(token);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken
                = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return "invalid verification token";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "verification token expired";
        }

        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }
}
