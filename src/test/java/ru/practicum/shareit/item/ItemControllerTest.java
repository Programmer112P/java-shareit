package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/test-schema.sql", "/data.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void create_shouldReturn200_whenCorrectRequest() throws Exception {
        CreateItemDto createItemDto = CreateItemDto.builder()
                .name("new_item")
                .available(true)
                .description("new_item_desc")
                .requestId(1L)
                .build();
        String requestBody = objectMapper.writeValueAsString(createItemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isOk()
                );

        mockMvc.perform(get("/items/{itemId}", 4)
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(4),
                        jsonPath("$.available").value(true),
                        jsonPath("$.description").value("new_item_desc"),
                        jsonPath("$.name").value("new_item"),
                        jsonPath("$.requestId").value(1)
                );

        createItemDto = CreateItemDto.builder()
                .name("new_item_new")
                .available(true)
                .description("new_new")
                .build();
        requestBody = objectMapper.writeValueAsString(createItemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isOk()
                );

        mockMvc.perform(get("/items/{itemId}", 5)
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(5),
                        jsonPath("$.available").value(true),
                        jsonPath("$.description").value("new_new"),
                        jsonPath("$.name").value("new_item_new"),
                        jsonPath("$.requestId").isEmpty()
                );
    }

    @Test
    void create_shouldReturn404_whenNotFoundRequestId() throws Exception {
        CreateItemDto createItemDto = CreateItemDto.builder()
                .name("new_item")
                .available(true)
                .description("new_item_desc")
                .requestId(12345L)
                .build();
        String requestBody = objectMapper.writeValueAsString(createItemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void create_shouldReturn404_whenNotFoundUserId() throws Exception {
        CreateItemDto createItemDto = CreateItemDto.builder()
                .name("new_item")
                .available(true)
                .description("new_item_desc")
                .build();
        String requestBody = objectMapper.writeValueAsString(createItemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 112345)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void update_shouldReturn200_whenCorrectRequest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(2L)
                .available(false)
                .description("update_desc")
                .name("update_name")
                .requestId(1L)
                .build();
        String requestBody = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(patch("/items/{itemId}", 2)
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isOk()
                );

        mockMvc.perform(get("/items/{itemId}", 2)
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(2),
                        jsonPath("$.available").value(false),
                        jsonPath("$.description").value("update_desc"),
                        jsonPath("$.name").value("update_name"),
                        jsonPath("$.requestId").value(1)
                );
    }

    @Test
    void update_shouldReturn404_whenNotFoundRequestId() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .available(false)
                .description("update_desc")
                .name("update_name")
                .requestId(1231241L)
                .build();
        String requestBody = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(patch("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void update_shouldReturn404_whenNotFoundItemId() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .available(false)
                .description("update_desc")
                .name("update_name")
                .build();
        String requestBody = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(patch("/items/{itemId}", 1123433)
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void update_shouldReturn404_whenNotFoundUserId() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .available(false)
                .description("update_desc")
                .name("update_name")
                .build();
        String requestBody = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(patch("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1123415)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void getById_shouldReturn200_whenCorrectRequest() throws Exception {
        mockMvc.perform(get("/items/{itemId}", 3)
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(3),
                        jsonPath("$.available").value(true),
                        jsonPath("$.description").value("item3_description"),
                        jsonPath("$.name").value("item3_name"),
                        jsonPath("$.comments[0].text").value("item1_nice")
                );
    }

    @Test
    void getById_shouldReturn404_whenNotFoundItemId() throws Exception {
        mockMvc.perform(get("/items/{itemId}", 312341234)
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void getAllByUserId_shouldReturn200_whenCorrectId() throws Exception {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[0].id").value(1),
                        jsonPath("$.[0].available").value(true),
                        jsonPath("$.[0].description").value("item1_description"),
                        jsonPath("$.[0].name").value("item1_name"),
                        jsonPath("$.[0].requestId").value(1),
                        jsonPath("$.[1].id").value(2),
                        jsonPath("$.[1].available").value(false),
                        jsonPath("$.[1].description").value("item2_description"),
                        jsonPath("$.[1].name").value("item2_name")
                );
    }

    @Test
    void getAllByUserId_shouldReturn404_whenNotFoundUserId() throws Exception {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 11234)
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void search_shouldReturn200AndAvailableItems_whenCorrectRequest() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "ITEM")
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[0].id").value(1),
                        jsonPath("$.[1].id").value(3)
                );

        //when not available item
        mockMvc.perform(get("/items/search")
                        .param("text", "item2")
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void search_shouldReturn200AndEmptyList_whenBlankText() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "")
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void addComment_shouldReturn200_whenCorrectRequest() throws Exception {
        CreateCommentDto createCommentDto = CreateCommentDto.builder()
                .text("good")
                .build();
        String requestBody = objectMapper.writeValueAsString(createCommentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 3)
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isOk()
                );

        mockMvc.perform(get("/items/{itemId}", 3)
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(3),
                        jsonPath("$.available").value(true),
                        jsonPath("$.description").value("item3_description"),
                        jsonPath("$.name").value("item3_name"),
                        jsonPath("$.comments[0].text").value("item1_nice"),
                        jsonPath("$.comments[1].text").value("good"),
                        jsonPath("$.comments[1].authorName").value("user1")
                );
    }

    @Test
    void addComment_shouldReturn404_whenNotFoundUserId() throws Exception {
        CreateCommentDto createCommentDto = CreateCommentDto.builder()
                .text("good")
                .build();
        String requestBody = objectMapper.writeValueAsString(createCommentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 3)
                        .header("X-Sharer-User-Id", 112341)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void addComment_shouldReturn404_whenNotFoundItemId() throws Exception {
        CreateCommentDto createCommentDto = CreateCommentDto.builder()
                .text("good")
                .build();
        String requestBody = objectMapper.writeValueAsString(createCommentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 31234)
                        .header("X-Sharer-User-Id", 1)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void addComment_shouldReturn400_whenUserHasNotBookedItem() throws Exception {
        CreateCommentDto createCommentDto = CreateCommentDto.builder()
                .text("good")
                .build();
        String requestBody = objectMapper.writeValueAsString(createCommentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 3)
                        .header("X-Sharer-User-Id", 2)
                        .header("Content-Type", "application/json")
                        .content(requestBody))
                .andExpectAll(
                        status().isBadRequest()
                );
    }
}