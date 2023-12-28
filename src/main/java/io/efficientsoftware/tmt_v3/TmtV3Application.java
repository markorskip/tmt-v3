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
public class TmtV3Application {

    public static void main(final String[] args) {
        SpringApplication.run(TmtV3Application.class, args);
    }

}
