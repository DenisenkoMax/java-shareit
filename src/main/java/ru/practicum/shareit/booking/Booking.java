package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class
Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long item;
    private Long booker;
    private String description;
    private BookingStatus status;

    public enum BookingStatus {WAITING, APPROVED, REJECTED, CANCELED}

    ;
}
