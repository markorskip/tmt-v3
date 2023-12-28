package io.efficientsoftware.tmt_v3.project;

import io.efficientsoftware.tmt_v3.config.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
aq  `\
\\\\\\\\\
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ProjectTest extends BaseIT {

    @Test
    @WithAnonymousUser
    public void expectHomePage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/")).andDo(print())
                .andExpect(view().name("home/index"));
    }

    @Test
    @WithAnonymousUser
    public void expectError() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/projects")).andDo(print()).andExpect(status().is3xxRedirection());
    }
}
