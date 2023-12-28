package io.efficientsoftware.tmt_v3.authority;

import io.efficientsoftware.tmt_v3.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findFirstByUser(User user);

}
