package ru.practicum.shareit.requests;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.requests.model.ItemRequest;

@Repository
public interface ItemRequestRepositoryJpa extends JpaRepository<ItemRequest, Long> {

    @Query("select i from ItemRequest i where i.requestor.id = ?1")
    Page<ItemRequest> findItemRequestsByUser(Long userId, Pageable pageable);

    @Query("select i from ItemRequest i where i.requestor.id <> ?1")
    Page<ItemRequest> findItemRequestsByAnotherUsers(Long userId, Pageable pageable);
}