package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.CommentDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
public class CommentDtoJsonTest {
    @Autowired
    private JacksonTester<CommentDto> jacksonTester;

    @Test
    void serializedTest() throws IOException {
        LocalDateTime localDateTime = LocalDateTime.of(2022, Month.OCTOBER, 9, 0, 0);
        CommentDto commentDto = new CommentDto(1L, "text", 1L, "name", localDateTime);
        JsonContent<CommentDto> result = jacksonTester.write(commentDto);
        then(result).extractingJsonPathValue("$.id").isEqualTo(1);
        then(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        then(result).extractingJsonPathValue("$.itemId").isEqualTo(1);
        then(result).extractingJsonPathStringValue("$.authorName").isEqualTo("name");
        then(result).extractingJsonPathStringValue("$.created").isEqualTo("2022-10-09T00:00:00");
    }
}
