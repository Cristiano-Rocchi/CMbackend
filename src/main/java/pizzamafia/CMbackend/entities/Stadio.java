package pizzamafia.CMbackend.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "Stadio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stadio {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String luogo;

    @Column(nullable = false)
    private Long capienza;
}
