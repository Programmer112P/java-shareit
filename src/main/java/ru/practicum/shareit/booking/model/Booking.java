package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Status;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime start;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonIgnore
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonIgnore
    private User booker;

    @Enumerated(EnumType.STRING)
    private Status status;

}
