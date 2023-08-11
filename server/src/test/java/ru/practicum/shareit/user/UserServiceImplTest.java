package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/test-schema.sql", "/data.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceImplTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void create_shouldReturn200_whenCorrectRequest() throws Exception {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("user_name")
                .email("user4@email.ru")
                .build();

        String requestBody = objectMapper.writeValueAsString(createUserDto);

        mockMvc.perform(post("/users")
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(4),
                        jsonPath("$.name").value("user_name"),
                        jsonPath("$.email").value("user4@email.ru")
                );
    }

    @Test
    void create_shouldReturn409_whenEmailDuplicate() throws Exception {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("user_name")
                .email("user1@email.com")
                .build();

        String requestBody = objectMapper.writeValueAsString(createUserDto);

        mockMvc.perform(post("/users")
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isConflict()
                );
    }


    @Test
    void delete_shouldReturn200_whenCorrectRequest() throws Exception {
        mockMvc.perform(delete("/users/{userId}", 1L)
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    void delete_shouldReturn404_whenNotFoundUser() throws Exception {
        mockMvc.perform(delete("/users/{userId}", 11233L)
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void update_shouldReturn200_whenCorrectRequest() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("new@email.ru")
                .name("new_name")
                .build();
        String requestBody = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(patch("/users/{userId}", 1)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isOk()
                );

        mockMvc.perform(get("/users/{userId}", 1)
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isOk(),
                        content().json(requestBody));
    }

    @Test
    void update_shouldReturn404_whenNotFoundUser() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("new@email.ru")
                .name("new_name")
                .build();
        String requestBody = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(patch("/users/{userId}", 11233)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void update_shouldReturn400_whenNotValidEmail() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("bad")
                .name("new_name")
                .build();
        String requestBody = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(patch("/users/{userId}", 1)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    void update_shouldReturn409_whenDuplicateEmail() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("user2@email.com")
                .name("new_name")
                .build();
        String requestBody = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(patch("/users/{userId}", 1)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isConflict()
                );
    }

    @Test
    void getById_ShouldReturn200_whenCorrectId() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("user1@email.com")
                .name("user1")
                .build();
        String response = objectMapper.writeValueAsString(userDto);


        mockMvc.perform(get("/users/{userId}", 1)
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isOk(),
                        content().json(response));
    }

    @Test
    void getById_ShouldReturn404_whenNotFoundUserId() throws Exception {
        mockMvc.perform(get("/users/{userId}", 11231231)
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void getAll_shouldReturnCorrectList_whenRequestAll() throws Exception {
        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .email("user1@email.com")
                .name("user1")
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(2L)
                .email("user2@email.com")
                .name("user2")
                .build();
        UserDto userDto3 = UserDto.builder()
                .id(3L)
                .email("user3@email.com")
                .name("user3")
                .build();
        List<UserDto> users = List.of(userDto1, userDto2, userDto3);
        String response = objectMapper.writeValueAsString(users);

        mockMvc.perform(get("/users")
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isOk(),
                        content().json(response)
                );
    }
}
