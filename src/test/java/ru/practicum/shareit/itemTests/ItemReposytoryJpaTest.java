package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ItemReposytoryJpaTest {
    @Autowired
    private ItemRepositoryJpa itemRepositoryJpa;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void searchTest() {
        User user1 = new User(null, "user1", "user1@gh.ru", null, null, null);
        entityManager.persist(user1);
        entityManager.persist(user1);
        Item item1 = new Item(null, "лобзик", "новый", user1, true, null);
        entityManager.persist(item1);
        Item item2 = new Item(null, "пылесос", "новый", user1, true, null);
        entityManager.persist(item2);
        assertThat(item1.getName()).isEqualTo(itemRepositoryJpa.search("лобз", PageRequest.of(0, 10))
                .getContent().get(0).getName());
    }

    @Test
    public void getItemsByOwner() {
        User user1 = new User(null, "user1", "user1@gh.ru", null, null, null);
        entityManager.persist(user1);
        Item item1 = new Item(null, "лобзик", "новый", user1, true, null);
        entityManager.persist(item1);
        Item item2 = new Item(null, "пылесос", "новый", user1, true, null);
        entityManager.persist(item2);
        assertThat(item1.getName()).isEqualTo(itemRepositoryJpa.getItemsByOwner(user1.getId(),
                PageRequest.of(0, 10)).getContent().get(0).getName());
        assertThat(item2.getName()).isEqualTo(itemRepositoryJpa.getItemsByOwner(user1.getId(),
                PageRequest.of(0, 10)).getContent().get(1).getName());
    }
}
