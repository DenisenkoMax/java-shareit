package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    private JacksonTester<ItemDto> jacksonTester;

    @Test
    void serializedTest() throws IOException {
        ItemDto itemDto = new ItemDto(1L, "name", "description", true, 1L);
        JsonContent<ItemDto> result = jacksonTester.write(itemDto);
        then(result).extractingJsonPathValue("$.id").isEqualTo(1);
        then(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        then(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        then(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        then(result).extractingJsonPathValue("$.requestId").isEqualTo(1);
    }
}
