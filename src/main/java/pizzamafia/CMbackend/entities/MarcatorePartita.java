package pizzamafia.CMbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarcatorePartita {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partita_id", nullable = false)
    private Partita partita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giocatore_id", nullable = false)
    private Giocatore giocatore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squadra_id", nullable = false)
    private Squadra squadra;

    @Column(nullable = false)
    private int minuto;

}
