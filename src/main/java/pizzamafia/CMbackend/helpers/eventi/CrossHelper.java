package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.enums.TipoEvento;
import pizzamafia.CMbackend.helpers.utility.DefensiveMatchup;
import pizzamafia.CMbackend.helpers.utility.MomentumBonusManager;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CrossHelper {

    private static final Random random = new Random();

    // === Versione compatibile con codice esistente ===
    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        return genera(minuto, secondo, partita, squadraAttaccante, titolariAttacco, titolariDifesa, null, null);
    }

    // === Versione con ruoli specifici ===
    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            Ruolo ruoloCrossatore,
            Ruolo ruoloDestinatario
    ) {
        // ===== 1) Selezione attori =====
        List<Giocatore> possibiliCrossatori = (ruoloCrossatore == null)
                ? titolariAttacco.stream().map(Titolari::getGiocatore).collect(Collectors.toList())
                : titolariAttacco.stream()
                .filter(t -> t.getRuolo() == ruoloCrossatore)
                .map(Titolari::getGiocatore)
                .collect(Collectors.toList());

        List<Giocatore> possibiliDestinatari = (ruoloDestinatario == null)
                ? titolariAttacco.stream().map(Titolari::getGiocatore).collect(Collectors.toList())
                : titolariAttacco.stream()
                .filter(t -> t.getRuolo() == ruoloDestinatario)
                .map(Titolari::getGiocatore)
                .collect(Collectors.toList());

        if (possibiliCrossatori.isEmpty() || possibiliDestinatari.isEmpty()) {
            return EventoPartita.builder()
                    .minuto(minuto).secondo(secondo).durataStimata(3)
                    .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                    .giocatorePrincipale(null).giocatoreSecondario(null)
                    .esito("NESSUN GIOCATORE DISPONIBILE")
                    .note("Cross non eseguito: ruoli mancanti")
                    .partita(partita).squadra(squadraAttaccante)
                    .build();
        }

        Giocatore crossatore = possibiliCrossatori.get(random.nextInt(possibiliCrossatori.size()));
        Giocatore destinatario = possibiliDestinatari.get(random.nextInt(possibiliDestinatari.size()));

        Ruolo ruoloEffCrossatore = (ruoloCrossatore != null) ? ruoloCrossatore : deduciRuoloDaTitolari(crossatore, titolariAttacco);
        Ruolo ruoloEffDestinatario = (ruoloDestinatario != null) ? ruoloDestinatario : deduciRuoloDaTitolari(destinatario, titolariAttacco);

        // ===== 2) Scelta difendente plausibile =====
        // modelliamo il "prima dell'arrivo in area":
        // - 70%: pressione sul crossatore (blocco del cross)
        // - 30%: intercetto sulla traiettoria (prima che raggiunga il destinatario)
        boolean pressioneSulCrossatore = random.nextDouble() < 0.70;

        Giocatore difensore = pressioneSulCrossatore
                ? DefensiveMatchup.scegliPressatoreSuPortatore(ruoloEffCrossatore, titolariDifesa, random)
                : DefensiveMatchup.scegliIntercettoreSuDestinatario(ruoloEffDestinatario, titolariDifesa, random);

        // ===== 3) Statistiche (INVARIATE) =====
        StatisticheTecnicheGiocatore sa = crossatore.getStatistiche();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        double punteggioCross = sd.getCross() * 0.6 +
                sd.getAssist() * 0.2 +
                sd.getLetturaDelGioco() * 0.15 +
                sd.getConcentrazione() * 0.05 +
                random.nextInt(11);
        // Momentum cumulativo per l'azione
        punteggioCross += MomentumBonusManager.peek(partita, squadraAttaccante);


        double punteggioDifensore = sd.getIntercettazione() * 0.4 +
                sd.getLetturaDelGioco() * 0.2 +
                sd.getContrasti() * 0.2 +
                sd.getAggressivita() * 0.2 +
                random.nextInt(11);

        // ===== 4) Esito =====
        if (punteggioCross > punteggioDifensore) {
            // Evento riuscito -> accumula momentum per l'azione
            MomentumBonusManager.addSuccess(partita, squadraAttaccante);

            // Cross parte ed arriva al destinatario → dopo questo evento chiamo ColpoDiTestaHelper
            return EventoPartita.builder()
                    .minuto(minuto).secondo(secondo).durataStimata(3)
                    .tipoEvento(TipoEvento.PASSAGGIO)
                    .giocatorePrincipale(crossatore)
                    .giocatoreSecondario(destinatario)
                    .esito("RIUSCITO")
                    .note("Cross riuscito")
                    .partita(partita).squadra(squadraAttaccante)
                    .build();

        } else if (punteggioDifensore - punteggioCross >= 10) {
            // Cross bloccato (in pressione) o intercettato prima di arrivare
            return EventoPartita.builder()
                    .minuto(minuto).secondo(secondo).durataStimata(3)
                    .tipoEvento(TipoEvento.INTERCETTO)
                    .giocatorePrincipale(difensore)
                    .giocatoreSecondario(crossatore)
                    .esito("PALLA RECUPERATA")
                    .note(pressioneSulCrossatore ? "Cross bloccato in pressione" : "Traiettoria del cross intercettata")
                    .partita(partita).squadra(difensore.getSquadra())
                    .build();

        } else {
            // Cross fuori misura
            return EventoPartita.builder()
                    .minuto(minuto).secondo(secondo).durataStimata(3)
                    .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                    .giocatorePrincipale(crossatore)
                    .giocatoreSecondario(null)
                    .esito("FUORI MISURA")
                    .note("Cross sbagliato")
                    .partita(partita).squadra(squadraAttaccante)
                    .build();
        }
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
