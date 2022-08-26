package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;

import java.util.*;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users;
    private static long userId;

    public UserRepositoryImpl() {
        this.users = new HashMap<>();
        userId = 0L;
    }

    private long generateId() {
        return ++userId;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) throws ValidationException {
      validateNoExistEmail(user);
        user.setId(generateId());
        users.put(userId, user);
        return user;
    }

    @Override
    public Optional<User> findUserById(long id) {
        return Optional.ofNullable(users.get(id));

    }
    @Override
    public boolean deleteUserById(long id) {
        return users.remove(id) != null;
    }

    @Override
    public Optional<User> update(User user) throws ValidationException {

        if (users.containsKey(user.getId())) {
            if(user.getName()!=null){
                users.get(user.getId()).setName(user.getName());
            }
            if(user.getEmail()!=null){
                validateNoExistEmail(user);
                users.get(user.getId()).setEmail(user.getEmail());
            }

            return Optional.of(users.get(user.getId()));
        }
        return Optional.empty();
    }

    private void validateNoExistEmail(User user) throws ValidationException {
        if (users.values().stream().filter(p -> p.getEmail().equals(user.getEmail())).count() > 0) {
            throw new ValidationException("dublicate email");
        }
    }

}
