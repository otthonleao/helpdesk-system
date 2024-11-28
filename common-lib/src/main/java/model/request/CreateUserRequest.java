package model.request;

import lombok.With;
import model.enums.ProfileEnum;
import java.util.Set;
@With
public record CreateUserRequest(

        String name,
        String email,
        String password,
        Set<ProfileEnum> profiles

) {
}
