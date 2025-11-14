package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role implements BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleName role;
}
