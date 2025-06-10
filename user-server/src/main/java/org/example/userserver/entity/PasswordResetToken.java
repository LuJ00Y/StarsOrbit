package org.example.userserver.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Calendar;

@Data
public class PasswordResetToken {
    private static final int EXPIRATION_HOURS = 1; // 令牌有效期1小时

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
    private Date expiryDate;

    private boolean used;

    public PasswordResetToken() {
        this.expiryDate = calculateExpiryDate();
    }

    public PasswordResetToken(User user, String token) {
        this.user = user;
        this.token = token;
        this.expiryDate = calculateExpiryDate();
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, EXPIRATION_HOURS);
        return cal.getTime();
    }

    public boolean isExpired() {
        return new Date().after(this.expiryDate);
    }
}