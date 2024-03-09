package com.taskMangementService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskMangementService.model.dto.UserDto;
import com.taskMangementService.model.entities.User;
import com.taskMangementService.model.response.LoginResponse;
import com.taskMangementService.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testRegisterUser() throws Exception {
        UserDto userDto = new UserDto("testUser", "testPassword", User.Role.USER);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User registered successfully"));
    }
    @Test
    void testRegisterUserUnAuthenticated() throws Exception {
        UserDto userDto = new UserDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testRegisterUserNotEligible() throws Exception {
        UserDto userDto = new UserDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testAuthenticate() throws Exception {
        UserDto userDto = new UserDto();

        LoginResponse loginResponse = LoginResponse.builder()
                .loginStatus(LoginResponse.LoginStatus.AUTHORIZED)
                .token("JWT").build();

        when(userService.authenticate(any(UserDto.class))).thenReturn(loginResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.loginStatus").value(LoginResponse.LoginStatus.AUTHORIZED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("JWT"));
    }

}
