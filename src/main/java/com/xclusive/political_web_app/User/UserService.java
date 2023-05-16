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
    public List<AppUser> getUsers() {
        return userRepository.findAll();
    }

    public  String checkUserPasswordByEmail(String email){
        return userRepository.checkUserPasswordByEmail(email);
    }

    public AppUser getUserDetailsByEmail(String email){
        return userRepository.getUserDetailsByEmail(email);
    }

    @Override
    public AppUser registerUser(RegistrationRequest request) {
        Optional<AppUser> user = userRepository.findByEmail(request.email());
        if (user.isPresent()){
            throw new UserAlreadyExistException("User with email " + request.email() + " Already exist");
        }
        var newUser = new AppUser();
        newUser.setFirstname(request.firstname());
        newUser.setLastname(request.lastname());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());
        return userRepository.save(newUser);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveVerificationTokenForUser(String verificationToken, AppUser appUser) {
        VerificationToken token
                = new VerificationToken(appUser, verificationToken);

        verificationTokenRepository.save(token);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken
                = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return "invalid verification token";
        }

        AppUser appUser = verificationToken.getAppUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "verification token expired";
        }

        appUser.setEnabled(true);
        userRepository.save(appUser);
        return "valid";
    }
}
