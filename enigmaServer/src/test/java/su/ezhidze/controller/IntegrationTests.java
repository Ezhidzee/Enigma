package su.ezhidze.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import su.ezhidze.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createUser() throws Exception {
        UserRegistrationModel userRegistrationModel = new UserRegistrationModel("testUser", "+76546489412", "", "Qwerty1#");
        ResultActions register = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userRegistrationModel)));
        register.andExpect(status().isOk());
        Integer id = JsonPath.read(register.andReturn().getResponse().getContentAsString(), "$.id");

        AuthenticationModel authenticationModel = new AuthenticationModel("testUser", "Qwerty1#", "123");
        ResultActions authenticate = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationModel)));
        authenticate.andExpect(status().isOk());
        String token = JsonPath.read(authenticate.andReturn().getResponse().getContentAsString(), "$.token");

        ResultActions delete = mockMvc.perform(MockMvcRequestBuilders.delete("/enigma/delete")
                .param("id", String.valueOf(id))
                .header("Authorization", "Bearer " + token));
        delete.andExpect(status().isAccepted());
    }

    @Test
    void patchUser() throws Exception {
        UserRegistrationModel userRegistrationModel = new UserRegistrationModel("testUser", "+76546489412", "", "Qwerty1#");
        ResultActions register = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userRegistrationModel)));
        register.andExpect(status().isOk());
        Integer id = JsonPath.read(register.andReturn().getResponse().getContentAsString(), "$.id");

        AuthenticationModel authenticationModel = new AuthenticationModel("testUser", "Qwerty1#", "123");
        ResultActions authenticate = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationModel)));
        authenticate.andExpect(status().isOk());
        String token = JsonPath.read(authenticate.andReturn().getResponse().getContentAsString(), "$.token");

        Map<String, Object> fields = new HashMap<>();
        fields.put("nickname", "testUser2");
        ResultActions patch = mockMvc.perform(MockMvcRequestBuilders.patch("/enigma/patch")
                .param("id", String.valueOf(id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fields))
                .header("Authorization", "Bearer " + token));
        patch
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("testUser2"));

        ResultActions delete = mockMvc.perform(MockMvcRequestBuilders.delete("/enigma/delete")
                .param("id", String.valueOf(id))
                .header("Authorization", "Bearer " + token));
        delete.andExpect(status().isAccepted());
    }

    @Test
    void createChatAndAddUsers() throws Exception {
        UserRegistrationModel user1RegistrationModel = new UserRegistrationModel("testUser", "+76546489412", "", "Qwerty1#");
        ResultActions register = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user1RegistrationModel)));
        register.andExpect(status().isOk());
        Integer id1 = JsonPath.read(register.andReturn().getResponse().getContentAsString(), "$.id");
        AuthenticationModel authenticationModel1 = new AuthenticationModel("testUser", "Qwerty1#", "123");
        ResultActions authenticate1 = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationModel1)));
        authenticate1.andExpect(status().isOk());
        String token1 = JsonPath.read(authenticate1.andReturn().getResponse().getContentAsString(), "$.token");

        UserRegistrationModel user2RegistrationModel = new UserRegistrationModel("testUser2", "+76546489412", "", "Qwerty1#");
        ResultActions register2 = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user2RegistrationModel)));
        register2.andExpect(status().isOk());
        Integer id2 = JsonPath.read(register2.andReturn().getResponse().getContentAsString(), "$.id");
        AuthenticationModel authenticationModel2 = new AuthenticationModel("testUser", "Qwerty1#", "123");
        ResultActions authenticate2 = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationModel2)));
        authenticate2.andExpect(status().isOk());
        String token2 = JsonPath.read(authenticate2.andReturn().getResponse().getContentAsString(), "$.token");

        ResultActions createChat = mockMvc.perform(MockMvcRequestBuilders.post("/addChat")
                .header("Authorization", "Bearer " + token1));
        createChat.andExpect(status().isOk());
        Integer chatId = JsonPath.read(createChat.andReturn().getResponse().getContentAsString(), "$.id");

        ResultActions joinUser1 = mockMvc.perform(MockMvcRequestBuilders.put("/joinUser")
                .param("chatId", String.valueOf(chatId))
                .param("userId", String.valueOf(id1))
                .header("Authorization", "Bearer " + token1));
        joinUser1.andExpect(status().isOk());
        ResultActions joinUser2 = mockMvc.perform(MockMvcRequestBuilders.put("/joinUser")
                .param("chatId", String.valueOf(chatId))
                .param("userId", String.valueOf(id2))
                .header("Authorization", "Bearer " + token1));
        joinUser2.andExpect(status().isOk());
        List<UserResponseModel> users = JsonPath.read(joinUser2.andReturn().getResponse().getContentAsString(), "$.users");
        assertThat(users.size(), equalTo(2));
        Integer responseId1 = JsonPath.read(joinUser2.andReturn().getResponse().getContentAsString(), "$.users[0].id");
        Integer responseId2 = JsonPath.read(joinUser2.andReturn().getResponse().getContentAsString(), "$.users[1].id");
        boolean isFoundUser1 = responseId1.equals(id1) || responseId2.equals(id1);
        assertThat(isFoundUser1, equalTo(true));
        boolean isFoundUser2 = responseId1.equals(id2) || responseId2.equals(id2);
        assertThat(isFoundUser2, equalTo(true));

        ResultActions deleteChat = mockMvc.perform(MockMvcRequestBuilders.delete("/deleteChat")
                .param("id", String.valueOf(chatId))
                .header("Authorization", "Bearer " + token1));
        deleteChat.andExpect(status().isOk());

        ResultActions deleteUser1 = mockMvc.perform(MockMvcRequestBuilders.delete("/enigma/delete")
                .param("id", String.valueOf(id1))
                .header("Authorization", "Bearer " + token1));
        deleteUser1.andExpect(status().isAccepted());
        ResultActions deleteUser2 = mockMvc.perform(MockMvcRequestBuilders.delete("/enigma/delete")
                .param("id", String.valueOf(id2))
                .header("Authorization", "Bearer " + token2));
        deleteUser2.andExpect(status().isAccepted());
    }

    @Test
    void deleteChat() throws Exception {
        UserRegistrationModel user1RegistrationModel = new UserRegistrationModel("testUser", "+76546489412", "", "Qwerty1#");
        ResultActions register = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user1RegistrationModel)));
        register.andExpect(status().isOk());
        Integer id1 = JsonPath.read(register.andReturn().getResponse().getContentAsString(), "$.id");
        AuthenticationModel authenticationModel1 = new AuthenticationModel("testUser", "Qwerty1#", "123");
        ResultActions authenticate1 = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationModel1)));
        authenticate1.andExpect(status().isOk());
        String token1 = JsonPath.read(authenticate1.andReturn().getResponse().getContentAsString(), "$.token");

        UserRegistrationModel user2RegistrationModel = new UserRegistrationModel("testUser2", "+76546489412", "", "Qwerty1#");
        ResultActions register2 = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user2RegistrationModel)));
        register2.andExpect(status().isOk());
        Integer id2 = JsonPath.read(register2.andReturn().getResponse().getContentAsString(), "$.id");
        AuthenticationModel authenticationModel2 = new AuthenticationModel("testUser", "Qwerty1#", "123");
        ResultActions authenticate2 = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationModel2)));
        authenticate2.andExpect(status().isOk());
        String token2 = JsonPath.read(authenticate2.andReturn().getResponse().getContentAsString(), "$.token");

        ResultActions createChat = mockMvc.perform(MockMvcRequestBuilders.post("/addChat")
                .header("Authorization", "Bearer " + token1));
        createChat.andExpect(status().isOk());
        Integer chatId = JsonPath.read(createChat.andReturn().getResponse().getContentAsString(), "$.id");

        ResultActions joinUser1 = mockMvc.perform(MockMvcRequestBuilders.put("/joinUser")
                .param("chatId", String.valueOf(chatId))
                .param("userId", String.valueOf(id1))
                .header("Authorization", "Bearer " + token1));
        joinUser1.andExpect(status().isOk());
        ResultActions joinUser2 = mockMvc.perform(MockMvcRequestBuilders.put("/joinUser")
                .param("chatId", String.valueOf(chatId))
                .param("userId", String.valueOf(id2))
                .header("Authorization", "Bearer " + token1));
        joinUser2.andExpect(status().isOk());

        ResultActions deleteChat = mockMvc.perform(MockMvcRequestBuilders.delete("/deleteChat")
                .param("id", String.valueOf(chatId))
                .header("Authorization", "Bearer " + token1));
        deleteChat.andExpect(status().isOk());

        ResultActions getUserChats = mockMvc.perform(MockMvcRequestBuilders.get("/enigma/getUserChats")
                .param("userId", String.valueOf(id1))
                .header("Authorization", "Bearer " + token1));
        getUserChats.andExpect(status().isOk());
        ArrayList<ChatModel> userChats = JsonPath.read(getUserChats.andReturn().getResponse().getContentAsString(), "$");
        assertThat(userChats.size(), equalTo(0));

        ResultActions delete1 = mockMvc.perform(MockMvcRequestBuilders.delete("/enigma/delete")
                .param("id", String.valueOf(id1))
                .header("Authorization", "Bearer " + token1));
        delete1.andExpect(status().isAccepted());
        ResultActions delete2 = mockMvc.perform(MockMvcRequestBuilders.delete("/enigma/delete")
                .param("id", String.valueOf(id2))
                .header("Authorization", "Bearer " + token2));
        delete2.andExpect(status().isAccepted());
    }

    @Test
    void deleteUserChats() throws Exception {
        UserRegistrationModel user1RegistrationModel = new UserRegistrationModel("testUser", "+76546489412", "", "Qwerty1#");
        ResultActions register = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user1RegistrationModel)));
        register.andExpect(status().isOk());
        Integer id1 = JsonPath.read(register.andReturn().getResponse().getContentAsString(), "$.id");
        AuthenticationModel authenticationModel1 = new AuthenticationModel("testUser", "Qwerty1#", "123");
        ResultActions authenticate1 = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationModel1)));
        authenticate1.andExpect(status().isOk());
        String token1 = JsonPath.read(authenticate1.andReturn().getResponse().getContentAsString(), "$.token");

        UserRegistrationModel user2RegistrationModel = new UserRegistrationModel("testUser2", "+76546489412", "", "Qwerty1#");
        ResultActions register2 = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user2RegistrationModel)));
        register2.andExpect(status().isOk());
        Integer id2 = JsonPath.read(register2.andReturn().getResponse().getContentAsString(), "$.id");
        AuthenticationModel authenticationModel2 = new AuthenticationModel("testUser", "Qwerty1#", "123");
        ResultActions authenticate2 = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationModel2)));
        authenticate2.andExpect(status().isOk());
        String token2 = JsonPath.read(authenticate2.andReturn().getResponse().getContentAsString(), "$.token");

        ResultActions createChat = mockMvc.perform(MockMvcRequestBuilders.post("/addChat")
                .header("Authorization", "Bearer " + token1));
        createChat.andExpect(status().isOk());
        Integer chatId = JsonPath.read(createChat.andReturn().getResponse().getContentAsString(), "$.id");

        ResultActions joinUser1 = mockMvc.perform(MockMvcRequestBuilders.put("/joinUser")
                .param("chatId", String.valueOf(chatId)).param("userId", String.valueOf(id1))
                .header("Authorization", "Bearer " + token1));
        joinUser1.andExpect(status().isOk());
        ResultActions joinUser2 = mockMvc.perform(MockMvcRequestBuilders.put("/joinUser")
                .param("chatId", String.valueOf(chatId)).param("userId", String.valueOf(id2))
                .header("Authorization", "Bearer " + token2));
        joinUser2.andExpect(status().isOk());
        List<UserResponseModel> users = JsonPath.read(joinUser2.andReturn().getResponse().getContentAsString(), "$.users");
        assertThat(users.size(), equalTo(2));

        ResultActions deleteUserChats = mockMvc.perform(MockMvcRequestBuilders.delete("/deleteUserChats")
                .param("id", String.valueOf(id1))
                .header("Authorization", "Bearer " + token1));
        deleteUserChats.andExpect(status().isOk());

        ResultActions getUserChats = mockMvc.perform(MockMvcRequestBuilders.get("/enigma/getUserChats")
                .param("userId", String.valueOf(id2))
                .header("Authorization", "Bearer " + token2));
        getUserChats.andExpect(status().isOk());
        ArrayList<ChatModel> user2Chats = JsonPath.read(getUserChats.andReturn().getResponse().getContentAsString(), "$");
        assertThat(user2Chats.size(), equalTo(0));

        ResultActions delete1 = mockMvc.perform(MockMvcRequestBuilders.delete("/enigma/delete")
                .param("id", String.valueOf(id1))
                .header("Authorization", "Bearer " + token1));
        delete1.andExpect(status().isAccepted());
        ResultActions delete2 = mockMvc.perform(MockMvcRequestBuilders.delete("/enigma/delete")
                .param("id", String.valueOf(id2))
                .header("Authorization", "Bearer " + token2));
        delete2.andExpect(status().isAccepted());
    }

    @Test
    void signOutUser() throws Exception {
        UserRegistrationModel userRegistrationModel = new UserRegistrationModel("testUser", "+76546489412", "", "Qwerty1#");
        ResultActions register = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userRegistrationModel)));
        register.andExpect(status().isOk());
        Integer id = JsonPath.read(register.andReturn().getResponse().getContentAsString(), "$.id");

        AuthenticationModel authenticationModel = new AuthenticationModel("testUser", "Qwerty1#", "123");
        ResultActions authenticate = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationModel)));
        authenticate.andExpect(status().isOk());
        String token = JsonPath.read(authenticate.andReturn().getResponse().getContentAsString(), "$.token");

        ResultActions signOutUser = mockMvc.perform(MockMvcRequestBuilders.post("/enigma/signOutUser")
                .param("id", String.valueOf(id))
                .header("Authorization", "Bearer " + token));
        signOutUser.andExpect(status().isOk());

        ResultActions getUserById = mockMvc.perform(MockMvcRequestBuilders.get("/enigma")
                .param("id", String.valueOf(id))
                .header("Authorization", "Bearer " + token));
        getUserById.andExpect(status().isOk());

        assertThat(JsonPath.read(getUserById.andReturn().getResponse().getContentAsString(), "$.publicKey"), equalTo(null));

        ResultActions delete = mockMvc.perform(MockMvcRequestBuilders.delete("/enigma/delete")
                .param("id", String.valueOf(id))
                .header("Authorization", "Bearer " + token));
        delete.andExpect(status().isAccepted());
    }
}