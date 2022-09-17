package ru.practicum.shareit.item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoAnswer;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepositoryJpa;
import ru.practicum.shareit.validation.Validation;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepositoryJpa itemRepository;
    private final UserRepositoryJpa userRepository;
    private final BookingService bookingService;
    private final Validation validation;

    private final CommentRepository commentRepository;

    @Override
    public Optional<Item> create(ItemDto itemDto, long userId) throws NotFoundEx {
        validation.validateUser(userId);
        validation.validateItemDtoParametrs(itemDto);

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.findById(userId).get());
        return Optional.ofNullable(itemRepository.save(item));
    }

    @Override
    public Optional<ItemDto> updateItem(ItemDto itemDto, long itemId, long userId) throws NotFoundEx {
        validation.validateUser(userId);
        validation.validateItem(itemId);
        Item item = itemRepository.findById(itemId).get();
        validation.validateItemOwner(item, userId);
        if ((itemDto.getName() != null) && (!itemDto.getName().isEmpty())) {
            item.setName(itemDto.getName());
        }
        if ((itemDto.getDescription() != null) && (!itemDto.getDescription().isEmpty())) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        item.setOwner(userRepository.findById(userId).get());
        itemRepository.save(item);
        return Optional.ofNullable(ItemMapper.toItemDto(item));
    }

    @Override
    public Optional<ItemDtoAnswer> findItemById(long userId, long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (!item.isPresent()) {
            return Optional.empty();
        }

        if ((userRepository.findById(userId).isPresent()) &&
                (item.get().getOwner().getId() == userId)) {
            Booking lastBooking = bookingService.getItemLastBookings(itemId, userId);
            Booking nextBooking = bookingService.getItemNextBookings(itemId, userId);
            return Optional.ofNullable(ItemMapper.toItemDtoAnswer(item.get(),
                    BookingMapper.toBookingDto(lastBooking),
                    BookingMapper.toBookingDto(nextBooking),
                    commentRepository.findByItem(itemId).stream().map(p -> CommentMapper.toCommentDto(p)).collect(Collectors.toList())
            ));
        } else return Optional.ofNullable(ItemMapper.toItemDtoAnswer(item.get(),
                null,
                null,
                commentRepository.findByItem(itemId).stream().map(p -> CommentMapper.toCommentDto(p)).collect(Collectors.toList())
        ));
    }

    @Override
    public List<ItemDtoAnswer> getItemsByOwner(long userId) throws NotFoundEx {
        validation.validateUser(userId);

        return itemRepository.getItemsByOwner(userId).stream().map(p -> ItemMapper.toItemDtoAnswer(p,
                        BookingMapper.toBookingDto(bookingService.getItemLastBookings(p.getId(), userId)),
                        BookingMapper.toBookingDto(bookingService.getItemNextBookings(p.getId(), userId)),
                        commentRepository.findByItem(p.getId()).stream().map(n -> CommentMapper.toCommentDto(n)).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text).stream().map(p -> ItemMapper.toItemDto(p)).collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) throws NotFoundEx, IllegalArgumentEx {
        validation.validateUser(userId);
        validation.validateItem(itemId);

        Comment comment;
        if (bookingService.getUserBookings(userId, "PAST")
                .stream().filter(p -> p.getStatus().equals(BookingStatus.APPROVED))
                .anyMatch(p -> p.getItem().getId().equals(itemId))) {
            comment = CommentMapper.toComment(commentDto);
            comment.setCreated(LocalDateTime.now());
            comment.setAuthor(userRepository.findById(userId).get());
            comment.setItem(itemRepository.findById(itemId).get());
            commentRepository.save(comment);
        } else
            throw new IllegalArgumentEx("You can not take a comments for someone else's booking");
        return CommentMapper.toCommentDto(comment);
    }
}