package sonia.meetapp.app.participants;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
    @WithMockUser(username = "username")
    void getAllParticipants() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/participants"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        []
                        """));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void addParticipant() throws Exception {
        MvcResult result = mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mike"}
                                 """)
                        .with(csrf()))
                .andExpect(status().is(201))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("Mike"));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void deleteParticipant() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mike"}
                                 """)
                        .with(csrf())
                )
                .andExpect(status().is(201));

        mockMvc.perform(delete("/participants/" + "123")
                        .with(csrf()))
                .andExpect(status().is(204));

        mockMvc.perform(MockMvcRequestBuilders.get("/participants")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        []
                        """));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void deleteParticipantDoesNotExist() throws Exception {
        String id = "111";
        mockMvc.perform(MockMvcRequestBuilders.delete("/participants/" + id)
                        .with(csrf()))
                .andExpect(status().is(404));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void editParticipant() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mike", "email":"123@gmail.com"}
                                 """)
                        .with(csrf()))
                .andExpect(status().is(201));

        mockMvc.perform(put("/participants/edit/" + "123")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Nike", "email":"123@gmail.com"}
                                 """)
                        .with(csrf()))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/participants")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{"name":"Nike", "id": "123", "email":"123@gmail.com"}]
                          """));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void addLikes() throws Exception {

        String saveResult = mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mike", "email":"123@gmail.com"}
                                 """)
                        .with(csrf()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Participant saveResultParticipant = objectMapper.readValue(saveResult, Participant.class);
        String id = saveResultParticipant.getId();

        String saveResult2 = mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mary", "email":"12@gmail.com"}
                                 """)
                        .with(csrf()))
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

                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"name":"Mike", "id": "<ID>", "peopleILike":["<ID2>"]}
                          """.replaceFirst("<ID>", id).replaceFirst("<ID2>", id2)));

        mockMvc.perform(MockMvcRequestBuilders.get("/participants"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        {"name":"Mike", "id": "<ID>", "peopleILike":["<ID2>"], "email":"123@gmail.com"},
                        {"name":"Mary", "id": "<ID2>", "peopleWhoLikeMe":["<ID>"], "email":"12@gmail.com"}
                        ]
                        """.replaceAll("<ID>", id).replaceAll("<ID2>", id2)));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void getMatches() throws Exception {

        String saveResult = mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mike", "email":"123@gmail.com"}
                                 """)
                        .with(csrf()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Participant saveResultParticipant = objectMapper.readValue(saveResult, Participant.class);
        String id = saveResultParticipant.getId();

        String saveResult2 = mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Mary", "email":"12@gmail.com"}
                                 """)
                        .with(csrf()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Participant saveResultParticipant2 = objectMapper.readValue(saveResult2, Participant.class);
        String id2 = saveResultParticipant2.getId();


        String saveResult3 = mockMvc.perform(post("/participants")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"Sara", "email":"1@gmail.com"}
                                 """)
                        .with(csrf()))
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

                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"name":"Mike", "id": "<ID>", "peopleILike":["<ID2>"]}
                          """.replaceFirst("<ID>", id).replaceFirst("<ID2>", id2)));
        mockMvc.perform(put("/participants/likes/")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"likerID":"<ID2>","likedPeopleIDs": ["<ID>"]}
                                                             
                                 """.replaceFirst("<ID>", id).replaceFirst("<ID2>", id2))

                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"name":"Mary", "id": "<ID2>", "peopleILike":["<ID>"]}
                          """.replaceFirst("<ID>", id).replaceFirst("<ID2>", id2)));

        mockMvc.perform(get("/participants/likes/analysis/" + "<ID>".replaceFirst("<ID>", id)))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        ["Mary"]
                        """));
    }

    @Test
    void unauthorized() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "username")
    void getTestWithUsername() throws Exception {
        mockMvc.perform(get("/hello/login"))
                .andExpect(content().string("username"));
    }

}
