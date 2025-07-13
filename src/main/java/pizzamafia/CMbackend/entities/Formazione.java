package pizzamafia.CMbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import pizzamafia.CMbackend.enums.Modulo;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Formazione {

    @Id
    @GeneratedValue
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partita_id", nullable = false)
    private Partita partita;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squadra_id", nullable = false)
    private Squadra squadra;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modulo modulo;

    @OneToMany(mappedBy = "formazione", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Titolari> titolari;
}
