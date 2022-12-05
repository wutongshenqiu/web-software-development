package com.example.dormmanagement.domain.entity;

import com.example.dormmanagement.type.enumration.RoleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "tb_role",
        indexes = {
                @Index(columnList = "name")
        }
)
public class Role extends Base {
    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @JsonIgnore
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private RoleStatus roleStatus = RoleStatus.ACTIVE;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    Set<User> users;
}
