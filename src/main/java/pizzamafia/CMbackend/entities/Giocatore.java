package pizzamafia.CMbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import pizzamafia.CMbackend.enums.Piede;
import pizzamafia.CMbackend.enums.Ruolo;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "giocatori")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Giocatore {

    // =================== ID ===================
    @Id
    @GeneratedValue
    private UUID id;

    // =================== DATI ANAGRAFICI ===================
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false)
    private LocalDate dataDiNascita;

    @Column(nullable = false)
    private String nazionalita;

    @Column(nullable = false)
    private Integer altezza;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Piede piede;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ruolo ruolo;

    // =================== VALORE TECNICO ===================
    @Column(nullable = false)
    private Integer valoreTecnico;

    // =================== RELAZIONE con SQUADRA ===================
    @ManyToOne
    @JoinColumn(name = "squadra_id", nullable = false)
    private Squadra squadra;

    // =================== RELAZIONE con STATISTICHE ===================
    @OneToOne(mappedBy = "giocatore", cascade = CascadeType.ALL, orphanRemoval = true)
    private StatisticheTecnicheGiocatore statistiche;


}
