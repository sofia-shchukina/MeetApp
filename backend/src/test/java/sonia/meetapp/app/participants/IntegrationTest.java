package sonia.meetapp.app.participants;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private Utility utility;

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
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mike"}
                                 """)
                )
                .andExpect(status().is(201));


        mockMvc.perform(delete("/participants/" + "123"))
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

    @DirtiesContext
    @Test
    void editParticipant() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mike"}
                                 """)
                )
                .andExpect(status().is(201));

        mockMvc.perform(put("/participants/edit/" + "123")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Nike"}
                                 """)
                )
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/participants"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{"name":"Nike", "id": "123"}]
                          """));
    }
}
