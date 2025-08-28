package pizzamafia.CMbackend.helpers.azioni.giocofasce;

import pizzamafia.CMbackend.enums.Modulo;
import pizzamafia.CMbackend.enums.Ruolo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ModuliGiocoSulleFasceMap {
    private static final Random RND = new Random();

    /** Restituisce una variante valida (1..5) disponibile per il modulo passato. */
    public static int scegliVariante(Modulo modulo) {
        List<Integer> disponibili = new ArrayList<>();
        if (GIOCOFASCE1_MAP.containsKey(modulo)) disponibili.add(1);
        if (GIOCOFASCE2_MAP.containsKey(modulo)) disponibili.add(2);
        if (GIOCOFASCE3_MAP.containsKey(modulo)) disponibili.add(3);
        if (GIOCOFASCE4_MAP.containsKey(modulo)) disponibili.add(4);
        if (GIOCOFASCE5_MAP.containsKey(modulo)) disponibili.add(5);
        if (disponibili.isEmpty()) return 1; // fallback
        return disponibili.get(RND.nextInt(disponibili.size()));
    }

    /** Lista ruoli per la variante indicata e il modulo dato. */
    public static List<Ruolo> ruoliPer(Modulo modulo, int variante) {
        return switch (variante) {
            case 1 -> GIOCOFASCE1_MAP.getOrDefault(modulo, List.of());
            case 2 -> GIOCOFASCE2_MAP.getOrDefault(modulo, List.of());
            case 3 -> GIOCOFASCE3_MAP.getOrDefault(modulo, List.of());
            case 4 -> GIOCOFASCE4_MAP.getOrDefault(modulo, List.of());
            case 5 -> GIOCOFASCE5_MAP.getOrDefault(modulo, List.of());
            default -> List.of();
        };
    }

    // 1.----------------- GIOCO SULLE FASCE 1 --------------------
    public static final Map<Modulo, List<Ruolo>> GIOCOFASCE1_MAP = Map.of(
            Modulo._4_4_2_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore 2
                    Ruolo.ALA_DX,                      // Ricevente 2 / Dribbling / Cross
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore 2
                    Ruolo.SECONDA_PUNTA,            // Ricevente 2 / Dribbling / Cross
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            ),
            Modulo._4_3_3_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore 2
                    Ruolo.ATTACCANTE_ESTERNO_DX,       // Ricevente 2 / Dribbling / Cross
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore 2
                    Ruolo.ATTACCANTE_ESTERNO_DX,       // Ricevente 2 / Dribbling / Cross
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            )
    );

    //2.---------------------GIOCO SULLE FASCE 2---------------------------
    public static final Map<Modulo, List<Ruolo>> GIOCOFASCE2_MAP = Map.of(
            Modulo._4_4_2_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO_DX,                  // Ricevente 1 / Passatore 2 / Ricevente 3 / Dribbling / Cross
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 2 / Passatore 3
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO_DX,                  // Ricevente 1 / Passatore 2 / Ricevente 3 / Dribbling / Cross
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 2 / Passatore 3
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            ),
            Modulo._4_3_3_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO_DX,                  // Ricevente 1 / Passatore 2 / Ricevente 3 / Dribbling / Cross
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 2 / Passatore 3
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO_DX,                  // Ricevente 1 / Passatore 2 / Ricevente 3 / Dribbling / Cross
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 2 / Passatore 3
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            )
    );

    // ----------------- GIOCO SULLE FASCE 3 --------------------
    public static final Map<Modulo, List<Ruolo>> GIOCOFASCE3_MAP = Map.of(
            Modulo._4_4_2_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO_SX,                  // Ricevente 1 / Dribbling / Passatore 2
                    Ruolo.ALA_SX,                      // Ricevente 2 / Dribbling / Cross
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO_SX,                  // Ricevente 1 / Dribbling / Passatore 2
                    Ruolo.CENTROCAMPISTA_OFFENSIVO, // Ricevente 2 / Dribbling / Cross
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            ),
            Modulo._4_3_3_1, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO_SX,                  // Ricevente 1 / Dribbling / Passatore 2
                    Ruolo.ATTACCANTE_ESTERNO_SX,       // Ricevente 2 / Dribbling / Cross
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.DIFENSORE_CENTRALE,       // Passatore 1
                    Ruolo.TERZINO_SX,                  // Ricevente 1 / Dribbling / Passatore 2
                    Ruolo.ATTACCANTE_ESTERNO_SX,       // Ricevente 2 / Dribbling / Cross
                    Ruolo.BOMBER                    // Ricevente cross / Colpo di testa
            )
    );



    // ----------------- GIOCO SULLE FASCE 4 --------------------
    public static final Map<Modulo, List<Ruolo>> GIOCOFASCE4_MAP = Map.of(
            Modulo._4_4_2_1, List.of(
                    Ruolo.TERZINO_SX,                  // Passatore 1 / Dribbling / Cross / Tiro
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore 2
                    Ruolo.ALA_SX,                      // Ricevente 2 / Dribbling / Passatore 3 / Ricevente 5 / Tiro
                    Ruolo.SECONDA_PUNTA             // Ricevente 3 / Passatore 4
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.TERZINO_SX,                  // Passatore 1 / Dribbling / Cross / Tiro
                    Ruolo.CENTROCAMPISTA_DIFENSIVO, // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO_SX,                  // Ricevente 2 / Dribbling / Passatore 3 / Ricevente 5 / Tiro
                    Ruolo.SECONDA_PUNTA             // Ricevente 3 / Passatore 4
            ),
            Modulo._4_3_3_1, List.of(
                    Ruolo.TERZINO_SX,                  // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore 2
                    Ruolo.ATTACCANTE_ESTERNO_SX,       // Ricevente 2 / Dribbling / Passatore 3 / Ricevente 5 / Tiro
                    Ruolo.CENTROCAMPISTA_OFFENSIVO  // Ricevente 3 / Passatore 4
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.TERZINO_SX,                  // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore 2
                    Ruolo.ATTACCANTE_ESTERNO_SX,       // Ricevente 2 / Dribbling / Passatore 3 / Ricevente 5 / Tiro
                    Ruolo.BOMBER                    // Ricevente 3 / Passatore 4
            )
    );
    // ----------------- GIOCO SULLE FASCE 5 --------------------
    public static final Map<Modulo, List<Ruolo>> GIOCOFASCE5_MAP = Map.of(
            Modulo._4_4_2_1, List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore 2
                    Ruolo.ALA_DX,                      // Ricevente 2 / Dribbling x2 / Passatore 5
                    Ruolo.SECONDA_PUNTA             // Ricevente 5 / Tiro
            ),
            Modulo._4_4_2_2, List.of(
                    Ruolo.CENTROCAMPISTA_DIFENSIVO, // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore 2
                    Ruolo.TERZINO_DX,                  // Ricevente 2 / Dribbling x2 / Passatore 5
                    Ruolo.SECONDA_PUNTA             // Ricevente 5 / Tiro
            ),
            Modulo._4_3_3_1, List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore 2
                    Ruolo.ATTACCANTE_ESTERNO_DX,       // Ricevente 2 / Dribbling x2 / Passatore 5
                    Ruolo.BOMBER                    // Ricevente 5 / Tiro
            ),
            Modulo._4_3_3_2, List.of(
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Passatore 1
                    Ruolo.CENTROCAMPISTA_CENTRALE,  // Ricevente 1 / Passatore 2
                    Ruolo.ATTACCANTE_ESTERNO_DX,       // Ricevente 2 / Dribbling x2 / Passatore 5
                    Ruolo.BOMBER                    // Ricevente 5 / Tiro
            )
    );








}
