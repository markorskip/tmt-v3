package io.efficientsoftware.tmt_v3.task;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.efficientsoftware.tmt_v3.project.Project;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Tasks")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Task {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(precision = 10, scale = 2)
    private BigDecimal time;

    @Column
    private LocalDateTime dateCompleted;

    @Column
    private String completedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "parentTask", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderColumn(name = "order_id")
    private List<Task> tasks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    public BigDecimal getComputedCost() {
        getTasks();
        if (tasks.isEmpty()){
            return cost != null ? cost : new BigDecimal(0);
        }
        BigDecimal result = new BigDecimal(0);
        tasks.forEach(task-> {
            result.add(task.getComputedCost());
        });
        return result;
    }

    public BigDecimal getComputedTime() {
        if (tasks.isEmpty()) return time != null ? cost : new BigDecimal(0);
        BigDecimal result = new BigDecimal(0);
        tasks.forEach(task-> {
            result.add(task.getComputedTime());
        });
        return result;
    }

}
