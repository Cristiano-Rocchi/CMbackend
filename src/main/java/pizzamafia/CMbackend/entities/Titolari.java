package pizzamafia.CMbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import pizzamafia.CMbackend.enums.Ruolo;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Titolari {

    @Id
    @GeneratedValue
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formazione_id", nullable = false)
    private Formazione formazione;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giocatore_id", nullable = false)
    private Giocatore giocatore;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ruolo ruolo;

    // =================== VALORE EFFETTIVO USATO NEL CALCOLO ===================
    @Column(nullable = false)
    private int malus;


    @Column(nullable = false)
    private int valoreEffettivo;
}
