package models;

public enum Role {
    ARTIST("Artist"),
    DELIVERYMAN("Deliveryman"),
    BROWSER("Browser"),
    ADMIN("Admin"); // Add the admin role

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }


    public static Role fromString(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            // Handle null or empty values appropriately, such as returning a default role
            return getDefaultRole();
        }
        for (Role role : Role.values()) {
            if (role.roleName.equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No constant with name " + roleName + " found");
    }

    public static Role getDefaultRole() {
        return BROWSER; // You can change this to the desired default role
    }

    @Override
    public String toString() {
        return roleName;
    }
}