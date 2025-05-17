package br.com.devjf.salessync.model;

import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public final class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 50, unique = true)
    private String login;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_type_id")
    private UserType type;
    @Column(nullable = false)
    private boolean active = true;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Constructors
    public User() {
        this.name = getName();
        this.login = getLogin();
        this.type = getType();
    }

    public User(String name, String login, String password, UserType type) {
        this.name = name;
        this.login = login;
        this.password = BCrypt.hashpw(password,
                BCrypt.gensalt());
        this.type = type;
    }

    // Methods
    public boolean authenticate(String login, String password) {
        return this.login.equals(login) && BCrypt.checkpw(password,
                this.password);
    }

    public void changePassword(String newPassword) {
        // Always hash the new password
        this.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // Check if the password is already hashed (starts with $2a$)
        if (password.startsWith("$2a$")) {
            this.password = password;
        } else {
            // Hash the raw password
            this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        }
    }

    public final UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
