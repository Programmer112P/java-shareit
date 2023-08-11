package ru.practicum.shareit.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    public User(long id) {
        this.id = id;
    }

    public User(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Email
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    @ToString.Exclude
    @JsonIgnore
    List<Item> items;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "requestor")
    @ToString.Exclude
    @JsonIgnore
    List<ItemRequest> itemRequests;

}
