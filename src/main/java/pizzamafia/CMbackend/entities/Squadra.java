package pizzamafia.CMbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "squadre")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Squadra {

    // =================== ID ===================
    @Id
    @GeneratedValue
    private UUID id;

    // =================== ATTRIBUTI ===================
    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String coloriSociali;

    @Column(nullable = false)
    private String magliaColorePrimario;

    @Column(nullable = false)
    private String magliaColoreSecondario;

    @Column(nullable = false)
    private Integer valoreTecnicoTotale;

    // =================== RELAZIONE con STADIO ===================
    @ManyToOne
    @JoinColumn(name = "stadio_id", nullable = false)
    private Stadio stadio;

    // =================== RELAZIONE con GIOCATORI ===================
    @OneToMany(mappedBy = "squadra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Giocatore> giocatori;
}
