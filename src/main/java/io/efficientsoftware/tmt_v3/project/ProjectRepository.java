package io.efficientsoftware.tmt_v3.project;

import io.efficientsoftware.tmt_v3.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findFirstByOwner(User user);

}
