package sonia.meetapp.app.integration;

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
import sonia.meetapp.app.participants.Utility;

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
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        mockMvc.perform(MockMvcRequestBuilders.get("/participants/123")).andExpect(status().isOk()).andExpect(content().json("""
                []
                """));
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void addParticipantEventNotFound() throws Exception {
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike"}
                 """).with(csrf())).andExpect(status().is(404));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void addParticipantNameIsNotUnique() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike"}
                 """).with(csrf())).andExpect(status().is(201));
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike"}
                 """).with(csrf())).andExpect(status().is(400));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void addParticipantEmailIsNotUnique() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike", "email": "mike@gmail.com"}
                 """).with(csrf())).andExpect(status().is(201));
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mary", "email": "mike@gmail.com"}
                 """).with(csrf())).andExpect(status().is(403));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void deleteParticipant() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        given(utility.createIdAsString()).willReturn("124");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike"}
                 """).with(csrf())).andExpect(status().is(201));

        mockMvc.perform(delete("/participants/" + "123/" + "124").with(csrf())).andExpect(status().is(204));

        mockMvc.perform(MockMvcRequestBuilders.get("/participants/123").with(csrf())).andExpect(status().isOk()).andExpect(content().json("""
                []
                """));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void deleteParticipantDoesNotExist() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        String id = "111";
        mockMvc.perform(MockMvcRequestBuilders.delete("/participants/123/" + id).with(csrf())).andExpect(status().is(404));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void editParticipant() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        given(utility.createIdAsString()).willReturn("124");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike", "email":"123@gmail.com"}
                 """).with(csrf())).andExpect(status().is(201));

        mockMvc.perform(put("/participants/edit/123/124").contentType(APPLICATION_JSON).content("""
                {"name":"Nike", "email":"123@gmail.com"}
                 """).with(csrf())).andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/participants/123").with(csrf())).andExpect(status().isOk()).andExpect(content().json("""
                [{"name":"Nike", "id": "124", "email":"123@gmail.com"}]
                  """));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void addLikes() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        given(utility.createIdAsString()).willReturn("1");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike", "email":"123@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("2");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mary", "email":"12@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        mockMvc.perform(put("/participants/likes/123").contentType(APPLICATION_JSON).content("""
                {"likerID":"1","likedPeopleIDs": ["2"]}
                 """).with(csrf())).andExpect(status().isOk()).andExpect(content().json("""
                {"name":"Mike", "id": "1", "peopleILike":["2"]}
                  """));

        mockMvc.perform(MockMvcRequestBuilders.get("/participants/123")).andExpect(status().isOk()).andExpect(content().json("""
                [
                {"name":"Mike", "id": "1", "peopleILike":["2"], "email":"123@gmail.com"},
                {"name":"Mary", "id": "2", "peopleWhoLikeMe":["1"], "email":"12@gmail.com"}
                ]
                """));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void getMatches() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        given(utility.createIdAsString()).willReturn("1");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike", "email":"123@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("2");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mary", "email":"12@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        mockMvc.perform(put("/participants/likes/123").contentType(APPLICATION_JSON).content("""
                {"likerID":"1","likedPeopleIDs": ["2"]}
                                             
                 """).with(csrf())).andExpect(status().isOk()).andExpect(content().json("""
                {"name":"Mike", "id": "1", "peopleILike":["2"]}
                  """));

        mockMvc.perform(put("/participants/likes/123").contentType(APPLICATION_JSON).content("""
                {"likerID":"2","likedPeopleIDs": ["1"]}
                                             
                 """).with(csrf())).andExpect(status().isOk()).andExpect(content().json("""
                {"name":"Mary", "id": "2", "peopleILike":["1"]}
                  """));

        mockMvc.perform(get("/participants/likes/analysis/123/" + "1")).andExpect(status().isOk()).andExpect(content().json("""
                [{"name":"Mary","id":"2","peopleILike":["1"],"peopleWhoLikeMe":["1"],"email":"12@gmail.com"}]
                """));
    }

    @Test
    void unauthorized() throws Exception {
        mockMvc.perform(get("/hello")).andExpect(status().is4xxClientError());
    }

    @DirtiesContext
    @Test
    void createAccount() throws Exception {
        MvcResult result = mockMvc.perform(post("/hello").contentType(APPLICATION_JSON).content("""
                {"email": "abcde@gmail.com",
                               "password": "aaaaaa",
                               "repeatPassword":"aaaaaa",
                               "contacts":"insta"
                               }
                 """).with(csrf())).andExpect(status().is(201)).andReturn();
        String content = result.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("abcde@gmail.com"));
    }

    @DirtiesContext
    @Test
    void createAccountEmailAlreadyExists() throws Exception {
        mockMvc.perform(post("/hello").contentType(APPLICATION_JSON).content("""
                {"email": "abcde@gmail.com",
                               "password": "aaaaaa",
                               "repeatPassword":"aaaaaa",
                               "contacts":"insta"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        mockMvc.perform(post("/hello").contentType(APPLICATION_JSON).content("""
                {"email": "abcde@gmail.com",
                               "password": "bbbbbb",
                               "repeatPassword":"bbbbbb",
                               "contacts":"telegram"
                               }
                 """).with(csrf())).andExpect(status().is(403));
    }

    @DirtiesContext
    @Test
    void createAccountPasswordDoesNotMatch() throws Exception {
        mockMvc.perform(post("/hello").contentType(APPLICATION_JSON).content("""
                {"email": "abcde@gmail.com",
                               "password": "aaaaaa",
                               "repeatPassword":"bbbbbb",
                               "contacts":"insta"
                               }
                 """).with(csrf())).andExpect(status().is(403));
    }

    @DirtiesContext
    @Test
    void createAccountConstraintsViolation() throws Exception {
        mockMvc.perform(post("/hello").contentType(APPLICATION_JSON).content("""
                {"email": "abcdegmail.com",
                               "password": "aaaaaa",
                               "repeatPassword":"aaaaaa",
                               "contacts":"insta"
                               }
                 """).with(csrf())).andExpect(status().is(400));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void getAllUsers() throws Exception {
        mockMvc.perform(post("/hello").contentType(APPLICATION_JSON).content("""
                {"email": "testUser1@gmail.com",
                               "password": "aaaaaa",
                               "repeatPassword":"aaaaaa",
                               "contacts":"telegram"
                               }
                 """).with(csrf())).andExpect(status().is(201));
        mockMvc.perform(post("/hello").contentType(APPLICATION_JSON).content("""
                {"email": "testUser2@gmail.com",
                               "password": "aaaaaa",
                               "repeatPassword":"aaaaaa",
                               "contacts":"insta"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/hello/findUsers")).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("testUser1@gmail.com"));
        Assertions.assertTrue(content.contains("testUser2@gmail.com"));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username@gmail.com")
    void logintest() throws Exception {

        mockMvc.perform(post("/hello").contentType(APPLICATION_JSON).content("""
                {"email": "username@gmail.com",
                               "password": "aaaaaa",
                               "repeatPassword":"aaaaaa",
                               "contacts":"telegram"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        MvcResult result = mockMvc.perform(get("/hello/login")).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("username"));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username@gmail.com")
    void logouttest() throws Exception {
        mockMvc.perform(get("/hello/logout")).andExpect(status().isOk());

        mockMvc.perform(get("/hello/login")).andExpect(status().is(404));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username@gmail.com")
    void createPairsOnce() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        given(utility.createIdAsString()).willReturn("1");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike", "email":"1@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("2");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mary", "email":"2@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("3");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Misha", "email":"3@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("4");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mark", "email":"4@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        mockMvc.perform(get("/participants/pairs/123")).andExpect(status().isOk()).andExpect(content().json("""
                                 [
                                [{"name":"Mike","id":"1","email":"1@gmail.com"},
                {"name":"Mary","id":"2","email":"2@gmail.com"}],
                                [{"name":"Misha","id":"3","email":"3@gmail.com"},
                {"name":"Mark","id":"4","email":"4@gmail.com"}]
                                ]
                """));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username@gmail.com")
    void createPairsSeveralTimes() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        given(utility.createIdAsString()).willReturn("1");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike", "email":"1@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("2");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mary", "email":"2@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("3");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Misha", "email":"3@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("4");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mark", "email":"4@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        mockMvc.perform(get("/participants/pairs/123")).andExpect(status().isOk()).andExpect(content().json("""
                                 [
                                [{"name":"Mike","id":"1","email":"1@gmail.com", "peopleITalkedTo":["2"]},
                {"name":"Mary","id":"2","email":"2@gmail.com", "peopleITalkedTo":["1"]}],
                                [{"name":"Misha","id":"3","email":"3@gmail.com", "peopleITalkedTo":["4"]},
                {"name":"Mark","id":"4","email":"4@gmail.com", "peopleITalkedTo":["3"]}]
                                ]
                """));

        mockMvc.perform(get("/participants/pairs/123")).andExpect(status().isOk()).andExpect(content().json("""
                [
                    [
                        {"name":"Mike","id":"1","email":"1@gmail.com", "peopleITalkedTo":["2", "3"]},
                        {"name":"Misha","id":"3","email":"3@gmail.com", "peopleITalkedTo":["4", "1"]}
                    ],
                    [
                        {"name":"Mary","id":"2","email":"2@gmail.com", "peopleITalkedTo":["1", "4"]},
                        {"name":"Mark","id":"4","email":"4@gmail.com", "peopleITalkedTo":["3", "2"]}
                    ]
                ]
                """));

        mockMvc.perform(get("/participants/pairs/123")).andExpect(status().isOk()).andExpect(content().json("""
                [
                    [
                        {"name":"Mike","id":"1","email":"1@gmail.com", "peopleITalkedTo":["2", "3", "4"]},
                        {"name":"Mark","id":"4","email":"4@gmail.com", "peopleITalkedTo":["3","2", "1"]}
                    ],
                    [
                        {"name":"Mary","id":"2","email":"2@gmail.com", "peopleITalkedTo":["1", "4", "3"]},
                        {"name":"Misha","id":"3","email":"3@gmail.com", "peopleITalkedTo":["4", "1", "2"]}
                    ]
                ]
                """));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username@gmail.com")
    void createPairsSeveralTimesTillNoPossibleCombination() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        given(utility.createIdAsString()).willReturn("1");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike", "email":"1@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("2");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mary", "email":"2@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("3");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Misha", "email":"3@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("4");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mark", "email":"4@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        mockMvc.perform(get("/participants/pairs/123")).andExpect(status().isOk()).andExpect(content().json("""
                                 [
                                [{"name":"Mike","id":"1","email":"1@gmail.com", "peopleITalkedTo":["2"]},
                {"name":"Mary","id":"2","email":"2@gmail.com", "peopleITalkedTo":["1"]}],
                                [{"name":"Misha","id":"3","email":"3@gmail.com", "peopleITalkedTo":["4"]},
                {"name":"Mark","id":"4","email":"4@gmail.com", "peopleITalkedTo":["3"]}]
                                ]
                """));

        mockMvc.perform(get("/participants/pairs/123")).andExpect(status().isOk()).andExpect(content().json("""
                [
                    [
                        {"name":"Mike","id":"1","email":"1@gmail.com", "peopleITalkedTo":["2", "3"]},
                        {"name":"Misha","id":"3","email":"3@gmail.com", "peopleITalkedTo":["4", "1"]}
                    ],
                    [
                        {"name":"Mary","id":"2","email":"2@gmail.com", "peopleITalkedTo":["1", "4"]},
                        {"name":"Mark","id":"4","email":"4@gmail.com", "peopleITalkedTo":["3", "2"]}
                    ]
                ]
                """));

        mockMvc.perform(get("/participants/pairs/123")).andExpect(status().isOk()).andExpect(content().json("""
                [
                    [
                        {"name":"Mike","id":"1","email":"1@gmail.com", "peopleITalkedTo":["2", "3", "4"]},
                        {"name":"Mark","id":"4","email":"4@gmail.com", "peopleITalkedTo":["3","2", "1"]}
                    ],
                    [
                        {"name":"Mary","id":"2","email":"2@gmail.com", "peopleITalkedTo":["1", "4", "3"]},
                        {"name":"Misha","id":"3","email":"3@gmail.com", "peopleITalkedTo":["4", "1", "2"]}
                    ]
                ]
                """));
        mockMvc.perform(get("/participants/pairs/123")).andExpect(status().is(417));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username@gmail.com")
    void createPairsOddNumberOfParticipants() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        given(utility.createIdAsString()).willReturn("1");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike", "email":"1@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("2");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mary", "email":"2@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("3");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Misha", "email":"3@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        mockMvc.perform(get("/participants/pairs/123")).andExpect(status().isOk()).andExpect(content().json("""
                                 [
                                [{"name":"Mike","id":"1","email":"1@gmail.com"},
                {"name":"Mary","id":"2","email":"2@gmail.com"}],
                                [{"name":"Misha","id":"3","email":"3@gmail.com"},
                {"name":"break","id":"break","email":"break"}]
                                ]
                """));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username@gmail.com")
    void createPairsOddNumberOfParticipantsWithLatecomer() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        given(utility.createIdAsString()).willReturn("1");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike", "email":"1@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("2");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mary", "email":"2@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("3");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Misha", "email":"3@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        mockMvc.perform(get("/participants/pairs/123")).andExpect(status().isOk()).andExpect(content().json("""
                                 [
                                [{"name":"Mike","id":"1","email":"1@gmail.com"},
                {"name":"Mary","id":"2","email":"2@gmail.com"}],
                                [{"name":"Misha","id":"3","email":"3@gmail.com"},
                {"name":"break","id":"break","email":"break"}]
                                ]
                """));

        given(utility.createIdAsString()).willReturn("4");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mark", "email":"4@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        mockMvc.perform(get("/participants/pairs/123")).andExpect(status().isOk()).andExpect(content().json("""
                [
                    [
                        {"name":"Mike","id":"1","email":"1@gmail.com", "peopleITalkedTo":["2", "3"]},
                        {"name":"Misha","id":"3","email":"3@gmail.com", "peopleITalkedTo":["break", "1"]}
                    ],
                    [
                        {"name":"Mary","id":"2","email":"2@gmail.com", "peopleITalkedTo":["1", "4"]},
                        {"name":"Mark","id":"4","email":"4@gmail.com", "peopleITalkedTo":["2"]}
                    ]
                ]
                """));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void getAllEvents() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/events")).andExpect(status().isOk()).andExpect(content().json("""
                []
                """));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void addEvent() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201)).andExpect(content().json("""
                {"id":"123",
                "name": "speed-friending outdoors",
                "place": "park",
                "time":"2022-09-13T09:04:33.089Z",
                "description":"lovely event"
                } 
                """));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "username")
    void receiveCurrentRound() throws Exception {
        given(utility.createIdAsString()).willReturn("123");
        mockMvc.perform(post("/events").contentType(APPLICATION_JSON).content("""
                {"name": "speed-friending outdoors",
                               "place": "park",
                               "time":"2022-09-13T09:04:33.089Z",
                               "description":"lovely event"
                               }
                 """).with(csrf())).andExpect(status().is(201));

        given(utility.createIdAsString()).willReturn("1");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mike", "email":"1@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("2");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mary", "email":"2@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("3");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Misha", "email":"3@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        given(utility.createIdAsString()).willReturn("4");
        mockMvc.perform(post("/participants/123").contentType(APPLICATION_JSON).content("""
                {"name":"Mark", "email":"4@gmail.com"}
                 """).with(csrf())).andReturn().getResponse();

        mockMvc.perform(get("/participants/pairs/123")).andExpect(status().isOk()).andExpect(content().json("""
                                 [
                                [{"name":"Mike","id":"1","email":"1@gmail.com"},
                {"name":"Mary","id":"2","email":"2@gmail.com"}],
                                [{"name":"Misha","id":"3","email":"3@gmail.com"},
                {"name":"Mark","id":"4","email":"4@gmail.com"}]
                                ]
                """));

        mockMvc.perform(get("/participants/pairs/123/currentRound")).andExpect(status().isOk()).andExpect(content().json("""
                                 [
                                [{"name":"Mike","id":"1","email":"1@gmail.com"},
                {"name":"Mary","id":"2","email":"2@gmail.com"}],
                                [{"name":"Misha","id":"3","email":"3@gmail.com"},
                {"name":"Mark","id":"4","email":"4@gmail.com"}]
                                ]
                                  """));
    }
}
