package pizzamafia.CMbackend.helpers;

import pizzamafia.CMbackend.entities.Giocatore;
import pizzamafia.CMbackend.entities.Titolari;
import pizzamafia.CMbackend.enums.Modulo;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.utility.ModuloUtils;

import java.util.*;

public class SelezioneCpuHelper {

    // Metodo principale: seleziona i migliori 11 per un modulo dato

    public static List<Titolari> generaTitolariDalModulo(List<Giocatore> rosa, Modulo modulo) {
        // === BLOCCO 1: definizione del modulo (ruoli richiesti con quantità) ===
        Map<Ruolo, Integer> ruoliRichiesti = ModuloUtils.getRuoliPerModulo(modulo);

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
        int valoreEffettivo = ValutazioneGiocatoreHelper.calcolaValoreEffettivo(g, ruolo);
        int malus = g.getValoreTecnico() - valoreEffettivo;

        return Titolari.builder()
                .giocatore(g)
                .ruolo(ruolo)
                .malus(malus)
                .valoreEffettivo(valoreEffettivo)
                .build();
    }


    // === BLOCCO 3: ruoli alternativi per copertura intelligente ===
    private static Map<Ruolo, List<Ruolo>> getRuoliAlternativi() {
        Map<Ruolo, List<Ruolo>> map = new HashMap<>();

        // Ali: 1) lato opposto, 2) stesso lato att esterno, 3) stesso lato terzino
        map.put(Ruolo.ALA_DX, List.of(Ruolo.ALA_SX, Ruolo.TERZINO_DX, Ruolo.ATTACCANTE_ESTERNO_DX));
        map.put(Ruolo.ALA_SX, List.of(Ruolo.ALA_DX, Ruolo.TERZINO_SX, Ruolo.ATTACCANTE_ESTERNO_SX));

        // Attaccanti esterni: 1) lato opposto, 2) stesso lato ala, 3) seconda punta
        map.put(Ruolo.ATTACCANTE_ESTERNO_DX, List.of(Ruolo.ATTACCANTE_ESTERNO_SX, Ruolo.ALA_DX, Ruolo.SECONDA_PUNTA, Ruolo.CENTROCAMPISTA_OFFENSIVO));
        map.put(Ruolo.ATTACCANTE_ESTERNO_SX, List.of(Ruolo.ATTACCANTE_ESTERNO_DX, Ruolo.ALA_SX, Ruolo.SECONDA_PUNTA, Ruolo.CENTROCAMPISTA_OFFENSIVO));

        // Terzini: 1) lato opposto, 2) stesso lato ala, 3) difensore centrale
        map.put(Ruolo.TERZINO_DX, List.of(Ruolo.TERZINO_SX, Ruolo.ALA_DX, Ruolo.DIFENSORE_CENTRALE));
        map.put(Ruolo.TERZINO_SX, List.of(Ruolo.TERZINO_DX, Ruolo.ALA_SX, Ruolo.DIFENSORE_CENTRALE));

        // Centrocampo
        map.put(Ruolo.CENTROCAMPISTA_OFFENSIVO, List.of(Ruolo.CENTROCAMPISTA_CENTRALE, Ruolo.CENTROCAMPISTA_DIFENSIVO, Ruolo.SECONDA_PUNTA));
        map.put(Ruolo.CENTROCAMPISTA_DIFENSIVO, List.of(Ruolo.CENTROCAMPISTA_CENTRALE, Ruolo.CENTROCAMPISTA_OFFENSIVO, Ruolo.DIFENSORE_CENTRALE));
        map.put(Ruolo.CENTROCAMPISTA_CENTRALE, List.of(Ruolo.CENTROCAMPISTA_OFFENSIVO, Ruolo.CENTROCAMPISTA_DIFENSIVO));

        // Punta
        map.put(Ruolo.SECONDA_PUNTA, List.of(Ruolo.CENTROCAMPISTA_OFFENSIVO, Ruolo.BOMBER, Ruolo.ATTACCANTE_ESTERNO_DX, Ruolo.ATTACCANTE_ESTERNO_SX));
        map.put(Ruolo.BOMBER, List.of(Ruolo.SECONDA_PUNTA, Ruolo.ATTACCANTE_ESTERNO_DX, Ruolo.ATTACCANTE_ESTERNO_SX));

        // Difesa centrale
        map.put(Ruolo.DIFENSORE_CENTRALE, List.of(Ruolo.TERZINO_DX, Ruolo.TERZINO_SX, Ruolo.CENTROCAMPISTA_DIFENSIVO));

        // Portiere: nessun alternativo

        return map;
    }

}
