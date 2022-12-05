package com.example.apiservice.domain.entity;

import com.example.apiservice.type.enumration.BedStatus;
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
@Table(name = "tb_bed")
public class Bed extends Base {
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private BedStatus bedStatus = BedStatus.AVAILABLE;
}
