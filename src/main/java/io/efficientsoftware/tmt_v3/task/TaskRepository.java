package io.efficientsoftware.tmt_v3.task;

import io.efficientsoftware.tmt_v3.project.Project;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllById(Long id, Sort sort);

    Task findFirstByProject(Project project);

    Task findFirstByParentTaskAndIdNot(Task task, final Long id);

}
