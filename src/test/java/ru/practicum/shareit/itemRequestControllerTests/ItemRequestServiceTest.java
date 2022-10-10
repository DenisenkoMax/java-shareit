package ru.practicum.shareit.itemRequestControllerTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
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
    private ItemRequestRepositoryJpa itemRequestRepositoryJpa;
    @Mock
    private UserRepositoryJpa userRepositoryJpa;
    @Mock
    private Validation validation;
    @Mock
    private ItemRequestService itemRequestService;

    @BeforeEach
    public void before() {
        itemRequestRepositoryJpa = Mockito.mock(ItemRequestRepositoryJpa.class);
        userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
        validation = Mockito.mock(Validation.class);
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepositoryJpa, userRepositoryJpa, validation);
    }

    @Test
    public void CreateItemRequest() throws NotFoundEx {
        Long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "text");
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);

        Mockito.doNothing().when(validation).validateRequester(anyLong());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepositoryJpa.save(any())).thenReturn(itemRequest);

        Assertions.assertEquals("text", itemRequestService.createItemRequest(userId, itemRequestDto)
                .getDescription());
    }

    @Test
    public void CreateItemRequestWrongRequester() throws NotFoundEx {
        Long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "text");
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);

        Mockito.doThrow(new NotFoundEx("")).when(validation).validateUser(any());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepositoryJpa.save(any())).thenReturn(itemRequest);

        Assertions.assertThrows(NotFoundEx.class,
                () -> itemRequestService.createItemRequest(userId, itemRequestDto));
    }

    @Test
    public void FindItemRequestById() throws NotFoundEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);

        Mockito.doNothing().when(validation).validateRequester(anyLong());
        when(itemRequestRepositoryJpa.findById(any())).thenReturn(Optional.of(itemRequest));

        Assertions.assertEquals("text", itemRequestService.findItemRequestById(1L, 1L).get()
                .getDescription());
    }

    @Test
    public void findOwnerUsersItemRequests() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(2L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);

        Mockito.doNothing().when(validation).validateRequester(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
        when(itemRequestRepositoryJpa.findItemRequestsByUser(anyLong(), any())).
                thenReturn(new PageImpl<>(List.of(itemRequest)));

        Assertions.assertEquals("text", itemRequestService.findUserOwnerItemRequests(2L, 0, 10)
                .get(0).getDescription());
    }

    @Test
    public void findAnotherUsersItemRequests() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(2L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);

        Mockito.doNothing().when(validation).validateRequester(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
        when(itemRequestRepositoryJpa.findItemRequestsByAnotherUsers(anyLong(), any())).
                thenReturn(new PageImpl<>(List.of(itemRequest)));

        Assertions.assertEquals("text", itemRequestService.findAnotherUsersItemRequests(1L, 0, 10)
                .get(0).getDescription());
    }


}
