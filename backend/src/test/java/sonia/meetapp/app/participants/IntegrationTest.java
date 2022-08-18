package sonia.meetapp.app.participants;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @DirtiesContext
    @Test
    void getAllParticipants() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/participants"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        []
                        """));
    }

    @DirtiesContext
    @Test
    void addParticipant() throws Exception {

        mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mike"}
                                 """)
                )
                .andExpect(status().is(201));
    }
}
