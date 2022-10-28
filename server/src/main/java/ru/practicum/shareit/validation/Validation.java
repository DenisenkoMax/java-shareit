package ru.practicum.shareit.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepositoryJpa;

@Service
@RequiredArgsConstructor
public class Validation {
    private final UserRepositoryJpa userRepository;
    private final ItemRepositoryJpa itemRepository;
    private final BookingRepositoryJpa bookingRepository;


    public void validateUserIsBookerOrOwner(Booking booking, long userId) throws NotFoundEx {
        long bookerId = booking.getBooker().getId();
        long ownerId = booking.getItem().getOwner().getId();
        if ((bookerId != userId) && (ownerId != userId))
            throw new NotFoundEx("User is not booker");
    }


    public void validateUser(Long userId) throws NotFoundEx {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundEx("User not found");
        }
    }

    public void validateItem(Long itemId) throws NotFoundEx {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundEx("Item not found");
        }
    }

    public void validateItemAvailable(Long itemId) throws IllegalArgumentEx {
        if (!itemRepository.findById(itemId).get().getAvailable()) {
            throw new IllegalArgumentEx("Item not available");
        }
    }

    public void validateBooking(Long bookingId) throws NotFoundEx {
        if (bookingRepository.findById(bookingId).isEmpty()) {
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
