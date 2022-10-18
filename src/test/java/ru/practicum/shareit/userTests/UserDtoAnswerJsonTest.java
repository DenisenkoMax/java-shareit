package ru.practicum.shareit.userTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDtoAnswer;
import java.io.IOException;
import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
public class UserDtoAnswerJsonTest {
    @Autowired
    private JacksonTester<UserDtoAnswer> jacksonTester;

    @Test
    void serializedTest() throws IOException {
        UserDtoAnswer userDtoAnswer = new UserDtoAnswer(1L,"name", "name@dsd.ru");
        JsonContent<UserDtoAnswer> result = jacksonTester.write(userDtoAnswer);
        then(result).extractingJsonPathValue("$.id").isEqualTo(1);
        then(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        then(result).extractingJsonPathStringValue("$.email").isEqualTo("name@dsd.ru");
    }
}
