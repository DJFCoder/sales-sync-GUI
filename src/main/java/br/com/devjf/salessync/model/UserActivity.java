package br.com.devjf.salessync.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "user_activities")
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String description;
    
    @Column(name = "activity_time", nullable = false)
    private LocalDateTime activityTime;
    
    @PrePersist
    protected void onCreate() {
        this.activityTime = LocalDateTime.now();
    }
    
    // Constructors
    public UserActivity() {
    }
    
    public UserActivity(User user, String description) {
        this.user = user;
        this.description = description;
        this.activityTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getActivityTime() {
        return activityTime;
    }
    
    public void setActivityTime(LocalDateTime activityTime) {
        this.activityTime = activityTime;
    }
    
    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return activityTime.format(formatter);
    }
    
    public String getFormattedTimeShort() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return activityTime.format(formatter);
    }
    
    @Override
    public String toString() {
        return "UserActivity{" +
                "id=" + id +
                ", user=" + user.getName() +
                ", description='" + description + '\'' +
                ", activityTime=" + activityTime +
                '}';
    }
}
