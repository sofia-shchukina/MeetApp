package sonia.meetapp.app.participants;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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

        MvcResult result = mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mike"}
                                 """)
                )
                .andExpect(status().is(201))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("Mike"));
    }

    @DirtiesContext
    @Test
    void deleteParticipant() throws Exception {

        String result = mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mike"}
                                 """)
                )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Participant savedParticipant = objectMapper.readValue(result, Participant.class);
        String id = savedParticipant.getId();

        mockMvc.perform(delete("/participants/" + id))
                .andExpect(status().is(204));

        mockMvc.perform(MockMvcRequestBuilders.get("/participants"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        []
                        """));
    }

    @DirtiesContext
    @Test
    void deleteParticipantDoesNotExist() throws Exception {

        String id = "111";
        mockMvc.perform(MockMvcRequestBuilders.delete("/participants/" + id))
                .andExpect(status().is(404));
    }

}
