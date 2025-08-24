package com.complete.api.gateway.model;

import com.complete.api.gateway.enums.UserType;
import com.complete.api.gateway.model.listener.EntityListener;
import jakarta.validation.constraints.*;
import lombok.*;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.complete.api.gateway.util.AppMessages.*;

@Entity
@Table(name = "\"users_table\"", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(EntityListener.class)
public class UsersTable extends BaseEntity implements Serializable {
    @Column(name = "name")
    @Size(max = 100, message = MAX_NAME_LIMIT_EXCEEDED)
    private String name;
    @Column(name = "email", unique = true, nullable = false)
    @Email(message = INVALID_EMAIL,
            flags = Pattern.Flag.CASE_INSENSITIVE,
            regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotEmpty(message = EMPTY_EMAIL)
    @NotNull(message = NULL_EMAIL)
    private String email;
    @Column(name = "password")
    @Size(min = 8, message = MIN_PASSWORD_LENGTH_NOT_REACHED)
    private String password;
    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Column(name = "user_token")
    private String userToken;
    @Column(name = "verified")
    private Boolean verified = false;
    @Column(name = "is_online")
    private Boolean isOnline = false;
    @Column(name = "otp_code")
    private String otpCode;
    @Column(name = "otp_expire_time")
    private String otpExpireTime;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userrole")
    private List<UserRole> userRoles;

    @Override
    public void setUuid(String uuid) {
        super.setUuid(uuid);
    }

    @Override
    public String getUuid() {
        return super.getUuid();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setDateCreated(LocalDateTime dateCreated) {
        super.setDateCreated(dateCreated);
    }

    @Override
    public LocalDateTime getDateCreated() {
        return super.getDateCreated();
    }

    @Override
    public void setLastModified(LocalDateTime lastModified) {
        super.setLastModified(lastModified);
    }

    @Override
    public LocalDateTime getLastModified() {
        return super.getLastModified();
    }
}
