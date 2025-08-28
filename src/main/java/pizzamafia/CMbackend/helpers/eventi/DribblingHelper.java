package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.enums.TipoEvento;
import pizzamafia.CMbackend.helpers.utility.DefensiveMatchup;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DribblingHelper {

    private static final Random random = new Random();

    // === Versione standard (compatibile con esistente) ===
    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        return genera(minuto, secondo, partita, squadraAttaccante, titolariAttacco, titolariDifesa, null);
    }

    // === Versione con filtro per ruolo dell’attaccante ===
    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            Ruolo ruoloAttaccante
    ) {
        // =================== 1) Attaccante coinvolto ===================
        List<Giocatore> candidatiAttaccanti = (ruoloAttaccante == null)
                ? titolariAttacco.stream().map(Titolari::getGiocatore).collect(Collectors.toList())
                : titolariAttacco.stream()
                .filter(t -> t.getRuolo() == ruoloAttaccante)
                .map(Titolari::getGiocatore)
                .collect(Collectors.toList());

        if (candidatiAttaccanti.isEmpty()) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .durataStimata(3)
                    .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                    .giocatorePrincipale(null)
                    .giocatoreSecondario(null)
                    .esito("NESSUN ATTACCANTE DISPONIBILE")
                    .note("Dribbling fallito: nessun giocatore con ruolo " + ruoloAttaccante)
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();
        }

        Giocatore attaccante = candidatiAttaccanti.get(random.nextInt(candidatiAttaccanti.size()));

        // Ruolo effettivo: se non specificato, deduci dai titolari
        Ruolo ruoloEffAttaccante = (ruoloAttaccante != null)
                ? ruoloAttaccante
                : deduciRuoloDaTitolari(attaccante, titolariAttacco);

        // =================== 2) Difensore plausibile (pressione sul portatore) ===================
        Giocatore difensore = DefensiveMatchup.scegliPressatoreSuPortatore(
                ruoloEffAttaccante, titolariDifesa, random
        );

        // =================== 3) Statistiche (INVARIATE) ===================
        StatisticheTecnicheGiocatore sa = attaccante.getStatistiche();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        double punteggioAttaccante = sa.getDribbling() * 0.5 +
                sa.getAgilita() * 0.2 +
                sa.getScatto() * 0.15 +
                sa.getIntuito() * 0.15 +
                random.nextInt(11); // bonus casuale

        double punteggioDifensore = sd.getMarcatura() * 0.4 +
                sd.getContrasti() * 0.3 +
                sd.getPosizione() * 0.2 +
                sd.getAggressivita() * 0.1 +
                random.nextInt(11); // bonus casuale

        // =================== 4) Esiti (INVARIATI) ===================
        boolean superato = punteggioAttaccante > punteggioDifensore;

        if (superato) {
            int aggressivita = sd.getAggressivita();
            double bonus = (aggressivita - 50) / 200.0;
            double probabilitaFallo = 0.3 + Math.max(0, bonus);
            boolean fallo = random.nextDouble() < probabilitaFallo;

            if (fallo) {
                return EventoPartita.builder()
                        .minuto(minuto)
                        .secondo(secondo)
                        .durataStimata(3)
                        .tipoEvento(TipoEvento.FALLO)
                        .giocatorePrincipale(difensore)
                        .giocatoreSecondario(attaccante)
                        .esito("FALLO COMMESSO")
                        .note("Fallo dopo dribbling superato")
                        .partita(partita)
                        .squadra(difensore.getSquadra())
                        .build();
            } else {
                return EventoPartita.builder()
                        .minuto(minuto)
                        .secondo(secondo)
                        .durataStimata(3)
                        .tipoEvento(TipoEvento.DRIBBLING)
                        .giocatorePrincipale(attaccante)
                        .giocatoreSecondario(difensore)
                        .esito("RIUSCITO")
                        .note("Dribbling riuscito senza fallo")
                        .partita(partita)
                        .squadra(squadraAttaccante)
                        .build();
            }
        }

        // Non superato → palla recuperata
        return EventoPartita.builder()
                .minuto(minuto)
                .secondo(secondo)
                .durataStimata(3)
                .tipoEvento(TipoEvento.INTERCETTO)
                .giocatorePrincipale(difensore)
                .giocatoreSecondario(attaccante)
                .esito("PALLA RECUPERATA")
                .note("Dribbling fallito, intercetto del difensore")
                .partita(partita)
                .squadra(difensore.getSquadra())
                .build();
    }

    // =========================
    // Supporto: deduzione ruolo
    // =========================
    private static Ruolo deduciRuoloDaTitolari(Giocatore g, List<Titolari> titolari) {
        return titolari.stream()
                .filter(t -> t.getGiocatore().getId().equals(g.getId()))
                .map(Titolari::getRuolo)
                .findFirst()
                .orElse(g.getRuolo());
    }
}
