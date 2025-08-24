package com.complete.api.gateway.model;

import com.complete.api.gateway.enums.Permissions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Table(name = "\"roles\"", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends BaseEntity {
    @Column(unique = true, name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    @JsonSerialize
    @JsonDeserialize
    @Type(JsonStringType.class)
    @Column(name = "permissions", columnDefinition = "JSON", nullable = false, updatable = true)
    private Set<Permissions> permissions;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleid")
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
