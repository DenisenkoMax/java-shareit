package ru.practicum.shareit.booking;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;

import javax.persistence.EntityManager;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BookingRepositoryJpaImpl implements BookingRepositoryJpaCustom {
    private final EntityManager entityManager;

    @Override
    public List<Booking> findBookingsByUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> book = cr.from(Booking.class);
        cr.select(book).where(cb.equal(book.get("booker").get("id"), userId)).orderBy(cb.desc(book.get("start")));
        List<Booking> foundBooking = entityManager.createQuery(cr).getResultList();
        return foundBooking;
    }

    @Override
    public List<Booking> findBookingsByUserAndStatus(Long userId, Booking.BookingStatus status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> book = cr.from(Booking.class);
        cr.select(book).where(cb.and(cb.equal(book.get("booker").get("id"), userId), cb.equal(book.get("status"), status)))
                .orderBy(cb.desc(book.get("end")));
        List<Booking> foundBooking = entityManager.createQuery(cr).getResultList();
        return foundBooking;
    }

    @Override
    public List<Booking> findPastBookingsByUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> book = cr.from(Booking.class);
        cr.select(book).where(cb.and(cb.equal(book.get("booker").get("id"), userId),
                        cb.lessThan(book.get("end"), LocalDateTime.now())))
                .orderBy(cb.desc(book.get("end")));
        List<Booking> foundBooking = entityManager.createQuery(cr).getResultList();
        return foundBooking;
    }

    @Override
    public List<Booking> findFutureBookingsByUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> book = cr.from(Booking.class);
        cr.select(book).where(cb.and(cb.equal(book.get("booker").get("id"), userId),
                        cb.greaterThan(book.get("start"), LocalDateTime.now())))
                .orderBy(cb.desc(book.get("end")));
        List<Booking> foundBooking = entityManager.createQuery(cr).getResultList();
        return foundBooking;
    }

    @Override
    public List<Booking> findCurrentBookingsByUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> book = cr.from(Booking.class);
        cr.select(book).where(cb.and(cb.equal(book.get("booker").get("id"), userId),
                        cb.and(cb.greaterThan(book.get("end"), LocalDateTime.now()),
                                cb.lessThan(book.get("start"), LocalDateTime.now()))))
                .orderBy(cb.desc(book.get("end")));
        List<Booking> foundBooking = entityManager.createQuery(cr).getResultList();
        return foundBooking;
    }

    @Override
    public List<Booking> findItemBookingsByUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> book = cr.from(Booking.class);
        cr.select(book).where(cb.equal(book.get("item").get("owner").get("id"), userId))
                .orderBy(cb.desc(book.get("start")));
        List<Booking> foundBooking = entityManager.createQuery(cr).getResultList();
        return foundBooking;
    }

    @Override
    public List<Booking> findItemBookingsByUserAndStatus(Long userId, Booking.BookingStatus status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> book = cr.from(Booking.class);
        cr.select(book).where(cb.and(cb.equal(book.get("item").get("owner").get("id"), userId),
                        cb.equal(book.get("status"), status)))
                .orderBy(cb.desc(book.get("end")));
        List<Booking> foundBooking = entityManager.createQuery(cr).getResultList();
        return foundBooking;
    }

    @Override
    public List<Booking> findPastItemBookingsByUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> book = cr.from(Booking.class);
        cr.select(book).where(cb.and(cb.equal(book.get("item").get("owner").get("id"), userId),
                        cb.lessThan(book.get("end"), LocalDateTime.now())))
                .orderBy(cb.desc(book.get("end")));
        List<Booking> foundBooking = entityManager.createQuery(cr).getResultList();
        return foundBooking;
    }

    @Override
    public List<Booking> findFutureItemBookingsByUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> book = cr.from(Booking.class);
        cr.select(book).where(cb.and(cb.equal(book.get("item").get("owner").get("id"), userId),
                        cb.greaterThan(book.get("start"), LocalDateTime.now())))
                .orderBy(cb.desc(book.get("end")));
        List<Booking> foundBooking = entityManager.createQuery(cr).getResultList();
        return foundBooking;
    }

    @Override
    public List<Booking> findCurrentItemBookingsByUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> book = cr.from(Booking.class);
        cr.select(book).where(cb.and(cb.equal(book.get("item").get("owner").get("id"), userId)),
                        cb.and(cb.greaterThan(book.get("end"), LocalDateTime.now()),
                                cb.lessThan(book.get("start"), LocalDateTime.now())))
                .orderBy(cb.desc(book.get("end")));
        List<Booking> foundBooking = entityManager.createQuery(cr).getResultList();
        return foundBooking;
    }

    @Override
    public boolean findBookingDates(Long itemId, LocalDateTime start, LocalDateTime end) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> book = cr.from(Booking.class);
        cr.select(book).where(cb.and(cb.equal(book.get("item").get("id"), itemId)),
                cb.or(cb.and
                                (cb.lessThanOrEqualTo(book.get("start"), start),
                                        (cb.greaterThanOrEqualTo(book.get("end"), start))),
                        cb.and(cb.lessThanOrEqualTo(book.get("start"), end),
                                cb.greaterThanOrEqualTo(book.get("end"), end))
                )
        );
        return !entityManager.createQuery(cr).getResultList().isEmpty();
    }

    @Override
    public Booking getLastItemBookings(Long itemId, Long ownerId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> book = cr.from(Booking.class);
        cr.select(book).where(cb.and(cb.equal(book.get("item").get("id"), itemId),
                        cb.lessThan(book.get("end"), LocalDateTime.now())))
                .orderBy(cb.asc(book.get("end")));
        List<Booking> foundBooking = entityManager.createQuery(cr).getResultList();
        if (foundBooking.isEmpty()) {
            return null;
        } else return foundBooking.get(0);
    }

    @Override
    public Booking getNextItemBookings(Long itemId, Long ownerId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> cr = cb.createQuery(Booking.class);
        Root<Booking> book = cr.from(Booking.class);
        cr.select(book).where(cb.and(cb.equal(book.get("item").get("id"), itemId),
                        cb.greaterThan(book.get("start"), LocalDateTime.now())))
                .orderBy(cb.desc(book.get("end")));
        List<Booking> foundBooking = entityManager.createQuery(cr).getResultList();
        if (foundBooking.isEmpty()) {
            return null;
        } else return foundBooking.get(0);
    }
}
