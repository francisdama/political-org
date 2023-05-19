package com.xclusive.political_web_app.User;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsersResponsePojo {
    private boolean success;
    private List<AppUser> data;
}
