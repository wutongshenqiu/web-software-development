package com.example.dormmanagement.domain.entity;

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
        name = "tb_student_info",
        indexes = {
                @Index(columnList = "studentId")
        }
)
public class StudentInfo extends Base {
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 50, nullable = false, unique = true)
    private String studentId;
}
