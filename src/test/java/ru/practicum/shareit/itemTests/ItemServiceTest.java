package ru.practicum.shareit.itemTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.ItemRequestRepositoryJpa;
import ru.practicum.shareit.user.UserRepositoryJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.Validation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Transactional
@RequiredArgsConstructor

public class ItemServiceTest {
    @Mock
    private ItemRepositoryJpa itemRepositoryJpa = Mockito.mock(ItemRepositoryJpa.class);
    @Mock
    private BookingRepositoryJpa bookingRepositoryJpa = Mockito.mock(BookingRepositoryJpa.class);
    @Mock
    private UserRepositoryJpa userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
    @Mock
    private BookingService bookingService = Mockito.mock(BookingService.class);
    @Mock
    private CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    @Mock
    private ItemRequestRepositoryJpa itemRequestRepositoryJpa = Mockito.mock(ItemRequestRepositoryJpa.class);
    private final Validation validation = new Validation(userRepositoryJpa, itemRepositoryJpa, bookingRepositoryJpa);
    private final ItemService itemService = new ItemServiceImpl(itemRepositoryJpa, userRepositoryJpa,
            bookingService, validation, commentRepository, itemRequestRepositoryJpa);

    @Test
    public void createItemTest() throws NotFoundEx {
        ItemDto itemDto = new ItemDto(1L, "Молоток", "кривой", true, null);
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Long userId = 1L;
        when(userRepositoryJpa.findById(any())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        Assertions.assertEquals("Молоток", itemService.create(itemDto, userId).get().getName());
    }

    @Test
    public void createItemWrongUserId() {
        ItemDto itemDto = new ItemDto(1L, "Молоток", "кривой", true, null);
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        Assertions.assertThrows(NotFoundEx.class,
                () -> itemService.create(itemDto, 2L));
    }

    @Test
    public void updateItem() throws NotFoundEx {
        ItemDto itemDto = new ItemDto(1L, "Молоток", "кривой", true, null);
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Long userId = 1L;
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        itemService.create(itemDto, 1L);
        itemDto.setName("топор");
        Assertions.assertEquals("топор", itemService.updateItem(itemDto, 1L, userId).get().getName());
    }

    @Test
    public void updateItemWrongUserId() {
        ItemDto itemDto = new ItemDto(1L, "Молоток", "кривой", true, null);
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Long userId = 1L;
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        itemDto.setName("топор");
        Assertions.assertThrows(NotFoundEx.class,
                () -> itemService.updateItem(itemDto, 12L, userId));
    }

    @Test
    public void updateItemWrongItem() {
        ItemDto itemDto = new ItemDto(1L, "Молоток", "кривой", true, null);
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Long userId = 1L;
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        itemDto.setName("топор");
        Assertions.assertThrows(NotFoundEx.class,
                () -> itemService.updateItem(itemDto, 1L, userId));
    }

    @Test
    public void updateItemUserIsNotOwner() {
        ItemDto itemDto = new ItemDto(1L, "Молоток", "кривой", true, null);
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Long userId = 1L;
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        itemDto.setName("топор");
        Assertions.assertThrows(NotFoundEx.class,
                () -> itemService.updateItem(itemDto, 1L, 2L));
    }

    @Test
    public void findByItemTest() {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        Assertions.assertEquals("Молоток", itemService.findItemById(1L, 1L).get().getName());
    }

    @Test
    public void findByItemWrongItemTest() {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        Assertions.assertEquals(Optional.empty(), itemService.findItemById(1L, 1L));
    }

    @Test
    public void findItemByUserIdUserIsNotOwner() {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        Assertions.assertEquals(item.getName(),
                itemService.findItemById(2L, 1L).get().getName());
    }

    @Test
    public void getItemsByOwner() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null, null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Item item2 = new Item(2L, "Молоток2", "кривой2", user, true, null);
        Comment comment = new Comment(1L, "sdsd", item, user, LocalDateTime.now());
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.getItemsByOwner(anyLong(), any())).thenReturn(new PageImpl<>(List.of(item, item2)));
        when(bookingService.getItemLastBookings(anyLong(), anyLong())).thenReturn(null);
        when(bookingService.getItemNextBookings(anyLong(), anyLong())).thenReturn(null);
        when(commentRepository.findByItem(anyLong())).thenReturn(List.of(comment));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        Assertions.assertEquals(comment.getId(), itemService.getItemsByOwner(1L, 0, 10).get(0)
                .getComments().stream().collect(Collectors.toList()).get(0).getId());
    }

    @Test
    public void searchTest() throws IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        when(itemRepositoryJpa.search(anyString(), any())).thenReturn(new PageImpl<>(List.of(item)));
        Assertions.assertEquals("Молоток", itemService.search("молот", 0, 10).get(0).getName());
    }

    @Test
    public void searchTestPaginationError() {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        when(itemRepositoryJpa.search(anyString(), any())).thenReturn(new PageImpl<>(List.of(item)));
        Assertions.assertThrows(IllegalArgumentEx.class,
                () -> itemService.search("молот", -1, 10).get(0).getName());
    }

    @Test
    public void createCommentTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Comment comment = new Comment(1L, "комментарий", item, user, LocalDateTime.now());
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.getItemsByOwner(anyLong(), any())).thenReturn(new PageImpl<>(List.of(item)));
        when(bookingService.getItemLastBookings(anyLong(), anyLong())).thenReturn(null);
        when(bookingService.getItemNextBookings(anyLong(), anyLong())).thenReturn(null);
        when(commentRepository.findByItem(anyLong())).thenReturn(List.of(comment));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        Assertions.assertEquals("комментарий", itemService.getItemsByOwner(1L, 0, 10).get(0)
                .getComments().stream().collect(Collectors.toList()).get(0).getText());
    }

    @Test
    public void createCommentWrongUser() {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Comment comment = new Comment(1L, "комментарий", item, user, LocalDateTime.now());
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(itemRepositoryJpa.getItemsByOwner(anyLong(), any())).thenReturn(new PageImpl<>(List.of(item)));
        when(bookingService.getItemLastBookings(anyLong(), anyLong())).thenReturn(null);
        when(bookingService.getItemNextBookings(anyLong(), anyLong())).thenReturn(null);
        when(commentRepository.findByItem(anyLong())).thenReturn(List.of(comment));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        Assertions.assertThrows(NotFoundEx.class,
                () -> itemService.getItemsByOwner(1L, 0, 10));
    }
}