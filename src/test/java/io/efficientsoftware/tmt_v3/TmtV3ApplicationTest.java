package io.efficientsoftware.tmt_v3;

import io.efficientsoftware.tmt_v3.config.BaseIT;
import org.junit.jupiter.api.Test;


public class TmtV3ApplicationTest extends BaseIT {

    @Test
    void contextLoads() {
    }

    /*
    To test:
    basic user can access their own projects, nothing else

    pro user can access their owner project, collab projects, read only projects

    anyone can access home, login, register, demo

    read only projects cannot edit tasks or projects

    collab can edits tasks, not project details

    owners can edit projects and tasks, and delete them

    tasks cannot be deleted with children tasks

    projects cannot be deleted???

     */

}
