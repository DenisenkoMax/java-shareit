package ru.practicum.shareit.itemTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoAnswer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @MockBean
    ItemService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createItemStatus200Test() throws Exception {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto(null, "item1", "item1", true, null);
        ItemDto itemDtoAnswer = new ItemDto(1L, "item1", "item1", true,
                userId);

        when(service.create(itemDto, userId)).thenReturn(Optional.of(itemDtoAnswer));

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemDtoAnswer.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoAnswer.getDescription())));
        verify(service, Mockito.times(1)).create(itemDto, 1L);
    }

    @Test
    public void createItemNoUserStatus400() throws Exception {
        ItemDto itemDto = new ItemDto(null, "item1", "item1", true, null);

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createItemErrorUserIdStatus400() throws Exception {
        Long userId = 15L;
        ItemDto itemDto = new ItemDto(null, "item1", "item1", true, null);

        when(service.create(itemDto, userId))
                .thenThrow(new NotFoundEx("User not found"));

        mockMvc.perform(post("/items").content(objectMapper.writeValueAsString(itemDto))
                .header("X-Sharer-User-Id", userId).characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

        verify(service, Mockito.times(1)).create(itemDto, userId);
    }

    @Test
    public void createItemNoAvailableStatus400() throws Exception {
        ItemDto itemDto = new ItemDto(null, "item1", "item1", null, null);

        mockMvc.perform(post("/items").content(objectMapper.writeValueAsString(itemDto))
                .header("X-Sharer-User-Id", 1L).characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void updateItemNoNameStatus400() throws Exception {
        ItemDto itemDto = new ItemDto(null, "", "item1", true, null);
        mockMvc.perform(post("/items").content(objectMapper.writeValueAsString(itemDto))
                .header("X-Sharer-User-Id", 1L).characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void createItemNoDescriptionStatus400() throws Exception {
        ItemDto itemDto = new ItemDto(null, "item1", "", true, null);
        mockMvc.perform(post("/items").content(objectMapper.writeValueAsString(itemDto))
                .header("X-Sharer-User-Id", 1L).characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void updateItemStatus200() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto(null, "item1", "", true, null);
        ItemDto itemDtoAnswer = new ItemDto(itemId, "item", "desc", true,
                userId);
        when(service.updateItem(itemDto, itemId, userId)).thenReturn(Optional.of(itemDtoAnswer));

        mockMvc.perform(patch("/items/{id}", itemId).content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId).characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemDtoAnswer.getName())));
        verify(service, Mockito.times(1)).updateItem(itemDto, itemId, userId);
    }

    @Test
    public void UpdateItemNoUserAuthStatus400() throws Exception {
        ItemDto itemDto = new ItemDto(null, "item1", "", true, null);

        mockMvc.perform(patch("/items/{id}", 1)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateItemErrorUserStatus404() throws Exception {
        Long userId = 100L;
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto(null, "item1", "", true, null);
        when(service.updateItem(itemDto, itemId, userId)).thenThrow(new NotFoundEx("User not found"));
        mockMvc.perform(patch("/items/{id}", itemId).content(objectMapper.writeValueAsString(itemDto))
                .header("X-Sharer-User-Id", userId).characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
        verify(service, Mockito.times(1)).updateItem(itemDto, itemId, userId);
    }

    @Test
    public void findItemByIdStatus200() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDtoAnswer itemDtoAnswer = new ItemDtoAnswer(1L, "item1", "item1", true,
                null, null, null, null);
        when(service.findItemById(userId, itemId)).thenReturn(Optional.of(itemDtoAnswer));
        mockMvc.perform(get("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemDtoAnswer.getName())));
        verify(service, Mockito.times(1)).findItemById(userId, itemId);
    }

    @Test
    public void findAllOwnersItemsStatus200() throws Exception {
        Long userId = 1L;
        ItemDtoAnswer itemDtoAnswer = new ItemDtoAnswer(1L, "item1", "item1", true,
                null, null, null, null);
        when(service.getItemsByOwner(userId, 0, 10)).thenReturn(List.of(itemDtoAnswer));
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(itemDtoAnswer.getName())));
        verify(service, Mockito.times(1)).getItemsByOwner(userId, 0, 10);
    }

    @Test
    public void getItemsNoParametrsStatus200() throws Exception {
        Long userId = 1L;
        ItemDtoAnswer itemDtoAnswer = new ItemDtoAnswer(1L, "item1", "item1", true,
                null, null, null, null);
        when(service.getItemsByOwner(userId, 0, 10)).thenReturn(List.of(itemDtoAnswer));
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(itemDtoAnswer.getName())));
        verify(service, Mockito.times(1)).getItemsByOwner(userId, 0, 10);
    }

    @Test
    public void getItemsErrorPaginationStatus400() throws Exception {
        Long userId = 1L;
        when(service.getItemsByOwner(userId, 0, -10))
                .thenThrow(new IllegalArgumentEx("Argument size incorrect"));
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(-10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, Mockito.times(1)).getItemsByOwner(userId, 0, -10);
    }

    @Test
    public void searchItemsStatus200() throws Exception {
        Long userId = 1L;

        String search = "text";
        ItemDto itemDto = new ItemDto(1L, "item1", "text", true,
                null);
        when(service.search(search, 0, 10)).thenReturn(List.of(itemDto));
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", search)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())));
        verify(service, Mockito.times(1)).search(search, 0, 10);
    }

    @Test
    public void searchItemsErrorPaginationStatus400() throws Exception {
        Long userId = 1L;
        when(service.search("sfdsfd", 0, -10))
                .thenThrow(new IllegalArgumentEx("Argument size incorrect"));
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", "sfdsfd")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(-10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, Mockito.times(1)).search("sfdsfd", 0, -10);
    }

    @Test
    public void createCommentStatus200() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        CommentDto commentDto = new CommentDto(null, "text", null, "author",
                null);
        CommentDto commentDtoAnswer = new CommentDto(1L, "text", 1l, "author",
                LocalDateTime.of(2022, Month.SEPTEMBER, 5, 8, 0));
        when(service.createComment(userId, itemId, commentDto)).thenReturn(commentDtoAnswer);
        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is(commentDtoAnswer.getText())));
        verify(service, Mockito.times(1)).createComment(userId, itemId, commentDto);
    }

    @Test
    public void createCommentNoTextStatus400() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        CommentDto commentDto = new CommentDto(null, "text", null, "author",
                null);
        when(service.createComment(userId, itemId, commentDto)).thenThrow(new IllegalArgumentEx("Argument size incorrect"));
        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
