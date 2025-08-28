package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.enums.TipoEvento;
import pizzamafia.CMbackend.helpers.utility.DefensiveMatchup;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * PassaggioLungoHelper
 * --------------------
 * Genera un evento di passaggio LUNGO.
 *
 * Novità:
 * - Difendente scelto in modo plausibile con DefensiveMatchup:
 *   * ~15%: esce in PRESSIONE sul PORTATORE (passatore)
 *   * ~85%: marca/intercetta in zona DESTINATARIO  ← più realistico sui lanci
 *
 * Nota: i pesi di valutazione (punteggio passatore/difensore) restano IDENTICI
 * alla tua versione originale. Cambia solo la scelta del difendente.
 */
public class PassaggioLungoHelper {

    private static final Random random = new Random();

    // === Versione standard (retrocompatibile) ===
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

    // === Versione con filtro per ruoli ===
    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            Ruolo ruoloPassatore,
            Ruolo ruoloDestinatario
    ) {
        // ---------- 1) Selezione attori ----------
        List<Giocatore> possibiliPassatori = (ruoloPassatore == null)
                ? titolariAttacco.stream().map(Titolari::getGiocatore).collect(Collectors.toList())
                : titolariAttacco.stream()
                .filter(t -> t.getRuolo() == ruoloPassatore)
                .map(Titolari::getGiocatore)
                .collect(Collectors.toList());

        List<Giocatore> possibiliDestinatari = (ruoloDestinatario == null)
                ? titolariAttacco.stream().map(Titolari::getGiocatore).collect(Collectors.toList())
                : titolariAttacco.stream()
                .filter(t -> t.getRuolo() == ruoloDestinatario)
                .map(Titolari::getGiocatore)
                .collect(Collectors.toList());

        if (possibiliPassatori.isEmpty() || possibiliDestinatari.isEmpty()) {
            return EventoPartita.builder()
                    .minuto(minuto).secondo(secondo).durataStimata(6)
                    .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                    .giocatorePrincipale(null).giocatoreSecondario(null)
                    .esito("NESSUN GIOCATORE DISPONIBILE")
                    .note("Passaggio lungo non eseguito: ruoli non trovati")
                    .partita(partita).squadra(squadraAttaccante)
                    .build();
        }

        Giocatore passatore = possibiliPassatori.get(random.nextInt(possibiliPassatori.size()));
        Giocatore destinatario = possibiliDestinatari.get(random.nextInt(possibiliDestinatari.size()));

        // Ruoli effettivi dedotti se non passati
        Ruolo ruoloEffPassatore = (ruoloPassatore != null) ? ruoloPassatore : deduciRuoloDaTitolari(passatore, titolariAttacco);
        Ruolo ruoloEffDestinatario = (ruoloDestinatario != null) ? ruoloDestinatario : deduciRuoloDaTitolari(destinatario, titolariAttacco);

        // ---------- 2) Scelta difendente plausibile ----------
        // Lanci lunghi: alta probabilità di contesa/intercetto sul destinatario
        boolean pressioneSulPortatore = random.nextDouble() < 0.15; // 15% pressione, 85% intercetto

        Giocatore difensore = pressioneSulPortatore
                ? DefensiveMatchup.scegliPressatoreSuPortatore(ruoloEffPassatore, titolariDifesa, random)
                : DefensiveMatchup.scegliIntercettoreSuDestinatario(ruoloEffDestinatario, titolariDifesa, random);

        // ---------- 3) Statistiche----------
        StatisticheTecnicheGiocatore sp = passatore.getStatistiche();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        double punteggioPassatore = sp.getTecnica() * 0.4 +
                sp.getCreativita() * 0.3 +
                sp.getGiocoDiSquadra() * 0.15 +
                sp.getCarisma() * 0.15 +
                random.nextInt(11);

        double punteggioDifensore = sd.getPosizione() * 0.4 +
                sd.getIntuito() * 0.3 +
                sd.getAggressivita() * 0.2 +
                sd.getContrasti() * 0.1 +
                random.nextInt(11);

        // ---------- 4) Esiti ----------
        if (punteggioPassatore > punteggioDifensore) {
            return EventoPartita.builder()
                    .minuto(minuto).secondo(secondo).durataStimata(6)
                    .tipoEvento(TipoEvento.PASSAGGIO)
                    .giocatorePrincipale(passatore)
                    .giocatoreSecondario(destinatario)
                    .esito("RIUSCITO")
                    .note("Passaggio lungo riuscito")
                    .partita(partita).squadra(squadraAttaccante)
                    .build();
        }

        if (punteggioDifensore - punteggioPassatore >= 10) {
            return EventoPartita.builder()
                    .minuto(minuto).secondo(secondo).durataStimata(6)
                    .tipoEvento(TipoEvento.INTERCETTO)
                    .giocatorePrincipale(difensore)
                    .giocatoreSecondario(passatore)
                    .esito("PALLA RECUPERATA")
                    .note(pressioneSulPortatore ? "Recupero in pressione sul portatore" : "Intercetto/anticipo su passaggio lungo")
                    .partita(partita).squadra(difensore.getSquadra())
                    .build();
        }

        return EventoPartita.builder()
                .minuto(minuto).secondo(secondo).durataStimata(6)
                .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                .giocatorePrincipale(passatore)
                .giocatoreSecondario(destinatario)
                .esito("FUORI MISURA")
                .note("Passaggio lungo sbagliato")
                .partita(partita).squadra(squadraAttaccante)
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
