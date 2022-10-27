package ru.practicum.shareit.itemRequestControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.requests.ItemRequestController;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoAnswer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTests {
    @MockBean
    private ItemRequestService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createItemRequestStatus200() throws Exception {
        Long userId = 1L;
        LocalDateTime time = LocalDateTime.now();
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "text");
        ItemDto itemDto = new ItemDto(1L, "name", "desc", true, 1L);
        ItemRequestDtoAnswer itemRequestDtoAnswer = new ItemRequestDtoAnswer(1L, "text", time,
                Set.of(itemDto));
        when(service.createItemRequest(userId, itemRequestDto)).thenReturn(itemRequestDtoAnswer);
        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is(itemRequestDtoAnswer.getDescription())))
                .andExpect(jsonPath("$.items[0].id", is(1)));
        verify(service, Mockito.times(1)).createItemRequest(userId, itemRequestDto);
    }

    @Test
    public void createItemRequestErrorUserStatus404() throws Exception {
        Long userId = 2L;
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "text");
        when(service.createItemRequest(userId, itemRequestDto)).thenThrow(new NotFoundEx(""));
        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, Mockito.times(1)).createItemRequest(userId, itemRequestDto);
    }

    @Test
    public void createItemRequestEmptyDescriptionStatus200() throws Exception {
        Long userId = 2L;
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "");
        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findItemRequestByIdStatus200() throws Exception {
        Long userId = 1L;
        Long requestId = 1L;
        LocalDateTime time = LocalDateTime.now();
        ItemDto itemDto = new ItemDto(1L, "name", "text", true, 1L);
        ItemRequestDtoAnswer itemRequestDtoAnswer = new ItemRequestDtoAnswer(1L, "text", time,
                Set.of(itemDto));
        when(service.findItemRequestById(userId, requestId)).thenReturn(Optional.of(itemRequestDtoAnswer));
        mockMvc.perform(get("/requests/{id}", requestId)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.items[0].id", is(1)));
        verify(service, Mockito.times(1)).findItemRequestById(userId, requestId);
    }

    @Test
    public void findItemRequestByIdErrorWrongUserStatus404() throws Exception {
        Long userId = 2L;
        Long requestId = 1L;
        when(service.findItemRequestById(userId, requestId)).thenThrow(new NotFoundEx("User not found"));
        mockMvc.perform(get("/requests/{id}", requestId)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, Mockito.times(1)).findItemRequestById(userId, requestId);
    }

    @Test
    public void findItemRequestErrorIdStatus404() throws Exception {
        Long userId = 2L;
        Long requestId = 1L;
        when(service.findItemRequestById(userId, requestId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/requests/{id}", requestId)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, Mockito.times(1)).findItemRequestById(userId, requestId);
    }

    @Test
    public void findUserOwnerItemRequestsStatus200() throws Exception {
        Long userId = 1L;
        LocalDateTime time = LocalDateTime.now();
        ItemDto itemDto = new ItemDto(1L, "name", "text", true, 1L);
        ItemRequestDtoAnswer itemRequestDtoAnswer = new ItemRequestDtoAnswer(1L, "text", time,
                Set.of(itemDto));
        when(service.findUserOwnerItemRequests(userId, 0, 10)).thenReturn(List.of(itemRequestDtoAnswer));
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].items[0].id", is(1)));
        verify(service, Mockito.times(1)).findUserOwnerItemRequests(userId, 0, 10);
    }

    @Test
    public void findUserOwnerItemRequestsNoParametrsStatus200() throws Exception {
        Long userId = 1L;
        LocalDateTime time = LocalDateTime.now();
        ItemDto itemDto = new ItemDto(1L, "name", "text", true, 1L);
        ItemRequestDtoAnswer itemRequestDtoAnswer = new ItemRequestDtoAnswer(1L, "text", time,
                Set.of(itemDto));
        when(service.findUserOwnerItemRequests(userId, 0, 10)).thenReturn(List.of(itemRequestDtoAnswer));
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].items[0].id", is(1)));
        verify(service, Mockito.times(1)).findUserOwnerItemRequests(userId, 0, 10);
    }

    @Test
    public void findAnotherUsersItemRequestsStatus200() throws Exception {
        Long userId = 1L;
        LocalDateTime time = LocalDateTime.now();
        ItemDto itemDto = new ItemDto(1L, "name", "text", true, 1L);
        ItemRequestDtoAnswer itemRequestDtoAnswer = new ItemRequestDtoAnswer(1L, "text", time,
                Set.of(itemDto));
        when(service.findAnotherUsersItemRequests(userId, 0, 10)).thenReturn(List.of(itemRequestDtoAnswer));
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())));
    }

    @Test
    public void findAnotherUsersItemRequestsNoParametrsStatus200() throws Exception {
        Long userId = 1L;
        LocalDateTime time = LocalDateTime.now();
        ItemDto itemDto = new ItemDto(1L, "name", "text", true, 1L);
        ItemRequestDtoAnswer itemRequestDtoAnswer = new ItemRequestDtoAnswer(1L, "text", time,
                Set.of(itemDto));
        when(service.findAnotherUsersItemRequests(userId, 0, 10)).thenReturn(List.of(itemRequestDtoAnswer));
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())));
    }
}
