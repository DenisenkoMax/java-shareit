package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validation.Validation;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";
    private final Validation validation;

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder, Validation validation) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        this.validation = validation;
    }

    public ResponseEntity<Object> createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        return post("/", userId, itemRequestDto);
    }

    public ResponseEntity<Object> findItemRequestById(Long userId, Long id) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> findUserOwnerItemRequests(Long userId, int from, int size) throws IllegalArgumentEx {
        validation.validatePagination(size, from);
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/", userId, parameters);
    }

    public ResponseEntity<Object> findAnotherUsersItemRequests(Long userId, int from, int size)
            throws IllegalArgumentEx {
        validation.validatePagination(size, from);
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }

}
