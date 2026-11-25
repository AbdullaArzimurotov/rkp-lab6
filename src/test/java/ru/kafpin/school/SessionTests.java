package ru.kafpin.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SessionTests {
    @Autowired
    private MockMvc mvc;

    @Test
    void userCanReadButOnlyAdminCanAdd() throws Exception {
        mvc.perform(get("/schoolboys")).andExpect(status().is3xxRedirection());
        mvc.perform(formLogin().user("user").password("wrong")).andExpect(redirectedUrl("/login?error"));

        var user = login("user", "UserLab6!");
        mvc.perform(get("/schoolboys").session(user)).andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Добавить школьника"))));
        mvc.perform(get("/schoolboys/new").session(user)).andExpect(status().isForbidden());
        mvc.perform(post("/schoolboys").session(user).with(csrf())).andExpect(status().isForbidden());
        mvc.perform(get("/logout").session(user)).andExpect(view().name("logout"));
        mvc.perform(post("/logout").session(user).with(csrf())).andExpect(redirectedUrl("/login?logout"));

        var admin = login("admin", "AdminLab6!");
        mvc.perform(get("/schoolboys").session(admin)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Добавить школьника")));
        mvc.perform(get("/schoolboys/new").session(admin)).andExpect(status().isOk());
        mvc.perform(post("/schoolboys").session(admin)).andExpect(status().isForbidden());
    }

    private MockHttpSession login(String username, String password) throws Exception {
        return (MockHttpSession) mvc.perform(formLogin().user(username).password(password))
                .andExpect(redirectedUrl("/schoolboys")).andReturn().getRequest().getSession(false);
    }
}
