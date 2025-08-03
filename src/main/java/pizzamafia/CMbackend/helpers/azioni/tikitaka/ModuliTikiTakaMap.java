package pizzamafia.CMbackend.helpers.azioni.tikitaka;

import pizzamafia.CMbackend.enums.Ruolo;

import java.util.List;
import java.util.Map;

public class ModuliTikiTakaMap {

    //-------------------TIKITAKA.1------------------------------
    public static final Map<String, List<Ruolo>> TIKITAKA1_MAP = Map.of(
            "4-4-2(1)", List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO,                  // Ricevente 1 / Passatore 2
                    Ruolo.CENTROCAMPISTA_CENTRALE, // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_CENTRALE, // Ricevente 3 / Dribbling / Passatore 4
                    Ruolo.SECONDA_PUNTA             // Ricevente 4 / Tiro
            ),
            "4-4-2(2)", List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO,                  // Ricevente 1 / Passatore 2
                    Ruolo.CENTROCAMPISTA_DIFENSIVO, // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_OFFENSIVO, // Ricevente 3 / Dribbling / Passatore 4
                    Ruolo.SECONDA_PUNTA            // Ricevente 4 / Tiro
            )
            // altri moduli qui...
    );

    //-------------------TIKITAKA.2------------------------------
    public static final Map<String, List<Ruolo>> TIKITAKA2_MAP = Map.of(
            "4-4-2(1)", List.of(
                    Ruolo.DIFENSORE_CENTRALE,         // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,    // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO,                    // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 3 / Passatore 4 / Ricevente 5 / Tiro
                    Ruolo.SECONDA_PUNTA               // Ricevente 4 / Passatore 5
            ),
            "4-4-2(2)", List.of(
                    Ruolo.DIFENSORE_CENTRALE,         // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,    // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO,                    // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_OFFENSIVO,   // Ricevente 3 / Passatore 4 / Ricevente 5 / Tiro
                    Ruolo.SECONDA_PUNTA               // Ricevente 4 / Passatore 5
            )
            // altri moduli qui...
    );

    //---------------------TIKITAKA.3--------------------------------
    public static final Map<String, List<Ruolo>> TIKITAKA3_MAP = Map.of(
            "4-4-2(1)", List.of(
                    Ruolo.DIFENSORE_CENTRALE,        // Passatore 1 / Ricevente 3 / Passatore 4
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO,                   // Ricevente 2 / Passatore 3
                    Ruolo.ALA,                   // Ricevente 4 / Passatore lungo
                    Ruolo.SECONDA_PUNTA,        // Ricevente lungo / Cross per bomber
                    Ruolo.BOMBER                     // Ricevente cross / Colpo di testa
            ),
            "4-4-2(2)", List.of(
                    Ruolo.DIFENSORE_CENTRALE,        // Passatore 1 / Ricevente 3 / Passatore 4
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO,                   // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_OFFENSIVO,  // Ricevente 4 / Passatore lungo
                    Ruolo.SECONDA_PUNTA,        // Ricevente lungo / Cross per bomber
                    Ruolo.BOMBER                     // Ricevente cross / Colpo di testa
            )
    );
    //---------------------TIKITAKA.4--------------------------------
    public static final Map<String, List<Ruolo>> TIKITAKA4_MAP = Map.of(
            "4-4-2(1)", List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,     // Passatore 1
                    Ruolo.TERZINO,                     // Ricevente 1 / Passatore 2
                    Ruolo.ALA,                         // Ricevente 2 / Passatore 3
                    Ruolo.DIFENSORE_CENTRALE,          // Ricevente 3 / Passatore 4
                    Ruolo.CENTROCAMPISTA_CENTRALE,    // Ricevente 4 / Passatore 5 / Passatore 6
                    Ruolo.SECONDA_PUNTA,               // Ricevente 6 / Dribbling / Tiro
                    Ruolo.CENTROCAMPISTA_CENTRALE     // Ricevente 5 (può essere lo stesso o un altro)
            ),
            "4-4-2(2)", List.of(
                    Ruolo.CENTROCAMPISTA_DIFENSIVO,     // Passatore 1
                    Ruolo.TERZINO,                     // Ricevente 1 / Passatore 2
                    Ruolo.CENTROCAMPISTA_CENTRALE,    // Ricevente 2 / Passatore 3
                    Ruolo.DIFENSORE_CENTRALE,          // Ricevente 3 / Passatore 4
                    Ruolo.CENTROCAMPISTA_CENTRALE,    // Ricevente 4 / Passatore 5 / Passatore 6
                    Ruolo.SECONDA_PUNTA,               // Ricevente 6 / Dribbling / Tiro
                    Ruolo.CENTROCAMPISTA_OFFENSIVO     // Ricevente 5
            )
    );

    //---------------------TIKITAKA.5--------------------------------
    public static final Map<String, List<Ruolo>> TIKITAKA5_MAP = Map.of(
            "4-4-2(1)", List.of(
                    Ruolo.ALA,                         // Passatore 1 / Ricevente 3 / Passatore 4 / Ricevente 6 / Tiro
                    Ruolo.CENTROCAMPISTA_CENTRALE,    // Ricevente 1 / Passatore 2
                    Ruolo.SECONDA_PUNTA,              // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_CENTRALE     // Ricevente 4 / Dribbling / Passatore 6
            ),
            "4-4-2(2)", List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,     // Passatore 1 / Ricevente 3 / Passatore 4 / Ricevente 6 / Tiro
                    Ruolo.CENTROCAMPISTA_DIFENSIVO,    // Ricevente 1 / Passatore 2
                    Ruolo.ATTACCANTE_ESTERNO,          // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_OFFENSIVO     // Ricevente 4 / Dribbling / Passatore 6
            )


    );






}
