package accountService.model;

public enum Role {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER"), MAIN_ADMIN("ROLE_MAIN_ADMIN"), GUEST("ROLE_GUEST");

    private final String name;

    Role(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return  name;
    }
}
