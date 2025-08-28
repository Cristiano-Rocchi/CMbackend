package pizzamafia.CMbackend.helpers.utility;

import pizzamafia.CMbackend.entities.Giocatore;
import pizzamafia.CMbackend.entities.Titolari;
import pizzamafia.CMbackend.enums.Ruolo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DefensiveMatchup
 * ----------------
 * Selettore unico per scegliere il difendente più plausibile in duelli 1v1:
 * - DRIBBLING  → chi esce sul portatore
 * - PASSAGGI   → chi esce sul portatore o chi intercetta sul destinatario
 * - CROSS/TESTA/TIRO → chi marca/intercetta il destinatario (o pressa il tiratore)
 *
 * Logica:
 * 1) Pesi "base" per ruolo attaccante → ruoli difensivi candidati (zona di campo).
 * 2) Bias dinamici in base ai ruoli REALMENTE presenti nei titolari difesa (es. mediano, ali, esterni 4-3-3).
 * 3) Fallback robusti (difensori naturali → centrocampisti → chiunque).
 */
public class DefensiveMatchup {

    // ============================================================
    // 1) TABELLA PESI BASE (neutra dal modulo)
    //    Chi marca CHI in linea di principio (fasce/centro).
    // ============================================================
    private static final Map<Ruolo, LinkedHashMap<Ruolo, Integer>> MATCHUP_PESI = buildPesiBase();

    private static Map<Ruolo, LinkedHashMap<Ruolo, Integer>> buildPesiBase() {
        Map<Ruolo, LinkedHashMap<Ruolo, Integer>> m = new EnumMap<>(Ruolo.class);

        // ------------------------ FASCE: ATTACCO A DESTRA → DIFESA A SINISTRA ------------------------
        m.put(Ruolo.TERZINO_DX, lh(
                e(Ruolo.TERZINO_SX, 60), e(Ruolo.ALA_SX, 40),
                e(Ruolo.CENTROCAMPISTA_CENTRALE, 20), e(Ruolo.DIFENSORE_CENTRALE, 15)
        ));
        m.put(Ruolo.ALA_DX, lh(
                e(Ruolo.TERZINO_SX, 60), e(Ruolo.ALA_SX, 40),
                e(Ruolo.CENTROCAMPISTA_CENTRALE, 20), e(Ruolo.DIFENSORE_CENTRALE, 15)
        ));
        m.put(Ruolo.ATTACCANTE_ESTERNO_DX, lh(
                e(Ruolo.TERZINO_SX, 55), e(Ruolo.DIFENSORE_CENTRALE, 35),
                e(Ruolo.CENTROCAMPISTA_CENTRALE, 15)
        ));

        // ------------------------ FASCE: ATTACCO A SINISTRA → DIFESA A DESTRA ------------------------
        m.put(Ruolo.TERZINO_SX, lh(
                e(Ruolo.TERZINO_DX, 60), e(Ruolo.ALA_DX, 40),
                e(Ruolo.CENTROCAMPISTA_CENTRALE, 20), e(Ruolo.DIFENSORE_CENTRALE, 15)
        ));
        m.put(Ruolo.ALA_SX, lh(
                e(Ruolo.TERZINO_DX, 60), e(Ruolo.ALA_DX, 40),
                e(Ruolo.CENTROCAMPISTA_CENTRALE, 20), e(Ruolo.DIFENSORE_CENTRALE, 15)
        ));
        m.put(Ruolo.ATTACCANTE_ESTERNO_SX, lh(
                e(Ruolo.TERZINO_DX, 55), e(Ruolo.DIFENSORE_CENTRALE, 35),
                e(Ruolo.CENTROCAMPISTA_CENTRALE, 15)
        ));

        // ------------------------ CORRIDOIO CENTRALE (ruoli offensivi tipici) ------------------------
        m.put(Ruolo.SECONDA_PUNTA, lh(
                e(Ruolo.DIFENSORE_CENTRALE, 60), e(Ruolo.CENTROCAMPISTA_DIFENSIVO, 40),
                e(Ruolo.CENTROCAMPISTA_CENTRALE, 25)
        ));
        m.put(Ruolo.BOMBER, lh(
                e(Ruolo.DIFENSORE_CENTRALE, 70), e(Ruolo.CENTROCAMPISTA_DIFENSIVO, 30)
        ));
        m.put(Ruolo.CENTROCAMPISTA_OFFENSIVO, lh(
                e(Ruolo.CENTROCAMPISTA_DIFENSIVO, 60), e(Ruolo.CENTROCAMPISTA_CENTRALE, 50),
                e(Ruolo.DIFENSORE_CENTRALE, 25)
        ));
        m.put(Ruolo.CENTROCAMPISTA_CENTRALE, lh(
                e(Ruolo.CENTROCAMPISTA_CENTRALE, 60), e(Ruolo.CENTROCAMPISTA_DIFENSIVO, 50),
                e(Ruolo.TERZINO_DX, 15), e(Ruolo.TERZINO_SX, 15)
        ));

        // ------------------------ NUOVI: PORTATORI "BASSI" IN COSTRUZIONE ------------------------
        m.put(Ruolo.DIFENSORE_CENTRALE, lh(
                e(Ruolo.BOMBER, 45), e(Ruolo.SECONDA_PUNTA, 35),
                e(Ruolo.ATTACCANTE_ESTERNO_DX, 20), e(Ruolo.ATTACCANTE_ESTERNO_SX, 20),
                e(Ruolo.CENTROCAMPISTA_CENTRALE, 15), e(Ruolo.CENTROCAMPISTA_DIFENSIVO, 10)
        ));
        m.put(Ruolo.CENTROCAMPISTA_DIFENSIVO, lh(
                e(Ruolo.CENTROCAMPISTA_OFFENSIVO, 50), e(Ruolo.SECONDA_PUNTA, 35),
                e(Ruolo.BOMBER, 20), e(Ruolo.CENTROCAMPISTA_CENTRALE, 25)
        ));
        m.put(Ruolo.PORTIERE, lh(
                e(Ruolo.BOMBER, 50), e(Ruolo.SECONDA_PUNTA, 35),
                e(Ruolo.ATTACCANTE_ESTERNO_DX, 20), e(Ruolo.ATTACCANTE_ESTERNO_SX, 20)
        ));

        return m;
    }

