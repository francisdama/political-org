package com.xclusive.political_web_app.Registration;

import org.hibernate.annotations.NaturalId;

public record RegistrationRequest(
         String firstname,
         String lastname,
         String email,
         String password,
         String role) {


}
