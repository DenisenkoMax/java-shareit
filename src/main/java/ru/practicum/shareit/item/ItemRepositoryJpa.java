package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepositoryJpa extends JpaRepository<Item, Long> {
    @Query(" select i from Item i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%'))   " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))" +
            " and i.available = true"
    )
    List<Item> search(String text);

    @Query(" select i from Item i " +
            "where upper(i.owner.id) like upper(?1) ")
    List<Item> getItemsByOwner(long userId);

}