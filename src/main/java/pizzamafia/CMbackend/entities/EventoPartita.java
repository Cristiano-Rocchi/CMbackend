package pizzamafia.CMbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.UUID;

@Entity
@Table(name = "evento_partita")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoPartita {

    // ========== ID ==========
    @Id
    @GeneratedValue
    private UUID id;

    // ========== INFO EVENTO ==========
    @Column(nullable = false)
    private int minuto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvento tipoEvento;

    @Column(nullable = false)
    private String esito;

    @Column
    private String note;

    // ========== RELAZIONI ==========

    @ManyToOne(optional = false)
    @JoinColumn(name = "partita_id")
    private Partita partita;

    @ManyToOne(optional = false)
    @JoinColumn(name = "giocatore_principale_id")
    private Giocatore giocatorePrincipale;

    @ManyToOne
    @JoinColumn(name = "giocatore_secondario_id")
    private Giocatore giocatoreSecondario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "squadra_id")
    private Squadra squadra;
}
