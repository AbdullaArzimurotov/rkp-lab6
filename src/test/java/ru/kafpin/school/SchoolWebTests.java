package ru.kafpin.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SchoolWebTests {
    @Autowired
    private MockMvc mvc;

    @Test
    void combinesSurnameCitySchoolAndClassFilters() throws Exception {
        mvc.perform(list().param("surname", "Соколов").param("city", "Москва"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Найдено записей: 2")));
        mvc.perform(list().param("schoolId", "1").param("classId", "1"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Найдено записей: 2")))
                .andExpect(content().string(not(containsString("Соколов Сергей"))));
        mvc.perform(list().param("city", "Тверь").param("schoolId", "1").param("classId", "2"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Найдено записей: 1")))
                .andExpect(content().string(containsString("Орлова Елена")));
        mvc.perform(list().param("schoolId", "1").param("classId", "3"))
                .andExpect(model().attributeHasErrors("filter"));
    }

    @Test
    void rejectsInvalidFormsAndSavesValidPupil() throws Exception {
        mvc.perform(post("/schoolboys").with(user("admin").roles("ADMIN")).with(csrf()))
                .andExpect(status().isOk()).andExpect(view().name("form")).andExpect(model().attributeHasErrors("schoolboy"));
        mvc.perform(form("3")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Укажите существующий класс выбранной школы.")));
        mvc.perform(form("1")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("/schoolboys/*"));
    }

    private MockHttpServletRequestBuilder list() {
        return get("/schoolboys").with(user("user").roles("USER"));
    }

    private MockHttpServletRequestBuilder form(String classId) {
        return post("/schoolboys").with(user("admin").roles("ADMIN")).with(csrf())
                .param("surname","Фомин").param("name","Артём").param("gender","Мужской")
                .param("nationality","Русский").param("height","156.5").param("weight","48.0")
                .param("birthDate","2013-04-15").param("phoneNumber","+7 900 000-44-01")
                .param("address.postalCode","101000").param("address.country","Россия")
                .param("address.region","Москва").param("address.district","Басманный")
                .param("address.city","Москва").param("address.street","Покровка").param("address.house","8")
                .param("schoolId","1").param("classId",classId);
    }
}
