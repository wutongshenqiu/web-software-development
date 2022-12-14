package com.example.apiservice.domain.entity;

import com.example.apiservice.type.enumration.GroupStatus;
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
@Table(
        name = "tb_group",
        indexes = {
                @Index(columnList = "inviteCode")
        }
)
public class Group extends Base {
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false, unique = true)
    private String inviteCode;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private GroupStatus groupStatus = GroupStatus.ACTIVE;
}
