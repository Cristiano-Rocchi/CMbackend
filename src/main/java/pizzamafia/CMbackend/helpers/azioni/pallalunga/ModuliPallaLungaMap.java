package pizzamafia.CMbackend.helpers.azioni.pallalunga;

import pizzamafia.CMbackend.enums.Modulo;
import pizzamafia.CMbackend.enums.Ruolo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ModuliPallaLungaMap {

    private static final Random RND = new Random();

    /** Restituisce una variante valida (1..4) disponibile per il modulo passato. */
    public static int scegliVariante(Modulo modulo) {
        List<Integer> disponibili = new ArrayList<>();
        if (PALLALUNGA1_MAP.containsKey(modulo)) disponibili.add(1);
        if (PALLALUNGA2_MAP.containsKey(modulo)) disponibili.add(2);
        if (PALLALUNGA3_MAP.containsKey(modulo)) disponibili.add(3);
        if (PALLALUNGA4_MAP.containsKey(modulo)) disponibili.add(4);
        if (disponibili.isEmpty()) return 1; // fallback di sicurezza
        return disponibili.get(RND.nextInt(disponibili.size()));
    }

    /** Lista ruoli per la variante indicata e il modulo dato. */
    public static List<Ruolo> ruoliPer(Modulo modulo, int variante) {
        return switch (variante) {
            case 1 -> PALLALUNGA1_MAP.getOrDefault(modulo, List.of());
            case 2 -> PALLALUNGA2_MAP.getOrDefault(modulo, List.of());
            case 3 -> PALLALUNGA3_MAP.getOrDefault(modulo, List.of());
            case 4 -> PALLALUNGA4_MAP.getOrDefault(modulo, List.of());
            default -> List.of();
        };
    }

    // ----------------- PALLA LUNGA 1 --------------------
    public static final Map<Modulo, List<Ruolo>> PALLALUNGA1_MAP = Map.of(
            Modulo._4_4_2_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore lungo
                    Ruolo.SECONDA_PUNTA             // Ricevente lungo / Dribbling / Tiro
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_DIFENSIVO, // Ricevente 1 / Passatore lungo
                    Ruolo.ATTACCANTE_ESTERNO_DX        // Ricevente lungo / Dribbling / Tiro
            ),
            Modulo._4_3_3_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore lungo
                    Ruolo.ATTACCANTE_ESTERNO_DX        // Ricevente lungo / Dribbling / Tiro

            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore lungo
                    Ruolo.ATTACCANTE_ESTERNO_DX        // Ricevente lungo / Dribbling / Tiro

            )
    );

    // ----------------- PALLA LUNGA 2 --------------------
    public static final Map<Modulo, List<Ruolo>> PALLALUNGA2_MAP = Map.of(
            Modulo._4_4_2_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore lungo
                    Ruolo.SECONDA_PUNTA,            // Ricevente lungo / Dribbling / Cross
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_DIFENSIVO, // Ricevente 1 / Passatore lungo
                    Ruolo.ATTACCANTE_ESTERNO_DX,       // Ricevente lungo / Dribbling / Cross
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            ),
            Modulo._4_3_3_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,      //Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE, // Ricevente 1 / Passatore lungo
                    Ruolo.ATTACCANTE_ESTERNO_DX,      // Ricevente lungo / Dribbling / Cross
                    Ruolo.BOMBER                   // Ricevente cross / Colpo di testa
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,      //Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE, // Ricevente 1 / Passatore lungo
                    Ruolo.ATTACCANTE_ESTERNO_DX,      // Ricevente lungo / Dribbling / Cross
                    Ruolo.BOMBER                   // Ricevente cross / Colpo di testa
            )
    );

    // ----------------- PALLA LUNGA 3 --------------------
    public static final Map<Modulo, List<Ruolo>> PALLALUNGA3_MAP = Map.of(
            Modulo._4_4_2_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Dribbling / Passatore lungo
                    Ruolo.SECONDA_PUNTA             // Ricevente lungo / Dribbling / Tiro
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE, // Ricevente 1 / Dribbling / Passatore lungo
                    Ruolo.BOMBER                    // Ricevente lungo / Dribbling / Tiro
            ),
            Modulo._4_3_3_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE, // Ricevente 1 / Dribbling / Passatore lungo
                    Ruolo.BOMBER                    // Ricevente lungo / Dribbling / Tiro
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE, // Ricevente 1 / Dribbling / Passatore lungo
                    Ruolo.BOMBER                    // Ricevente lungo / Dribbling / Tiro
            )
    );

    // ----------------- PALLA LUNGA 4 --------------------
    public static final Map<Modulo, List<Ruolo>> PALLALUNGA4_MAP = Map.of(
            Modulo._4_4_2_1, List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Dribbling / Passatore lungo
                    Ruolo.BOMBER                    // Ricevente lungo / Dribbling / Tiro
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Dribbling / Passatore lungo
                    Ruolo.ATTACCANTE_ESTERNO_SX        // Ricevente lungo / Dribbling / Tiro
            ),
            Modulo._4_3_3_1, List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Dribbling / Passatore lungo
                    Ruolo.BOMBER                    // Ricevente lungo / Dribbling / Tiro
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Dribbling / Passatore lungo
                    Ruolo.BOMBER                   // Ricevente lungo / Dribbling / Tiro
            )
    );
}
