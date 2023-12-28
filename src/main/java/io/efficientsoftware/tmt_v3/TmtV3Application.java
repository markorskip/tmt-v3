package io.efficientsoftware.tmt_v3;

import groovyjarjarpicocli.CommandLine;
import io.efficientsoftware.tmt_v3.authority.Authorities;
import io.efficientsoftware.tmt_v3.authority.Authority;
import io.efficientsoftware.tmt_v3.project.Project;
import io.efficientsoftware.tmt_v3.project.ProjectDTO;
import io.efficientsoftware.tmt_v3.project.ProjectRepository;
import io.efficientsoftware.tmt_v3.project.ProjectService;
import io.efficientsoftware.tmt_v3.task.TaskDTO;
import io.efficientsoftware.tmt_v3.task.TaskRepository;
import io.efficientsoftware.tmt_v3.task.TaskService;
import io.efficientsoftware.tmt_v3.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@SpringBootApplication
public class TmtV3Application implements CommandLineRunner {

    public static void main(final String[] args) {
        SpringApplication.run(TmtV3Application.class, args);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;


    @Override
    public void run(String...args) throws Exception {
        System.out.println("Application Started !!");

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("skip@efficientsoftware.io");
        userDTO.setNickname("Skip");
        userDTO.setPassword("password");
        userDTO.setEnabled(true);
        userService.create(userDTO);



        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setOwner(userDTO.getEmail());
        projectDTO.setName("Build TMT v3");
        projectDTO.setDescription("This is a sample project");
        Long id = projectService.create(projectDTO);

        TaskDTO t1 = new TaskDTO();
        t1.setName("Build App");
        t1.setProject(id);
        Long t1Id = taskService.create(t1);

        TaskDTO t4 = new TaskDTO();
        t4.setName("Scaffold App with Spring, Thymeleaf, and HTMX");
        t4.setParentTask(t1Id);
        t4.setProject(id);
        taskService.create(t4);

        TaskDTO t2 = new TaskDTO();
        t2.setName("Deploy App");
        t2.setProject(id);
        taskService.create(t2);

        TaskDTO t3 = new TaskDTO();
        t3.setName("Add Payment Processing");
        t3.setProject(id);
        taskService.create(t3);


    }

}
