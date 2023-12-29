package io.efficientsoftware.tmt_v3.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskDTO {

    private Long id;

    @Size(max = 255)
    private String name;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal cost;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal time;

    private LocalDateTime dateCompleted;

    @Size(max = 255)
    private String completedBy;

    private Long project;

    private Long parentTask;

    private List<TaskDTO> tasks;
}
