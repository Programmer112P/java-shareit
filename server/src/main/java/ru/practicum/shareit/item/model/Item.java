package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    public Item(Long id) {
        this.id = id;
    }

    public Item(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.owner = item.getOwner();
        this.available = item.getAvailable();
        this.description = item.getDescription();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    @ToString.Exclude
    @JsonIgnore
    private List<Booking> bookings;

    @Transient
    private Booking lastBooking;

    @Transient
    private Booking nextBooking;

    @Column(name = "is_available")
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonIgnore
    private User owner;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    @ToString.Exclude
    @JsonIgnore
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonIgnore
    private ItemRequest request;

}
