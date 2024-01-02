package io.efficientsoftware.tmt_v3.task;

import io.efficientsoftware.tmt_v3.project.Project;
import io.efficientsoftware.tmt_v3.project.ProjectRepository;
import io.efficientsoftware.tmt_v3.util.NotFoundException;
import io.efficientsoftware.tmt_v3.util.WebUtils;

import java.math.BigDecimal;
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
            return parentTask.getTasks().get(parentTask.getTasks().size()-1).getId();
        } else if (taskDTO.getProject() != null){
            Project project = projectRepository.findById(task.getProject().getId()).get();
            project.getTasks().add(task);
            projectRepository.save(project);
            return project.getTasks().get(project.getTasks().size()-1).getId();
        }
        throw new NotFoundException();
    }

    public void update(final Long id, final TaskDTO taskDTO) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(taskDTO, task);
        taskRepository.save(task);
    }

    public void delete(final Long taskIdToDelete) {
        //For ordering we need to find the parent id
        Task taskToRemove = taskRepository.findById(taskIdToDelete).get();
        Long parentTaskId = taskToRemove.getParentTask().getId();
        Long projectId = taskToRemove.getProject().getId();
        taskToRemove.setParentTask(null);
        taskToRemove.setProject(null);
        if (parentTaskId != null) {
            Task parentTask = taskRepository.findById(parentTaskId).get();
            parentTask.getTasks().remove(taskToRemove);
            taskRepository.save(parentTask);
        } else {
            Project project = projectRepository.findById(projectId).get();
            project.getTasks().remove(taskToRemove);
            projectRepository.save(project);
        }

        taskRepository.delete(taskToRemove);

        System.out.println("Deleted id: " + taskIdToDelete);
    }

    public TaskDTO mapToDTO(final Task task, final TaskDTO taskDTO) {
        taskDTO.setId(task.getId());
        taskDTO.setName(task.getName());
        taskDTO.setProject(task.getProject() == null ? null : task.getProject().getId());
        taskDTO.setParentTask(task.getParentTask() == null ? null : task.getParentTask().getId());
        if (task.getTasks() == null || task.getTasks().isEmpty()) {
            taskDTO.setCost(task.getCost() != null ? task.getCost() : new BigDecimal(0.0));
            taskDTO.setTime(task.getTime() != null ? task.getTime() : new BigDecimal(0.0));
            taskDTO.setDateCompleted(task.getDateCompleted());
            taskDTO.setCompletedBy(task.getCompletedBy());
        } else {
            taskDTO.setTasks(task.getTasks().stream().map(t->mapToDTO(t,new TaskDTO())).collect(Collectors.toList()));
        }
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

    public void changeOrderOfTask(Long idOfTaskToMove, int indexToMoveTo) {
        final Task taskToMove = taskRepository.findById(idOfTaskToMove)
                .orElseThrow(NotFoundException::new);
        Long parentID = taskToMove.getParentTask().getId();
        final Task parentTask = taskRepository.findById(parentID).get();

        parentTask.getTasks().remove(taskToMove);
        parentTask.getTasks().add(indexToMoveTo, taskToMove);
        taskRepository.save(parentTask);
    }
}
