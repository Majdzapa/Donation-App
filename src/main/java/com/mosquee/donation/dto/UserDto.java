package com.mosquee.donation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class UserDto {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
