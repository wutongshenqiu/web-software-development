package com.example.dormmanagement.domain.entity;

import com.example.dormmanagement.type.enumration.GroupMemberStatus;
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
@Table(name = "tb_group_member")
public class GroupMember extends Base {
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    @Builder.Default
    private LocalDateTime joinTime = LocalDateTime.now();

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime leaveTime;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private GroupMemberStatus groupMemberStatus = GroupMemberStatus.JOINED;
}
