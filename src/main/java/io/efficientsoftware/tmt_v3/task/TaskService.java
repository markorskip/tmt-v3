package io.efficientsoftware.tmt_v3.task;

import io.efficientsoftware.tmt_v3.project.Project;
import io.efficientsoftware.tmt_v3.project.ProjectRepository;
import io.efficientsoftware.tmt_v3.util.NotFoundException;
import io.efficientsoftware.tmt_v3.util.WebUtils;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;


@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(final TaskRepository taskRepository,
            final ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public List<TaskDTO> findAll(final String filter) {
        List<Task> tasks;
        final Sort sort = Sort.by("id");
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            tasks = taskRepository.findAllById(longFilter, sort);
        } else {
            tasks = taskRepository.findAll(sort);
        }
        tasks.forEach(task-> {
            task.getTasks();
        });
        return tasks.stream()
                .map(task -> mapToDTO(task, new TaskDTO()))
                .toList();
    }

    public TaskDTO get(final Long id) {
        return taskRepository.findById(id)
                .map(task -> mapToDTO(task, new TaskDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TaskDTO taskDTO) {
        final Task task = new Task();
        mapToEntity(taskDTO, task);

        // Since every task is part of a list somewhere, we need to update the parent with the new task so it can keep track of the
        // list.
        // Either a project is parent or a task - not both
        if (taskDTO.getParentTask() != null) {
            Task parentTask = taskRepository.findById(taskDTO.getParentTask()).get();
            parentTask.getTasks().add(task);
            taskRepository.save(parentTask);
        } else {
            Project project = projectRepository.findById(task.getProject().getId()).get();
            project.getTasks().add(task);
            projectRepository.save(project);
        }

        return 1L;
    }

    public void update(final Long id, final TaskDTO taskDTO) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(taskDTO, task);
        taskRepository.save(task);
    }

    public void delete(final Long id) {
        Task task = taskRepository.findById(id).get();
        task.setParentTask(null);
        task.setProject(null);
        taskRepository.save(task);
        taskRepository.deleteById(task.getId());
        System.out.println("Deleted id: " + task.getId());
    }

    public TaskDTO mapToDTO(final Task task, final TaskDTO taskDTO) {
        taskDTO.setId(task.getId());
        taskDTO.setName(task.getName());
      //  taskDTO.setCost(task.getComputedCost());
      //  taskDTO.setTime(task.getComputedTime());
        taskDTO.setCost(task.getCost());
        taskDTO.setTime(task.getTime());
        taskDTO.setDateCompleted(task.getDateCompleted());
        taskDTO.setCompletedBy(task.getCompletedBy());
        taskDTO.setProject(task.getProject() == null ? null : task.getProject().getId());
        taskDTO.setParentTask(task.getParentTask() == null ? null : task.getParentTask().getId());
        taskDTO.setTasks(task.getTasks().stream().map(t->mapToDTO(t,new TaskDTO())).collect(Collectors.toList()));
        return taskDTO;
    }

    private Task mapToEntity(final TaskDTO taskDTO, final Task task) {
        task.setName(taskDTO.getName());
        task.setCost(taskDTO.getCost());
        task.setTime(taskDTO.getTime());
        task.setDateCompleted(taskDTO.getDateCompleted());
        task.setCompletedBy(taskDTO.getCompletedBy());
        final Project project = taskDTO.getProject() == null ? null : projectRepository.findById(taskDTO.getProject())
                .orElseThrow(() -> new NotFoundException("project not found"));
        task.setProject(project);
        final Task parentTask = taskDTO.getParentTask() == null ? null : taskRepository.findById(taskDTO.getParentTask())
                .orElseThrow(() -> new NotFoundException("parentTask not found"));
        task.setParentTask(parentTask);
        return task;
    }

    public String getReferencedWarning(final Long id) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Task parentTaskTask = taskRepository.findFirstByParentTaskAndIdNot(task, task.getId());
        if (parentTaskTask != null) {
            return WebUtils.getMessage("task.task.parentTask.referenced", parentTaskTask.getId());
        }
        return null;
    }

}
