package ru.practicum.shareit.itemRequestControllerTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.requests.ItemRequestRepositoryJpa;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.ItemRequestServiceImpl;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserRepositoryJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.Validation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Transactional
@RequiredArgsConstructor
public class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepositoryJpa itemRequestRepositoryJpa = Mockito.mock(ItemRequestRepositoryJpa.class);
    @Mock
    private UserRepositoryJpa userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
    @Mock
    private ItemRepositoryJpa itemRepositoryJpa = Mockito.mock(ItemRepositoryJpa.class);
    @Mock
    private BookingRepositoryJpa bookingRepositoryJpa = Mockito.mock(BookingRepositoryJpa.class);
    private final Validation validation = new Validation(userRepositoryJpa, itemRepositoryJpa, bookingRepositoryJpa);
    private final ItemRequestService itemRequestService = new ItemRequestServiceImpl(itemRequestRepositoryJpa,
            userRepositoryJpa, validation);


    @Test
    public void createItemRequest() throws NotFoundEx {
        Long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "text");
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        itemRequest.setRequestor(user);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepositoryJpa.save(any())).thenReturn(itemRequest);
        Assertions.assertEquals("text", itemRequestService.createItemRequest(userId, itemRequestDto)
                .getDescription());
    }

    @Test
    public void createItemRequestWrongRequester() {
        Long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "text");
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(itemRequestRepositoryJpa.save(any())).thenReturn(itemRequest);
        Assertions.assertThrows(NotFoundEx.class,
                () -> itemRequestService.createItemRequest(userId, itemRequestDto));
    }

    @Test
    public void findItemRequestById() throws NotFoundEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        when(itemRequestRepositoryJpa.findById(any())).thenReturn(Optional.of(itemRequest));
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        Assertions.assertEquals("text", itemRequestService.findItemRequestById(1L, 1L).get()
                .getDescription());
    }

    @Test
    public void findOwnerUsersItemRequests() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(2L, "name", "email@dffd.ru", null,
                null, null);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        when(itemRequestRepositoryJpa.findItemRequestsByUser(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));
        Assertions.assertEquals("text", itemRequestService.findUserOwnerItemRequests(2L, 0, 10)
                .get(0).getDescription());
    }

    @Test
    public void findAnotherUsersItemRequests() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(2L, "name", "email@dffd.ru", null,
                null, null);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        when(itemRequestRepositoryJpa.findItemRequestsByAnotherUsers(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));
        Assertions.assertEquals("text", itemRequestService.findAnotherUsersItemRequests(1L,
                0, 10).get(0).getDescription());
    }
}
