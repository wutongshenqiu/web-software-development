package com.example.apiservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class Base implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @JsonIgnore
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    @Builder.Default
    protected LocalDateTime createTime = LocalDateTime.now();

    @JsonIgnore
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    @Builder.Default
    protected LocalDateTime updateTime = LocalDateTime.now();

    @JsonIgnore
    @Column(length = 1000)
    protected String remark;
}
