package br.com.devjf.salessync.model;

public enum UserType {
    ADMIN(0),
    OWNER(1),
    EMPLOYEE(2);
    private final int id;

    UserType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static UserType fromId(int id) {
        for (UserType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("No UserType with id " + id);
    }
}
