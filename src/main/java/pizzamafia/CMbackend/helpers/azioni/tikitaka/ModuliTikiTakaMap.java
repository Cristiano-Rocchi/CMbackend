package pizzamafia.CMbackend.helpers.azioni.tikitaka;

import pizzamafia.CMbackend.enums.Modulo;
import pizzamafia.CMbackend.enums.Ruolo;

import java.util.List;
import java.util.Map;

public class ModuliTikiTakaMap {

    //1.===============TIKITAKA 1================
    public static final Map<Modulo, List<Ruolo>> TIKITAKA1_MAP = Map.of(
            //1.1 ------442------
            Modulo._4_4_2_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO,                  // Ricevente 1 / Passatore 2
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 3 / Dribbling / Passatore 4
                    Ruolo.SECONDA_PUNTA             // Ricevente 4 / Tiro
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO,                  // Ricevente 1 / Passatore 2
                    Ruolo.CENTROCAMPISTA_DIFENSIVO, // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_OFFENSIVO, // Ricevente 3 / Dribbling / Passatore 4
                    Ruolo.SECONDA_PUNTA             // Ricevente 4 / Tiro
            ),
            //1.2 ------433------
            Modulo._4_3_3_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO,                  // Ricevente 1 / Passatore 2
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_OFFENSIVO, // Ricevente 3 / Dribbling / Passatore 4
                    Ruolo.BOMBER                    // Ricevente 4 / Tiro
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO,                  // Ricevente 1 / Passatore 2
                    Ruolo.CENTROCAMPISTA_DIFENSIVO,  // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 3 / Dribbling / Passatore 4
                    Ruolo.BOMBER                    // Ricevente 4 / Tiro
            )
    );

    //2.===============TIKITAKA 2================
    public static final Map<Modulo, List<Ruolo>> TIKITAKA2_MAP = Map.of(
            //2.1 ------442-----
            Modulo._4_4_2_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,         // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,    // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO,                    // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_CENTRALE,    // Ricevente 3 / Passatore 4 / Ricevente 5 / Tiro
                    Ruolo.SECONDA_PUNTA               // Ricevente 4 / Passatore 5
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,         // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,    // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO,                    // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_OFFENSIVO,   // Ricevente 3 / Passatore 4 / Ricevente 5 / Tiro
                    Ruolo.SECONDA_PUNTA               // Ricevente 4 / Passatore 5
            ),
            //2.2 ------433------
            Modulo._4_3_3_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,         // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,    // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO,                    // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_OFFENSIVO,   // Ricevente 3 / Passatore 4 / Ricevente 5 / Tiro
                    Ruolo.BOMBER                      // Ricevente 4 / Passatore 5
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,         // Passatore 1
                    Ruolo.CENTROCAMPISTA_DIFENSIVO,   // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO,                    // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_CENTRALE,    // Ricevente 3 / Passatore 4 / Ricevente 5 / Tiro
                    Ruolo.BOMBER                      // Ricevente 4 / Passatore 5
            )
    );

    //===============TIKITAKA 3================
    public static final Map<Modulo, List<Ruolo>> TIKITAKA3_MAP = Map.of(
            //3.1------442-----
            Modulo._4_4_2_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,        // Passatore 1 / Ricevente 3 / Passatore 4
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO,                   // Ricevente 2 / Passatore 3
                    Ruolo.ALA,                       // Ricevente 4 / Passatore lungo
                    Ruolo.SECONDA_PUNTA,             // Ricevente lungo / Cross per bomber
                    Ruolo.BOMBER                     // Ricevente cross / Colpo di testa
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,        // Passatore 1 / Ricevente 3 / Passatore 4
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO,                   // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_OFFENSIVO,  // Ricevente 4 / Passatore lungo
                    Ruolo.SECONDA_PUNTA,             // Ricevente lungo / Cross per bomber
                    Ruolo.BOMBER                     // Ricevente cross / Colpo di testa
            ),
            //3.2 ------433------
            Modulo._4_3_3_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,        // Passatore 1 / Ricevente 3 / Passatore 4
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO,                   // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_OFFENSIVO,  // Ricevente 4 / Passatore lungo
                    Ruolo.ATTACCANTE_ESTERNO,        // Ricevente lungo / Cross per bomber
                    Ruolo.BOMBER                     // Ricevente cross / Colpo di testa
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,        // Passatore 1 / Ricevente 3 / Passatore 4
                    Ruolo.CENTROCAMPISTA_DIFENSIVO,  // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO,                   // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 4 / Passatore lungo
                    Ruolo.ATTACCANTE_ESTERNO,        // Ricevente lungo / Cross per bomber
                    Ruolo.BOMBER                     // Ricevente cross / Colpo di testa
            )
    );

    //===============TIKITAKA 4================
    public static final Map<Modulo, List<Ruolo>> TIKITAKA4_MAP = Map.of(
            //4.1 ------442-----
            Modulo._4_4_2_1, List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Passatore 1
                    Ruolo.TERZINO,                   // Ricevente 1 / Passatore 2
                    Ruolo.ALA,                       // Ricevente 2 / Passatore 3
                    Ruolo.DIFENSORE_CENTRALE,        // Ricevente 3 / Passatore 4
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 4 / Passatore 5 / Passatore 6
                    Ruolo.SECONDA_PUNTA,             // Ricevente 6 / Dribbling / Tiro
                    Ruolo.CENTROCAMPISTA_CENTRALE    // Ricevente 5 (può essere lo stesso o un altro)
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.CENTROCAMPISTA_DIFENSIVO,  // Passatore 1
                    Ruolo.TERZINO,                   // Ricevente 1 / Passatore 2
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 2 / Passatore 3
                    Ruolo.DIFENSORE_CENTRALE,        // Ricevente 3 / Passatore 4
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 4 / Passatore 5 / Passatore 6
                    Ruolo.SECONDA_PUNTA,             // Ricevente 6 / Dribbling / Tiro
                    Ruolo.CENTROCAMPISTA_OFFENSIVO   // Ricevente 5
            ),
            //4.2------433------
            Modulo._4_3_3_1, List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Passatore 1
                    Ruolo.TERZINO,                   // Ricevente 1 / Passatore 2
                    Ruolo.CENTROCAMPISTA_OFFENSIVO,  // Ricevente 2 / Passatore 3
                    Ruolo.DIFENSORE_CENTRALE,        // Ricevente 3 / Passatore 4
                    Ruolo.CENTROCAMPISTA_OFFENSIVO,  // Ricevente 4 / Passatore 5 / Passatore 6
                    Ruolo.ATTACCANTE_ESTERNO,        // Ricevente 6 / Dribbling / Tiro
                    Ruolo.CENTROCAMPISTA_OFFENSIVO   // Ricevente 5
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.CENTROCAMPISTA_DIFENSIVO,  // Passatore 1
                    Ruolo.TERZINO,                   // Ricevente 1 / Passatore 2
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 2 / Passatore 3
                    Ruolo.DIFENSORE_CENTRALE,        // Ricevente 3 / Passatore 4
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 4 / Passatore 5 / Passatore 6
                    Ruolo.ATTACCANTE_ESTERNO,        // Ricevente 6 / Dribbling / Tiro
                    Ruolo.CENTROCAMPISTA_CENTRALE    // Ricevente 5
            )
    );

    //===============TIKITAKA 5================
    public static final Map<Modulo, List<Ruolo>> TIKITAKA5_MAP = Map.of(
            //5.1 ------442-----
            Modulo._4_4_2_1, List.of(
                    Ruolo.ALA,                       // Passatore 1 / Ricevente 3 / Passatore 4 / Ricevente 6 / Tiro
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Ricevente 1 / Passatore 2
                    Ruolo.SECONDA_PUNTA,             // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_CENTRALE    // Ricevente 4 / Dribbling / Passatore 6
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Passatore 1 / Ricevente 3 / Passatore 4 / Ricevente 6 / Tiro
                    Ruolo.CENTROCAMPISTA_DIFENSIVO,  // Ricevente 1 / Passatore 2
                    Ruolo.ATTACCANTE_ESTERNO,        // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_OFFENSIVO   // Ricevente 4 / Dribbling / Passatore 6
            ),
            //5.2 ------433------
            Modulo._4_3_3_1, List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,   // Passatore 1 / Ricevente 3 / Passatore 4 / Ricevente 6 / Tiro
                    Ruolo.CENTROCAMPISTA_OFFENSIVO,  // Ricevente 1 / Passatore 2
                    Ruolo.ATTACCANTE_ESTERNO,        // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_OFFENSIVO   // Ricevente 4 / Dribbling / Passatore 6
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Passatore 1 / Ricevente 3 / Passatore 4 / Ricevente 6 / Tiro
                    Ruolo.CENTROCAMPISTA_DIFENSIVO,  // Ricevente 1 / Passatore 2
                    Ruolo.ATTACCANTE_ESTERNO,        // Ricevente 2 / Passatore 3
                    Ruolo.CENTROCAMPISTA_CENTRALE    // Ricevente 4 / Dribbling / Passatore 6
            )
    );
}
