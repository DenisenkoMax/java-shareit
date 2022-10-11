package ru.practicum.shareit.item.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.user.model.User;

@Repository
public interface UserRepositoryJpa extends JpaRepository<User, Long> {

}
