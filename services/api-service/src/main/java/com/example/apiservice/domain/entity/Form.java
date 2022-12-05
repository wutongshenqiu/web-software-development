package com.example.apiservice.domain.entity;

import com.example.apiservice.type.enumration.FormStatus;
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
@Table(name = "tb_form")
public class Form extends Base {
    @ManyToOne
    @JoinColumn(name = "submitter_id", nullable = false)
    private User submitter;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(columnDefinition = "TIMESTAMP")
    @Builder.Default
    private LocalDateTime finishTime = LocalDateTime.now();

    @Column(length = 1000)
    private String resultContent;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private FormStatus formStatus = FormStatus.CREATED;
}
