package ru.practicum.shareit.requests.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ItemRequest implements Serializable {
    public ItemRequest(Long id, String description, User requestor, LocalDateTime created, Set<Item> items) {
        this.id = id;
        this.description = description;
        this.requestor = requestor;
        this.created = created;
        this.items = items;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id")
    private User requestor;
    @Column(name = "created")
    private LocalDateTime created;
    @OneToMany(mappedBy = "request")
    @ToString.Exclude
    private Set<Item> items = new HashSet<>();


}
