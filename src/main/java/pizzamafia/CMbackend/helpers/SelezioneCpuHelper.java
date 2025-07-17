package pizzamafia.CMbackend.helpers;

import pizzamafia.CMbackend.entities.Giocatore;
import pizzamafia.CMbackend.entities.Titolari;
import pizzamafia.CMbackend.enums.Ruolo;

import java.util.*;

public class SelezioneCpuHelper {

    // Metodo principale: seleziona i migliori 11 per un modulo dato (per ora solo _4_4_2)
    public static List<Titolari> generaTitolariDalModulo(List<Giocatore> rosa, String modulo) {
        if (!modulo.equals("_4_4_2")) {
            throw new UnsupportedOperationException("Solo il modulo _4_4_2 è supportato per ora.");
        }

        // === BLOCCO 1: definizione del modulo (ruoli richiesti con quantità) ===
        Map<Ruolo, Integer> ruoliRichiesti = Map.of(
                Ruolo.PORTIERE, 1,
                Ruolo.DIFENSORE_CENTRALE, 2,
                Ruolo.TERZINO, 2,
                Ruolo.CENTROCAMPISTA_DIFENSIVO, 1,
                Ruolo.CENTROCAMPISTA_OFFENSIVO, 1,
                Ruolo.ALA, 2,
                Ruolo.SECONDA_PUNTA, 1,
                Ruolo.BOMBER, 1
        );

        // Mappa ruoli alternativi per coprire eventuali mancanze
        Map<Ruolo, List<Ruolo>> ruoliAlternativi = getRuoliAlternativi();

        // Tiene traccia dei giocatori già scelti
        List<UUID> giaSelezionati = new ArrayList<>();
        List<Titolari> titolari = new ArrayList<>();

        // === BLOCCO 2: selezione per ogni ruolo richiesto ===
        for (Map.Entry<Ruolo, Integer> entry : ruoliRichiesti.entrySet()) {
            Ruolo ruoloRichiesto = entry.getKey();
            int quanti = entry.getValue();

            // 2a. Seleziona giocatori con ruolo esatto, ordinati per valore tecnico
            List<Giocatore> disponibili = rosa.stream()
                    .filter(g -> !giaSelezionati.contains(g.getId()))
                    .filter(g -> g.getRuolo() == ruoloRichiesto)
                    .sorted(Comparator.comparingInt(Giocatore::getValoreTecnico).reversed())
                    .toList();

            int presiDiretti = Math.min(disponibili.size(), quanti);
            for (int i = 0; i < presiDiretti; i++) {
                Giocatore g = disponibili.get(i);
                giaSelezionati.add(g.getId());
                titolari.add(buildTitolare(g, ruoloRichiesto));
            }

            // 2b. Se non bastano, cerca sostituti nei ruoli alternativi
            if (presiDiretti < quanti) {
                int rimanenti = quanti - presiDiretti;

                List<Ruolo> alternativi = ruoliAlternativi.getOrDefault(ruoloRichiesto, List.of());

                List<Giocatore> candidati = rosa.stream()
                        .filter(g -> !giaSelezionati.contains(g.getId()))
                        .filter(g -> alternativi.contains(g.getRuolo()))
                        .sorted(Comparator.comparingInt(Giocatore::getValoreTecnico).reversed())
                        .toList();

                for (int i = 0; i < Math.min(rimanenti, candidati.size()); i++) {
                    Giocatore g = candidati.get(i);
                    giaSelezionati.add(g.getId());
                    titolari.add(buildTitolare(g, ruoloRichiesto));
                }
            }
        }

        return titolari;
    }

    // Crea un oggetto Titolari con valore effettivo calcolato
    private static Titolari buildTitolare(Giocatore g, Ruolo ruolo) {
        return Titolari.builder()
                .giocatore(g)
                .ruolo(ruolo)
                .valoreEffettivo(ValutazioneGiocatoreHelper.calcolaValoreEffettivo(g, ruolo))
                .build();
    }

    // === BLOCCO 3: ruoli alternativi per copertura intelligente ===
    private static Map<Ruolo, List<Ruolo>> getRuoliAlternativi() {
        Map<Ruolo, List<Ruolo>> map = new HashMap<>();

        map.put(Ruolo.ALA, List.of(Ruolo.ATTACCANTE_ESTERNO, Ruolo.TERZINO));
        map.put(Ruolo.SECONDA_PUNTA, List.of(Ruolo.CENTROCAMPISTA_OFFENSIVO, Ruolo.BOMBER));
        map.put(Ruolo.CENTROCAMPISTA_OFFENSIVO, List.of(Ruolo.CENTROCAMPISTA_DIFENSIVO, Ruolo.SECONDA_PUNTA));
        map.put(Ruolo.CENTROCAMPISTA_DIFENSIVO, List.of(Ruolo.CENTROCAMPISTA_OFFENSIVO, Ruolo.DIFENSORE_CENTRALE));
        map.put(Ruolo.TERZINO, List.of(Ruolo.DIFENSORE_CENTRALE, Ruolo.ALA));
        map.put(Ruolo.DIFENSORE_CENTRALE, List.of(Ruolo.TERZINO, Ruolo.CENTROCAMPISTA_DIFENSIVO));
        map.put(Ruolo.BOMBER, List.of(Ruolo.SECONDA_PUNTA, Ruolo.ATTACCANTE_ESTERNO));
        // PORTIERE non ha alternativi

        return map;
    }
}
