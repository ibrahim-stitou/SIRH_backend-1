package com.tarmiz.SIRH_backend.model.entity.Contract;

import com.tarmiz.SIRH_backend.enums.Contract.ScheduleTypeEnum;
import com.tarmiz.SIRH_backend.model.entity.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "contract_schedules")
public class ContractSchedule extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Enumerated(EnumType.STRING)
    private ScheduleTypeEnum scheduleType;

    private Boolean shiftWork = false;
    private Integer hoursPerDay;
    private Integer daysPerWeek;
    private Integer hoursPerWeek;

    private LocalTime startTime;
    private LocalTime endTime;

    private Integer breakDuration;
    private Integer annualLeaveDays;

    @Column(columnDefinition = "TEXT")
    private String otherLeaves;

    @ManyToOne
    @JoinColumn(name = "amendment_id")
    private Amendment amendment;

    private LocalDate effectiveDate;

    @Column(nullable = false)
    private Boolean active = true;
}