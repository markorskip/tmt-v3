package io.efficientsoftware.tmt_v3.user;

import io.efficientsoftware.tmt_v3.project.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {

    User findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    User findFirstByReadProjects(Project project);

    User findFirstByWriteProjects(Project project);

    List<User> findAllByReadProjects(Project project);

    List<User> findAllByWriteProjects(Project project);

}
