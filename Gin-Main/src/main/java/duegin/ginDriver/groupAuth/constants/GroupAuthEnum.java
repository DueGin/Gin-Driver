package duegin.ginDriver.groupAuth.constants;

/**
 * @author DueGin
 */
public enum GroupAuthEnum {
    ADMIN("ROLE_GROUP_ADMIN"),
    USER("ROLE_GROUP_USER"),
    VISITOR("ROLE_GROUP_VISITOR"),
    DISABLED("ROLE_GROUP_DISABLED"),
    ;
    public final String value;

    GroupAuthEnum(String value) {
        this.value = value;
    }
}
