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

    @DirtiesContext
    @Test
    void addLikes() throws Exception {

        String saveResult = mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mike"}
                                 """)
                )
                .andReturn()
                .getResponse()
                .getContentAsString();

        Participant saveResultParticipant = objectMapper.readValue(saveResult, Participant.class);
        String id = saveResultParticipant.getId();

        String saveResult2 = mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mary"}
                                 """)
                )
                .andReturn()
                .getResponse()
                .getContentAsString();

        Participant saveResultParticipant2 = objectMapper.readValue(saveResult2, Participant.class);
        String id2 = saveResultParticipant2.getId();


        mockMvc.perform(put("/participants/likes/")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"likerID":"<ID>","likedPeopleIDs": ["<ID2>"]}
                                                             
                                 """.replaceFirst("<ID>", id).replaceFirst("<ID2>", id2))

                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"name":"Mike", "id": "<ID>", "peopleILike":["<ID2>"]}
                          """.replaceFirst("<ID>", id).replaceFirst("<ID2>", id2)));

        mockMvc.perform(MockMvcRequestBuilders.get("/participants"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        {"name":"Mike", "id": "<ID>", "peopleILike":["<ID2>"]},
                        {"name":"Mary", "id": "<ID2>", "peopleWhoLikeMe":["<ID>"]}
                        ]
                        """.replaceAll("<ID>", id).replaceAll("<ID2>", id2)));
    }

    @DirtiesContext
    @Test
    void getMatches() throws Exception {

        String saveResult = mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mike"}
                                 """)
                )
                .andReturn()
                .getResponse()
                .getContentAsString();

        Participant saveResultParticipant = objectMapper.readValue(saveResult, Participant.class);
        String id = saveResultParticipant.getId();

        String saveResult2 = mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mary"}
                                 """)
                )
                .andReturn()
                .getResponse()
                .getContentAsString();

        Participant saveResultParticipant2 = objectMapper.readValue(saveResult2, Participant.class);
        String id2 = saveResultParticipant2.getId();


        String saveResult3 = mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Sara"}
                                 """)
                )
                .andReturn()
                .getResponse()
                .getContentAsString();

        Participant saveResultParticipant3 = objectMapper.readValue(saveResult, Participant.class);
        String id3 = saveResultParticipant3.getId();

        mockMvc.perform(put("/participants/likes/")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"likerID":"<ID>","likedPeopleIDs": ["<ID2>"]}
                                                             
                                 """.replaceFirst("<ID>", id).replaceFirst("<ID2>", id2))

                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"name":"Mike", "id": "<ID>", "peopleILike":["<ID2>"]}
                          """.replaceFirst("<ID>", id).replaceFirst("<ID2>", id2)));
        mockMvc.perform(put("/participants/likes/")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"likerID":"<ID2>","likedPeopleIDs": ["<ID>"]}
                                                             
                                 """.replaceFirst("<ID>", id).replaceFirst("<ID2>", id2))

                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"name":"Mary", "id": "<ID2>", "peopleILike":["<ID>"]}
                          """.replaceFirst("<ID>", id).replaceFirst("<ID2>", id2)));

        mockMvc.perform(get("/participants/likes/analysis"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        "Hi, Mike, here are names of people, with whom you have match. It's mutual, so don't hesitate writing them: Mary.","Hi, Mary, here are names of people, with whom you have match. It's mutual, so don't hesitate writing them: Mike.","Hi, Sara, unfortunately after today's event you don't have any matches. I'm sure, it's just a bad luck, so see you soon on one of the next events."]
                        """));
    }
}
