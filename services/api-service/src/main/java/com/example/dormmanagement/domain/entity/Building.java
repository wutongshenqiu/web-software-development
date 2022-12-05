package com.example.dormmanagement.domain.entity;

import com.example.dormmanagement.type.enumration.BuildingStatus;
import lombok.Builder;
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
@Table(name = "tb_building")
public class Building extends Base {
    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(length = 100)
    private String imageUrl;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private BuildingStatus buildingStatus = BuildingStatus.AVAILABLE;
}
