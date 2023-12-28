package io.efficientsoftware.tmt_v3.note;

import io.efficientsoftware.tmt_v3.project.Project;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllById(Long id, Sort sort);

    Note findFirstByProject(Project project);

}
