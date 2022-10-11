package ru.practicum.shareit.bookingTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDtoAnswer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @MockBean
    private BookingService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createBookingStatus200() throws Exception {
        Long userId = 1L;
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(3);

        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, 1L, BookingStatus.WAITING);
        ItemDto itemDto = new ItemDto(1L, "name", "desc", true, 1L);
        UserDtoAnswer userDtoAnswer = new UserDtoAnswer(userId, "user", "user@user.ru");
        BookingDto bookingDtoAnswer = new BookingDto(1L, start, end, 1L, 1L, BookingStatus.WAITING);

        when(service.create(bookingDto, userId)).thenReturn(bookingDtoAnswer);

        mockMvc.perform(post("/bookings").content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userId).characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.itemId", is(1)));

        verify(service, Mockito.times(1)).create(bookingDto, userId);
    }

    @Test
    public void createBookingErrorUserStatus404() throws Exception {
        Long userId = 1L;
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(3);

        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, 1L, BookingStatus.WAITING);
        when(service.create(bookingDto, userId)).thenThrow(new NotFoundEx("User not found"));

        mockMvc.perform(post("/bookings").content(objectMapper.writeValueAsString(bookingDto))
                .header("X-Sharer-User-Id", userId).characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

        verify(service, Mockito.times(1)).create(bookingDto, userId);
    }

    @Test
    public void createBookingErrorDatesStatus400() throws Exception {
        Long userId = 1L;
        LocalDateTime start = LocalDateTime.now().plusHours(5);
        LocalDateTime end = LocalDateTime.now().minusHours(3);

        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, 1L, BookingStatus.WAITING);
        when(service.create(bookingDto, userId)).thenThrow(new IllegalArgumentEx("Argument size incorrect"));

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void confirmBookingRequestStatus200() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        boolean approved = true;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        ItemDto itemDto = new ItemDto(1L, "name", "desc", true, 1L);
        UserDtoAnswer userDtoAnswer = new UserDtoAnswer(userId, "user", "user@user.ru");
        BookingDtoAnswer bookingDtoAnswer = new BookingDtoAnswer(1L, start, end, itemDto, userDtoAnswer,
                BookingStatus.WAITING);
        when(service.confirmBookingRequest(bookingId, userId, approved)).thenReturn(bookingDtoAnswer);
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(1)));

        verify(service, Mockito.times(1)).confirmBookingRequest(bookingId, userId, approved);
    }

    @Test
    public void confirmBookingRequestWrongBookingStatus404() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        boolean approved = true;
        when(service.confirmBookingRequest(bookingId, userId, approved)).thenThrow(new NotFoundEx(""));
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, Mockito.times(1)).confirmBookingRequest(bookingId, userId, approved);
    }

    @Test
    public void getBookingByIdStatus200() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        ItemDto itemDto = new ItemDto(1L, "name", "desc", true, 1L);
        UserDtoAnswer userDtoAnswer = new UserDtoAnswer(userId, "user", "user@user.ru");
        BookingDtoAnswer bookingDtoAnswer = new BookingDtoAnswer(1L, start, end, itemDto, userDtoAnswer,
                BookingStatus.WAITING);
        when(service.getBookingById(bookingId, userId)).thenReturn(bookingDtoAnswer);
        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(1)));
        verify(service, Mockito.times(1)).getBookingById(bookingId, userId);
    }

    @Test
    public void getBookingByIdErrorUserStatus404() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        when(service.getBookingById(bookingId, userId)).thenThrow(new NotFoundEx("User not found"));
        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, Mockito.times(1)).getBookingById(bookingId, userId);
    }

    @Test
    public void getBookingByIdErrorIdStatus404() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        when(service.getBookingById(bookingId, userId)).thenThrow(new NotFoundEx(""));
        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, Mockito.times(1)).getBookingById(bookingId, userId);
    }

    @Test
    public void getAllUserBookingsStatus200() throws Exception {
        Long userId = 1L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        ItemDto itemDto = new ItemDto(1L, "name", "desc", true, 1L);
        UserDtoAnswer userDtoAnswer = new UserDtoAnswer(userId, "user", "user@user.ru");
        BookingDtoAnswer bookingDtoAnswer = new BookingDtoAnswer(1L, start, end, itemDto, userDtoAnswer,
                BookingStatus.WAITING);
        when(service.getUserBookings(userId, "ALL", 0, 10)).thenReturn(List.of(bookingDtoAnswer));
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].item.id", is(1)))
                .andExpect(jsonPath("$[0].booker.id", is(1)));
        verify(service, Mockito.times(1)).getUserBookings(userId, "ALL", 0, 10);
    }

    @Test
    public void getAllUserBookingsNoParametrsStatus200() throws Exception {
        Long userId = 1L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, 1L, BookingStatus.WAITING);
        ItemDto itemDto = new ItemDto(1L, "name", "desc", true, 1L);
        UserDtoAnswer userDtoAnswer = new UserDtoAnswer(userId, "user", "user@user.ru");
        BookingDtoAnswer bookingDtoAnswer = new BookingDtoAnswer(1L, start, end, itemDto, userDtoAnswer,
                BookingStatus.WAITING);
        when(service.getUserBookings(userId, "ALL", 0, 10)).thenReturn(List.of(bookingDtoAnswer));
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].item.id", is(1)))
                .andExpect(jsonPath("$[0].booker.id", is(1)));
        verify(service, Mockito.times(1)).getUserBookings(userId, "ALL", 0, 10);
    }
}
