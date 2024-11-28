package model.enums;

import java.util.Arrays;

public enum ProfileEnum {

    ROLE_ADMIN("Profile Admin"),
    ROLE_CUSTOMER("Profile Customer"),
    ROLE_TECHNICIAN("Profile Technician");

    private final String description;

    ProfileEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ProfileEnum toEnum(final String description) {
        return Arrays.stream(ProfileEnum.values())
                .filter(profileEnum -> profileEnum.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid description: " + description));
    }

}
