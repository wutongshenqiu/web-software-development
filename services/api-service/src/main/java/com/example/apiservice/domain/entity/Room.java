package com.example.apiservice.domain.entity;

import com.example.apiservice.type.enumration.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "room")
    private List<Bed> beds;
}
