package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;


/**
 * // TODO .
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(
        value = "/users",
        consumes = MediaType.ALL_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) throws ValidationException {
        return userService.createUser(userDto).map(newUser -> new ResponseEntity<>(newUser, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable long id) {
        return userService.findUserById(id).map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable long id) {
        return userService.deleteUserById(id) ? new ResponseEntity<>(null, HttpStatus.OK)
                : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserDto userDto,
                                            @PathVariable long userId) throws ValidationException {

        return userService.updateUser(userDto, userId).map(userResult -> new ResponseEntity<>(userResult,
                        HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}
