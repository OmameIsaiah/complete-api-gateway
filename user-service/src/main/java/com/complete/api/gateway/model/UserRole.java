package com.complete.api.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"user_role\"")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserRole extends BaseEntity {
    @JoinColumn(name = "roleid", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Role roleid;
    @JoinColumn(name = "userrole", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private UsersTable userrole;
}
