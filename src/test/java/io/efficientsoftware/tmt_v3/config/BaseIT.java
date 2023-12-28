package io.efficientsoftware.tmt_v3.config;

import io.efficientsoftware.tmt_v3.TmtV3Application;
import io.efficientsoftware.tmt_v3.authority.AuthorityRepository;
import io.efficientsoftware.tmt_v3.note.NoteRepository;
import io.efficientsoftware.tmt_v3.project.ProjectRepository;
import io.efficientsoftware.tmt_v3.task.TaskRepository;
import io.efficientsoftware.tmt_v3.user.UserRepository;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StreamUtils;


/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context, with all data
 * wiped out before each test.
 */
@SpringBootTest(
        classes = TmtV3Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ActiveProfiles("it")
@Sql({"/data/clearAll.sql", "/data/userData.sql"})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class BaseIT {

    private static MockHttpSession authenticatedSession = null;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public TaskRepository taskRepository;

    @Autowired
    public ProjectRepository projectRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public NoteRepository noteRepository;

    @Autowired
    public AuthorityRepository authorityRepository;

    @SneakyThrows
    public String readResource(final String resourceName) {
        return StreamUtils.copyToString(getClass().getResourceAsStream(resourceName), StandardCharsets.UTF_8);
    }

    public MockHttpSession authenticatedSession() throws Exception {
        if (authenticatedSession == null) {
            final MvcResult mvcResult = mockMvc.perform(
                    SecurityMockMvcRequestBuilders.formLogin().user("email", "bootify").password("password", "Bootify!")).andReturn();
            authenticatedSession = ((MockHttpSession)mvcResult.getRequest().getSession(false));
        }
        return authenticatedSession;
    }

}
