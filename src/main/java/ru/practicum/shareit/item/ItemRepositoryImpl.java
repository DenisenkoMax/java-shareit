package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private HashMap<Long, Item> items;
    private static long itemId;

    public ItemRepositoryImpl() {
        this.items = new HashMap<>();
        itemId = 0L;
    }

    private long generateId() {
        return ++itemId;
    }

    @Override
    public Item create(Item item) {
        item.setId(generateId());
        items.put(itemId, item);
        return item;
    }

    @Override
    public Optional<Item> findItemById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getItemsByOwner(long userId) {
        return items.values().stream().filter(p -> p.getOwner() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        return items.values().stream().filter(p -> ((p.getName().toLowerCase().contains(text.toLowerCase()) ||
                        p.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                        p.getAvailable()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> update(Item item, long userId) {
        if (items.containsKey(item.getId())) {
            if (items.get(item.getId()).getOwner() == userId) {

                if (item.getName() != null) {
                    items.get(item.getId()).setName(item.getName());
                }
                if (item.getDescription() != null) {
                    items.get(item.getId()).setDescription(item.getDescription());
                }
                if (item.getAvailable() != null) {
                    items.get(item.getId()).setAvailable(item.getAvailable());
                }
                if (item.getOwner() != null) {
                    items.get(item.getId()).setOwner(item.getOwner());
                }
                if (item.getRequest() != null) {
                    items.get(item.getId()).setRequest(item.getRequest());
                }
                return Optional.ofNullable(items.get(item.getId()));
            } else return Optional.empty();
        } else return Optional.empty();
    }
}