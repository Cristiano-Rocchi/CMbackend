package pizzamafia.CMbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import pizzamafia.CMbackend.enums.Competizione;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partita {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squadra_casa_id", nullable = false)
    private Squadra squadraCasa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squadra_trasferta_id", nullable = false)
    private Squadra squadraTrasferta;

    @Column(nullable = false)
    private int goalCasa;

    @Column(nullable = false)
    private int goalTrasferta;

    @Column(nullable = false)
    private LocalDateTime dataOra;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Competizione competizione;

    @OneToMany(mappedBy = "partita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MarcatorePartita> marcatori;

}

