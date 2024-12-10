package dev.otthon.helpdesk.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.enums.ProfileEnum;

import java.util.Set;

@Getter
@AllArgsConstructor
public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private Set<ProfileEnum> profiles;

}
