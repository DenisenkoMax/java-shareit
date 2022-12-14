package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

@Repository
public interface BookingRepositoryJpa extends JpaRepository<Booking, Long>, BookingRepositoryJpaCustom {
}