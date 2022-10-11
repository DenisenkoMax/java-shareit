package ru.practicum.shareit.userTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @Test
    void serializedTest() throws IOException {
        UserDto userDto = new UserDto("name", "name@dsd.ru");
        JsonContent<UserDto> result = jacksonTester.write(userDto);
        then(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        then(result).extractingJsonPathStringValue("$.email").isEqualTo("name@dsd.ru");
    }
}
