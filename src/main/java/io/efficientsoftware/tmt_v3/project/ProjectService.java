package io.efficientsoftware.tmt_v3.project;

import io.efficientsoftware.tmt_v3.note.Note;
import io.efficientsoftware.tmt_v3.note.NoteRepository;
import io.efficientsoftware.tmt_v3.task.Task;
import io.efficientsoftware.tmt_v3.task.TaskDTO;
import io.efficientsoftware.tmt_v3.task.TaskRepository;
import io.efficientsoftware.tmt_v3.task.TaskService;
import io.efficientsoftware.tmt_v3.user.User;
import io.efficientsoftware.tmt_v3.user.UserRepository;
import io.efficientsoftware.tmt_v3.util.NotFoundException;
import io.efficientsoftware.tmt_v3.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final NoteRepository noteRepository;

    private final TaskService taskService;

    public ProjectService(final ProjectRepository projectRepository,
            final UserRepository userRepository, final TaskRepository taskRepository,
            final NoteRepository noteRepository, final TaskService taskService) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.noteRepository = noteRepository;
        this.taskService =  taskService;
    }

    public List<ProjectDTO> findAll() {
        final List<Project> projects = projectRepository.findAll(Sort.by("id"));
        return projects.stream()
                .map(project -> mapToDTO(project, new ProjectDTO()))
                .toList();
    }

    public ProjectDTO get(final Long id) {
        return projectRepository.findById(id)
                .map(project -> mapToDTO(project, new ProjectDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProjectDTO projectDTO) {
        final Project project = new Project();
        mapToEntity(projectDTO, project);
        return projectRepository.save(project).getId();
    }

    public void update(final Long id, final ProjectDTO projectDTO) {
        final Project project = projectRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(projectDTO, project);
        projectRepository.save(project);
    }

    public void delete(final Long id) {
        final Project project = projectRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        userRepository.findAllByReadProjects(project)
                .forEach(user -> user.getReadProjects().remove(project));
        userRepository.findAllByWriteProjects(project)
                .forEach(user -> user.getWriteProjects().remove(project));
        projectRepository.delete(project);
    }

    private ProjectDTO mapToDTO(final Project project, final ProjectDTO projectDTO) {
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setOwner(project.getOwner() == null ? null : project.getOwner().getEmail());
        projectDTO.setTasks(project.getTasks().stream().map(x->taskService.mapToDTO(x, new TaskDTO())).collect(Collectors.toList()));
        return projectDTO;
    }

    private Project mapToEntity(final ProjectDTO projectDTO, final Project project) {
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        final User owner = projectDTO.getOwner() == null ? null : userRepository.findById(projectDTO.getOwner())
                .orElseThrow(() -> new NotFoundException("owner not found"));
        project.setOwner(owner);
        return project;
    }

    public String getReferencedWarning(final Long id) {
        final Project project = projectRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Task projectTask = taskRepository.findFirstByProject(project);
        if (projectTask != null) {
            return WebUtils.getMessage("project.task.project.referenced", projectTask.getId());
        }
        final Note projectNote = noteRepository.findFirstByProject(project);
        if (projectNote != null) {
            return WebUtils.getMessage("project.note.project.referenced", projectNote.getId());
        }
        final User readProjectsUser = userRepository.findFirstByReadProjects(project);
        if (readProjectsUser != null) {
            return WebUtils.getMessage("project.user.readProjects.referenced", readProjectsUser.getEmail());
        }
        final User writeProjectsUser = userRepository.findFirstByWriteProjects(project);
        if (writeProjectsUser != null) {
            return WebUtils.getMessage("project.user.writeProjects.referenced", writeProjectsUser.getEmail());
        }
        return null;
    }

}
