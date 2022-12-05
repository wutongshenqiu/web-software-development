package com.example.dormmanagement.domain.entity;

import com.example.dormmanagement.type.enumration.SysConfigStatus;
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
@Table(name = "tb_sys_config")
public class SysConfig extends Base {
    @Column(length = 100, nullable = false)
    private String keyName;

    @Column(length = 1000, nullable = false)
    private String keyValue;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private SysConfigStatus sysConfigStatus = SysConfigStatus.ENABLE;
}
