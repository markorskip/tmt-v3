package io.efficientsoftware.tmt_v3.project;

import io.efficientsoftware.tmt_v3.task.Task;
import io.efficientsoftware.tmt_v3.task.TaskDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ProjectDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 5000)
    private String description;

    @NotNull
    @Size(max = 255)
    private String owner;

    private List<TaskDTO> tasks;

}
