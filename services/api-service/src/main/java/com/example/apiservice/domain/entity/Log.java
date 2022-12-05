package com.example.apiservice.domain.entity;

import com.example.apiservice.type.enumration.LogStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_log")
public class Log extends Base {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 500, nullable = false)
    private String operation;

    @Column(length = 50, nullable = false)
    private String ip;

    @Column(length = 1000)
    private String content;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private LogStatus logStatus;
}
