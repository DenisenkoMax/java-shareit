package ru.practicum.shareit.itemRequestControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.io.IOException;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
public class RequestDtoJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDto> jacksonTester;

    @Test
    void serializedTest() throws IOException {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L,"description");
        JsonContent<ItemRequestDto> result = jacksonTester.write(itemRequestDto);
        then(result).extractingJsonPathValue("$.id").isEqualTo(1);
        then(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
    }
}
