package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

@Repository
public interface ItemRepositoryJpa extends JpaRepository<Item, Long> {
    @Query(" select i from Item i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%'))   " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))" +
            " and i.available = true"
    )
    Page<Item> search(String text, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.owner.id = ?1 ORDER BY i.id ASC")
    Page<Item> getItemsByOwner(long userId, Pageable pageable);

}