package pizzamafia.CMbackend.helpers.utility;

import pizzamafia.CMbackend.entities.Partita;
import pizzamafia.CMbackend.entities.Squadra;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MomentumBonusManager {
    private MomentumBonusManager() {}

    // ==============================
    // Configurazione globale
    // ==============================
    private static final int BONUS_PER_SUCCESS = 2000; // VALORE BONUS

    // key = partitaId:squadraId
    private static final Map<String, Integer> map = new ConcurrentHashMap<>();

    private static String key(Partita partita, Squadra squadra) {
        return partita.getId().toString() + ":" + squadra.getId().toString();
    }

    // ==============================
    // Lettura
    // ==============================
    // Leggi il bonus corrente senza azzerarlo
    public static int peek(Partita partita, Squadra squadra) {
        return map.getOrDefault(key(partita, squadra), 0);
    }

    // ==============================
    // Aggiornamento
    // ==============================
    // Aggiungi un valore arbitrario
    public static void add(Partita partita, Squadra squadra, int value) {
        String k = key(partita, squadra);
        map.merge(k, value, Integer::sum);
    }

    // Aggiungi il bonus standard (+2) quando un evento è riuscito
    public static void addSuccess(Partita partita, Squadra squadra) {
        add(partita, squadra, BONUS_PER_SUCCESS);
    }

    // ==============================
    // Reset
    // ==============================
    // Azzera il bonus per questa squadra in questa partita (fine/inizio azione)
    public static void clearForAction(Partita partita, Squadra squadra) {
        map.remove(key(partita, squadra));
    }

    // Pulizia globale per tutta la partita (utile a inizio/fine match)
    public static void clearAllForPartita(Partita partita) {
        String prefix = partita.getId().toString() + ":";
        map.keySet().removeIf(k -> k.startsWith(prefix));
    }
}
