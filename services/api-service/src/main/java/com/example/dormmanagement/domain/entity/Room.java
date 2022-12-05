package com.example.dormmanagement.domain.entity;

import com.example.dormmanagement.type.enumration.Gender;
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
@Table(name = "tb_room")
public class Room extends Base {
    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Gender gender;

    @Column(length = 1000)
    private String description;

    @Column(length = 100)
    private String imageUrl;
}
