package com.example.apiservice.domain.entity;


import com.example.apiservice.type.enumration.Gender;
import com.example.apiservice.type.enumration.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "tb_user",
        indexes = {
                @Index(columnList = "telephone"),
                @Index(columnList = "email")
        }
)
public class User extends Base {
    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private Gender gender = Gender.UNKNOWN;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(unique = true, nullable = false, length = 30)
    private String telephone;

    @JsonIgnore
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private UserStatus userStatus = UserStatus.ACTIVE;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Auth> auths;

    @ManyToMany
    @JoinTable(
            name = "tb_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles;
}
