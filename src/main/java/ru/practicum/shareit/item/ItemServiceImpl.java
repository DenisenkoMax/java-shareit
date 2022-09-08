package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(@Qualifier("itemRepositoryImpl") ItemRepository itemRepository,
                           @Qualifier("userRepositoryImpl") UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Item> create(ItemDto itemDto, long userId) {
        if (!userRepository.findUserById(userId).isPresent()) {
            return Optional.empty();
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userId);
        return Optional.ofNullable(itemRepository.create(item));
    }

    @Override
    public Optional<Item> updateItem(ItemDto itemDto, long itemId, long userId) {
        Item item = ItemMapper.toItem(itemDto);
        item.setId(itemId);
        return itemRepository.update(item, userId);
    }

    @Override
    public Optional<Item> findItemById(long itemId) {
        if (itemRepository.findItemById(itemId).isPresent()) {
            return itemRepository.findItemById(itemId);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Item> getItemsByOwner(long userId) {
        return itemRepository.getItemsByOwner(userId);
    }

    @Override
    public List<Item> search(String text) {
        return itemRepository.search(text);
    }
}