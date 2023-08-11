package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.PostItemRequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/test-schema.sql", "/data.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void create_shouldReturn200_whenCorrectRequest() throws Exception {
        PostItemRequestDto postItemRequestDto = PostItemRequestDto.builder()
                .description("хочу тирамису")
                .build();
        String requestBody = objectMapper.writeValueAsString(postItemRequestDto);

        mockMvc.perform(post("/requests")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 1)
                        .content(requestBody))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(2),
                        jsonPath("$.description").value("хочу тирамису")
                );
    }

    @Test
    void create_shouldReturn404_whenNorFoundUserId() throws Exception {
        PostItemRequestDto postItemRequestDto = PostItemRequestDto.builder()
                .description("хочу тирамису")
                .build();
        String requestBody = objectMapper.writeValueAsString(postItemRequestDto);

        mockMvc.perform(post("/requests")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 11234)
                        .content(requestBody))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void getByUserId_shouldReturn200_whenCorrectRequest() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[0].id").value(1),
                        jsonPath("$.[0].description").value("beze"),
                        jsonPath("$.[0].items[0].id").value(1)
                );
    }

    @Test
    void getByUserId_shouldReturn404_whenNotFoundUserId() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 11234))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void getById_shouldReturn200_whenCorrectRequest() throws Exception {
        mockMvc.perform(get("/requests/{requestId}", 1)
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 2))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.description").value("beze"),
                        jsonPath("$.items[0].id").value(1)
                );
    }

    @Test
    void getById_shouldReturn404_whenNotFoundUserId() throws Exception {
        mockMvc.perform(get("/requests/{requestId}", 1)
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 21234123))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void getById_shouldReturn404_whenNotFoundRequestId() throws Exception {
        mockMvc.perform(get("/requests/{requestId}", 112341)
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 1))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void getPage_shouldReturn200AndOtherUsersRequest_whenCorrectRequest() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 2))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[0].id").value(1),
                        jsonPath("$.[0].description").value("beze"),
                        jsonPath("$.[0].items[0].id").value(1)
                );

        mockMvc.perform(get("/requests/all")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 1))
                .andExpectAll(
                        status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void getPage_shouldReturn404_whenNotFoundUserId() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 112341))
                .andExpectAll(
                        status().isNotFound()
                );
    }
}
