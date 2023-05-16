package com.xclusive.political_web_app.Registration.Token;

import com.xclusive.political_web_app.User.AppUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class VerificationToken {

    // Expiration time will be 10mins only
    private static final int Expiration_Time = 10;
    @Id
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    private Integer id;

    private String token;
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",
            nullable = false)
//            foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private AppUser appUser;

    public VerificationToken(AppUser appUser, String token){
        super();
        this.token = token;
        this.appUser = appUser;
        this.expirationTime = CalculateExpirationDate(Expiration_Time);

    }

    public VerificationToken(String token){
        super();
        this.token = token;
        this.expirationTime = CalculateExpirationDate(Expiration_Time);
    }

    private Date CalculateExpirationDate(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