    // ============================================================
    // 2) API PUBBLICA
    // ============================================================

    public static Giocatore scegliDifensorePer(Ruolo ruoloAttaccante, List<Titolari> titolariDifesa, Random rng) {
        LinkedHashMap<Ruolo, Integer> base = new LinkedHashMap<>(
                MATCHUP_PESI.getOrDefault(
                        ruoloAttaccante,
                        lh(e(Ruolo.DIFENSORE_CENTRALE, 60), e(Ruolo.TERZINO_DX, 40), e(Ruolo.TERZINO_SX, 40))
                )
        );

        // Bias dinamici in base a ciò che è realmente in campo
        Set<Ruolo> ruoliPresenti = titolariDifesa.stream().map(Titolari::getRuolo).collect(Collectors.toSet());

        if (ruoliPresenti.contains(Ruolo.CENTROCAMPISTA_DIFENSIVO)) {
            switch (ruoloAttaccante) {
                case CENTROCAMPISTA_OFFENSIVO:
                case SECONDA_PUNTA:
                case CENTROCAMPISTA_CENTRALE:
                    base.merge(Ruolo.CENTROCAMPISTA_DIFENSIVO, 25, Integer::sum);
                    break;
                default:
                    break;
            }
        }

        if (ruoliPresenti.contains(Ruolo.ALA_SX) && isRightSideAttacker(ruoloAttaccante)) {
            base.merge(Ruolo.ALA_SX, 20, Integer::sum);
        }
        if (ruoliPresenti.contains(Ruolo.ALA_DX) && isLeftSideAttacker(ruoloAttaccante)) {
            base.merge(Ruolo.ALA_DX, 20, Integer::sum);
        }

        if (ruoliPresenti.contains(Ruolo.ATTACCANTE_ESTERNO_SX) && isRightSideAttacker(ruoloAttaccante)) {
            base.merge(Ruolo.ATTACCANTE_ESTERNO_SX, 20, Integer::sum);
        }
        if (ruoliPresenti.contains(Ruolo.ATTACCANTE_ESTERNO_DX) && isLeftSideAttacker(ruoloAttaccante)) {
            base.merge(Ruolo.ATTACCANTE_ESTERNO_DX, 20, Integer::sum);
        }

        // Candidati presenti
        List<Titolari> candidati = titolariDifesa.stream()
                .filter(t -> base.containsKey(t.getRuolo()))
                .collect(Collectors.toList());
        if (!candidati.isEmpty()) {
            return weightedPick(candidati, t -> base.getOrDefault(t.getRuolo(), 1), rng).getGiocatore();
        }

        // Fallback 1: difensori naturali
        List<Titolari> difNaturali = titolariDifesa.stream()
                .filter(t -> {
                    switch (t.getRuolo()) {
                        case TERZINO_DX:
                        case TERZINO_SX:
                        case DIFENSORE_CENTRALE:
                            return true;
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
        if (!difNaturali.isEmpty()) return randomPick(difNaturali, rng).getGiocatore();

        // Fallback 2: centrocampisti
        List<Titolari> cc = titolariDifesa.stream()
                .filter(t -> {
                    switch (t.getRuolo()) {
                        case CENTROCAMPISTA_DIFENSIVO:
                        case CENTROCAMPISTA_CENTRALE:
                        case CENTROCAMPISTA_OFFENSIVO:
                            return true;
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
        if (!cc.isEmpty()) return randomPick(cc, rng).getGiocatore();

        // Fallback 3: chiunque
        return randomPick(titolariDifesa, rng).getGiocatore();
    }

    /** Esce sul portatore (dribbling o pressione sul passatore). */
    public static Giocatore scegliPressatoreSuPortatore(Ruolo ruoloPortatore, List<Titolari> titolariDifesa, Random rng) {
        return scegliDifensorePer(ruoloPortatore, titolariDifesa, rng);
    }
    /** Marca/intercetta sul destinatario (passaggi/cross/testa). */
    public static Giocatore scegliIntercettoreSuDestinatario(Ruolo ruoloDestinatario, List<Titolari> titolariDifesa, Random rng) {
        return scegliDifensorePer(ruoloDestinatario, titolariDifesa, rng);
    }

    // ============================================================
    // 3) SUPPORTO: lato del campo + utilities
    // ============================================================

    private static boolean isRightSideAttacker(Ruolo r) {
        return r == Ruolo.TERZINO_DX || r == Ruolo.ALA_DX || r == Ruolo.ATTACCANTE_ESTERNO_DX;
    }
    private static boolean isLeftSideAttacker(Ruolo r) {
        return r == Ruolo.TERZINO_SX || r == Ruolo.ALA_SX || r == Ruolo.ATTACCANTE_ESTERNO_SX;
    }

    private static Titolari randomPick(List<Titolari> lista, Random rng) {
        return lista.get(rng.nextInt(lista.size()));
    }
    private static Titolari weightedPick(List<Titolari> lista, java.util.function.Function<Titolari, Integer> w, Random rng) {
        int sum = 0;
        for (Titolari t : lista) sum += Math.max(1, w.apply(t));
        int r = rng.nextInt(sum) + 1;
        int cum = 0;
        for (Titolari t : lista) {
            cum += Math.max(1, w.apply(t));
            if (r <= cum) return t;
        }
        return lista.get(lista.size() - 1);
    }

    // -------- helper per creare la LinkedHashMap coi pesi --------
    private static LinkedHashMap<Ruolo, Integer> lh(Map.Entry<Ruolo, Integer>... entries) {
        LinkedHashMap<Ruolo, Integer> map = new LinkedHashMap<>();
        for (Map.Entry<Ruolo, Integer> en : entries) map.put(en.getKey(), en.getValue());
        return map;
    }
    // (Compatibile con Java 8+) invece di Map.entry(...)
    private static Map.Entry<Ruolo, Integer> e(Ruolo r, int peso) {
        return new AbstractMap.SimpleEntry<>(r, peso);
    }
}
