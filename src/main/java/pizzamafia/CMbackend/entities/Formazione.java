package pizzamafia.CMbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import pizzamafia.CMbackend.enums.MentalitaTattica;
import pizzamafia.CMbackend.enums.Modulo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    //TITOLARI
    @OneToMany(mappedBy = "formazione", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Titolari> titolari;

    //PANCHINA
    @ManyToMany
    @JoinTable(
            name = "formazione_panchina",
            joinColumns = @JoinColumn(name = "formazione_id"),
            inverseJoinColumns = @JoinColumn(name = "giocatore_id")
    )
    private Set<Giocatore> panchina;

    //TATTICHE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MentalitaTattica mentalita = MentalitaTattica.BILANCIATA;

    // ===== CALCI PIAZZATI =====

    // 1) RIGORI
    @ElementCollection
    @CollectionTable(name = "formazione_rigoristi", joinColumns = @JoinColumn(name = "formazione_id"))
    @Column(name = "giocatore_id", nullable = false)
    @OrderColumn(name = "posizione")
    @Builder.Default
    private List<UUID> rigoristi = new ArrayList<>();

    // 2) PUNIZIONE RAVVICINATA
    @ElementCollection
    @CollectionTable(name = "formazione_pun_ravvicinata_centrale", joinColumns = @JoinColumn(name = "formazione_id"))
    @Column(name = "giocatore_id", nullable = false)
    @OrderColumn(name = "posizione")
    @Builder.Default
    private List<UUID> punizioneRavvicinataCentrale = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "formazione_pun_ravvicinata_destra", joinColumns = @JoinColumn(name = "formazione_id"))
    @Column(name = "giocatore_id", nullable = false)
    @OrderColumn(name = "posizione")
    @Builder.Default
    private List<UUID> punizioneRavvicinataDestra = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "formazione_pun_ravvicinata_sinistra", joinColumns = @JoinColumn(name = "formazione_id"))
    @Column(name = "giocatore_id", nullable = false)
    @OrderColumn(name = "posizione")
    @Builder.Default
    private List<UUID> punizioneRavvicinataSinistra = new ArrayList<>();

    // 3) PUNIZIONE LONTANA
    @ElementCollection
    @CollectionTable(name = "formazione_pun_lontana", joinColumns = @JoinColumn(name = "formazione_id"))
    @Column(name = "giocatore_id", nullable = false)
    @OrderColumn(name = "posizione")
    @Builder.Default
    private List<UUID> punizioneLontana = new ArrayList<>();

}

