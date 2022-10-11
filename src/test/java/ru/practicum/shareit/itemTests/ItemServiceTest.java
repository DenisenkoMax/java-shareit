package ru.practicum.shareit.itemTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
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
    private ItemRepositoryJpa itemRepositoryJpa;
    @Mock
    private UserRepositoryJpa userRepositoryJpa;
    @Mock
    private BookingService bookingService;
    @Mock
    private Validation validation;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepositoryJpa itemRequestRepositoryJpa;
    @Mock
    private ItemService itemService;

    @BeforeEach
    public void before() {
        itemRepositoryJpa = Mockito.mock(ItemRepositoryJpa.class);
        userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
        bookingService = Mockito.mock(BookingService.class);
        validation = Mockito.mock(Validation.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        itemRequestRepositoryJpa = Mockito.mock(ItemRequestRepositoryJpa.class);
        itemService = new ItemServiceImpl(itemRepositoryJpa, userRepositoryJpa, bookingService, validation,
                commentRepository, itemRequestRepositoryJpa);
    }

    @Test
    public void createItemTest() throws NotFoundEx {
        ItemDto itemDto = new ItemDto(1L, "Молоток", "кривой", true, null);
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Long userId = 1L;
        Mockito.doNothing().when(validation).validateUser(anyLong());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        Assertions.assertEquals("Молоток", itemService.create(itemDto, userId).get().getName());
    }

    @Test
    public void createItemWrongUserId() throws NotFoundEx {
        ItemDto itemDto = new ItemDto(1L, "Молоток", "кривой", true, null);
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Long userId = 1L;

        Mockito.doThrow(new NotFoundEx("user not found")).when(validation).validateUser(anyLong());
        
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        Assertions.assertThrows(NotFoundEx.class,
                () -> itemService.create(itemDto, userId));
    }

    @Test
    public void updateItem() throws NotFoundEx {
        ItemDto itemDto = new ItemDto(1L, "Молоток", "кривой", true, null);
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Long userId = 1L;
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validateItem(anyLong());
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.doNothing().when(validation).validateItemOwner(any(), anyLong());
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        

        when(itemRepositoryJpa.save(any())).thenReturn(item);
        itemService.create(itemDto, 1L);
        itemDto.setName("топор");
        Assertions.assertEquals("топор", itemService.updateItem(itemDto, 1L, userId).get().getName());
    }

    @Test
    public void updateItemWrongUserId() throws NotFoundEx {
        ItemDto itemDto = new ItemDto(1L, "Молоток", "кривой", true, null);
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Long userId = 1L;
        Mockito.doThrow(new NotFoundEx("user not found")).when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validateItem(anyLong());
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.doNothing().when(validation).validateItemOwner(any(), anyLong());
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        itemDto.setName("топор");
        Assertions.assertThrows(NotFoundEx.class,
                () -> itemService.updateItem(itemDto, 1L, userId));
    }

    @Test
    public void updateItemWrongItem() throws NotFoundEx {
        ItemDto itemDto = new ItemDto(1L, "Молоток", "кривой", true, null);
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Long userId = 1L;
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doThrow(new NotFoundEx("user not found")).when(validation).validateItem(anyLong());
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.doNothing().when(validation).validateItemOwner(any(), anyLong());
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        itemDto.setName("топор");
        Assertions.assertThrows(NotFoundEx.class,
                () -> itemService.updateItem(itemDto, 1L, userId));
    }

    @Test
    public void findByItemTest() throws NotFoundEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Mockito.doNothing().when(validation).validateUser(anyLong());
        
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.save(any())).thenReturn(item);

        Assertions.assertEquals("Молоток", itemService.findItemById(1L, 1L).get().getName());
    }

    @Test
    public void findByItemWrongItemTest() throws NotFoundEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Mockito.doNothing().when(validation).validateUser(anyLong());
        
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        Assertions.assertEquals(Optional.empty(), itemService.findItemById(1L, 1L));
    }

    @Test
    public void getItemsByOwner() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null, null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Comment comment = new Comment(1L, "sdsd", item, user, LocalDateTime.now());
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validateItem(anyLong());
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.doNothing().when(validation).validateItemOwner(any(), anyLong());
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.getItemsByOwner(anyLong(), any())).thenReturn(new PageImpl<>(List.of(item)));
        when(bookingService.getItemLastBookings(anyLong(), anyLong())).thenReturn(null);
        when(bookingService.getItemNextBookings(anyLong(), anyLong())).thenReturn(null);
        when(commentRepository.findByItem(anyLong())).thenReturn(List.of(comment));
        
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        Assertions.assertEquals("Молоток", itemService.getItemsByOwner(1L, 0, 10).get(0)
                .getName());
    }

    @Test
    public void searchTest() throws IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
        when(itemRepositoryJpa.search(anyString(), any())).thenReturn(new PageImpl<>(List.of(item)));
        Assertions.assertEquals("Молоток", itemService.search("молот", 0, 10).get(0).getName());
    }

    @Test
    public void createCommentTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Comment comment = new Comment(1L, "комментарий", item, user, LocalDateTime.now());
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validateItem(anyLong());
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.doNothing().when(validation).validateItemOwner(any(), anyLong());
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
    public void createCommentWrongUser() throws NotFoundEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        Item item = new Item(1L, "Молоток", "кривой", user, true, null);
        Comment comment = new Comment(1L, "комментарий", item, user, LocalDateTime.now());
        Mockito.doThrow(new NotFoundEx("")).when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validateItem(anyLong());
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.doNothing().when(validation).validateItemOwner(any(), anyLong());
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.getItemsByOwner(anyLong(), any())).thenReturn(new PageImpl<>(List.of(item)));
        when(bookingService.getItemLastBookings(anyLong(), anyLong())).thenReturn(null);
        when(bookingService.getItemNextBookings(anyLong(), anyLong())).thenReturn(null);
        when(commentRepository.findByItem(anyLong())).thenReturn(List.of(comment));
        
        when(itemRepositoryJpa.save(any())).thenReturn(item);
        Assertions.assertThrows(NotFoundEx.class,
                () -> itemService.getItemsByOwner(1L, 0, 10));
    }
}
