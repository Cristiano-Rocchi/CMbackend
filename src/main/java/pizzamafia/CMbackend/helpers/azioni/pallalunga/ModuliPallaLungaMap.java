package pizzamafia.CMbackend.helpers.azioni.pallalunga;

import pizzamafia.CMbackend.enums.Modulo;
import pizzamafia.CMbackend.enums.Ruolo;

import java.util.List;
import java.util.Map;

public class ModuliPallaLungaMap {

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
                    Ruolo.ATTACCANTE_ESTERNO        // Ricevente lungo / Dribbling / Tiro
            ),
            Modulo._4_3_3_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore lungo
                    Ruolo.ATTACCANTE_ESTERNO        // Ricevente lungo / Dribbling / Tiro

            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore lungo
                    Ruolo.ATTACCANTE_ESTERNO        // Ricevente lungo / Dribbling / Tiro

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
                    Ruolo.ATTACCANTE_ESTERNO,       // Ricevente lungo / Dribbling / Cross
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            ),
            Modulo._4_3_3_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,      //Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE, // Ricevente 1 / Passatore lungo
                    Ruolo.ATTACCANTE_ESTERNO,      // Ricevente lungo / Dribbling / Cross
                    Ruolo.BOMBER                   // Ricevente cross / Colpo di testa
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,      //Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE, // Ricevente 1 / Passatore lungo
                    Ruolo.ATTACCANTE_ESTERNO,      // Ricevente lungo / Dribbling / Cross
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
                    Ruolo.ATTACCANTE_ESTERNO        // Ricevente lungo / Dribbling / Tiro
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
