package pizzamafia.CMbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "statistiche_tecniche_giocatore")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticheTecnicheGiocatore {

    // =================== ID ===================
    @Id
    @GeneratedValue
    private UUID id;

    // =================== STATISTICHE ===================
    @Column(nullable = false)
    private Integer attacco;

    @Column(nullable = false)
    private Integer difesa;

    @Column(nullable = false)
    private Integer velocita;

    @Column(nullable = false)
    private Integer tiro;

    @Column(nullable = false)
    private Integer passaggio;

    @Column(nullable = false)
    private Integer portiere;

    // =================== RELAZIONE con GIOCATORE ===================
    @OneToOne
    @JoinColumn(name = "giocatore_id", nullable = false, unique = true)
    private Giocatore giocatore;
}
