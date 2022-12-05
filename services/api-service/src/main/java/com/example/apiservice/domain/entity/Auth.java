package com.example.apiservice.domain.entity;


import com.example.apiservice.type.enumration.AuthStatus;
import com.example.apiservice.type.enumration.AuthType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_auth")
public class Auth extends Base {
    @Column(nullable = false, length = 50)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private AuthStatus authStatus = AuthStatus.ACTIVE;

    @JsonIgnore
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private AuthType authType;

    @JsonIgnore
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime lastLoginTime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
