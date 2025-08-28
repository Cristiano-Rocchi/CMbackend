package pizzamafia.CMbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pizzamafia.CMbackend.enums.IdeaDiGioco;

import java.util.UUID;

@Entity
@Table(name = "allenatori")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Allenatore {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdeaDiGioco ideaDiGioco;

    // Relazione inversa (opzionale, ma utile se vuoi navigare da Allenatore → Squadra)
    @OneToOne(mappedBy = "allenatore")
    private Squadra squadra;
}

