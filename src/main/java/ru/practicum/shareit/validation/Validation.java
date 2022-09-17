package ru.practicum.shareit.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepositoryJpa;

import javax.validation.ValidationException;

@Service
@RequiredArgsConstructor
public class Validation {
    private final UserRepositoryJpa userRepository;
    private final ItemRepositoryJpa itemRepository;
    private final BookingRepositoryJpa bookingRepository;

    public void validateItemDtoParametrs(ItemDto itemDto) {
        if ((itemDto.getName().isEmpty()) || (itemDto.getName() == null)
                || (itemDto.getDescription() == null) || (itemDto.getDescription().isEmpty())
                || (itemDto.getAvailable() == null)
        )
            throw new ValidationException("error");
    }

    public void validateItemOwner(Item item, Long userId) throws NotFoundEx {
        if (item.getOwner().getId() != userId)
            throw new NotFoundEx("User is not owner");
    }

    public void validateUserIsBookerOrOwner(Booking booking, Long userId) throws NotFoundEx {
        Long bookerId = booking.getBooker().getId();
        Long ownerId = booking.getItem().getOwner().getId();
        if ((bookerId != userId) && (ownerId != userId))
            throw new NotFoundEx("User is not booker");
    }

    public void validateUser(Long userId) throws NotFoundEx {
        if (!userRepository.findById(userId).isPresent()) {
            throw new NotFoundEx("User not found");
        }
    }

    public void validateItem(Long itemId) throws NotFoundEx {
        if ((itemId == null) || (!itemRepository.findById(itemId).isPresent())) {
            throw new NotFoundEx("Item not found");
        }
    }

    public void validateItemAvailable(Long itemId) throws IllegalArgumentEx {
        if ((itemId == null) || (!itemRepository.findById(itemId).get().getAvailable() == true)) {
            throw new IllegalArgumentEx("Item not available");
        }
    }

    public void validateBookingDate(BookingDto bookingDto) throws IllegalArgumentEx {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new IllegalArgumentEx("Item date wrong");
        }
    }

    public void validateBooking(Long bookingId) throws NotFoundEx {
        if ((bookingId == null) || (!bookingRepository.findById(bookingId).isPresent())) {
            throw new NotFoundEx("Booking not found");
        }
    }

    public void validateItemOwner(Item item, long userId) throws NotFoundEx {
        if (item.getOwner().getId() != userId) {
            throw new NotFoundEx("User is not owner");
        }
    }

    public void validateBookerIsOwner(Item item, long userId) throws NotFoundEx {
        if (item.getOwner().getId() == userId) {
            throw new NotFoundEx("the owner cannot book his item");
        }
    }
}
