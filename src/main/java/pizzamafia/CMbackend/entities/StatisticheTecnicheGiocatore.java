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
    //=========FISICI========
    @Column(nullable = false)
    private int accelerazione;

    @Column(nullable = false)
    private int agilita;

    @Column(nullable = false)
    private int elevazione;

    @Column(nullable = false)
    private int forza;

    @Column(nullable = false)
    private int resistenza;

    @Column(nullable = false)
    private int scatto;

    @Column(nullable = false)
    private int inserimento;

    //=========MENTALI=======

    @Column(nullable = false)
    private int aggressivita;

    @Column(nullable = false)
    private int carisma;

    @Column(nullable = false)
    private int coraggio;

    @Column(nullable = false)
    private int creativita;

    @Column(nullable = false)
    private int determinazione;

    @Column(nullable = false)
    private int giocoDiSquadra;

    @Column(nullable = false)
    private int impegno;

    @Column(nullable = false)
    private int intuito;

    @Column(nullable = false)
    private int posizione;

    //=========TECNICI=======

    @Column(nullable = false)
    private int calciPiazzati;

    @Column(nullable = false)
    private int colpoDiTesta;

    @Column(nullable = false)
    private int contrasti;

    @Column(nullable = false)
    private int dribbling;

    @Column(nullable = false)
    private int finalizzazione;

    @Column(nullable = false)
    private int marcatura;

    @Column(nullable = false)
    private int riflessi;

    @Column(nullable = false)
    private int tecnica;

    @Column(nullable = false)
    private int assist;

    @Column(nullable = false)
    private int tiriDaLontano;

    // =================== RELAZIONI ===================
    @OneToOne
    @JoinColumn(name = "giocatore_id", nullable = false, unique = true)
    private Giocatore giocatore;



}
