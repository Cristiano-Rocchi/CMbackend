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

    //=========TECNICHE=======

    @Column(nullable = false)
    private int tecnica;

    @Column(nullable = false)
    private int equilibrio;

    @Column(nullable = false)
    private int colpoDiTesta;

    @Column(nullable = false)
    private int tiro;

    @Column(nullable = false)
    private int assist;

    @Column(nullable = false)
    private int finalizzazione;

    @Column(nullable = false)
    private int dribbling;

    @Column(nullable = false)
    private int visione;

    @Column(nullable = false)
    private int calciPiazzati;

    @Column(nullable = false)
    private int cross;

    @Column(nullable = false)
    private int contrasti;

    @Column(nullable = false)
    private int marcatura;

    @Column(nullable = false)
    private int intercettazione;

    //=========MENTALI=======

    @Column(nullable = false)
    private int carisma;

    @Column(nullable = false)
    private int concentrazione;

    @Column(nullable = false)
    private int coraggio;

    @Column(nullable = false)
    private int leadership;

    @Column(nullable = false)
    private int letturaDelGioco;

    @Column(nullable = false)
    private int giocoDiSquadra;

    @Column(nullable = false)
    private int creativita;

    @Column(nullable = false)
    private int freddezza;

    @Column(nullable = false)
    private int aggressivita;

    //=========FISICHE========

    @Column(nullable = false)
    private int accelerazione;

    @Column(nullable = false)
    private int scatto;

    @Column(nullable = false)
    private int elevazione;

    @Column(nullable = false)
    private int forzaFisica;

    @Column(nullable = false)
    private int resistenza;

    //========PORTIERE=======

    @Column(nullable = false)
    private int tuffo;

    @Column(nullable = false)
    private int riflessi;

    @Column(nullable = false)
    private int posizione;

    @Column(nullable = false)
    private int uscite;

    @Column(nullable = false)
    private int presa;

    //=========VARIABILI=========







    // =================== RELAZIONI ===================
    @OneToOne
    @JoinColumn(name = "giocatore_id", nullable = false, unique = true)
    private Giocatore giocatore;



}
