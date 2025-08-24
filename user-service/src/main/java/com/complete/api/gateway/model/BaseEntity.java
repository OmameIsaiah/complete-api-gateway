package com.complete.api.gateway.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Setter
@Getter
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @Basic(optional = false)
    private Long id;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "last_modified")
    @UpdateTimestamp
    private LocalDateTime lastModified;
    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;
}
