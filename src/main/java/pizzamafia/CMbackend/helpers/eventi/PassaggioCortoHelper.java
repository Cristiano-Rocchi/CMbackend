package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.enums.TipoEvento;
import pizzamafia.CMbackend.helpers.utility.DefensiveMatchup;
import pizzamafia.CMbackend.helpers.utility.MomentumBonusManager;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * PassaggioCortoHelper
 * --------------------
 * Genera un evento di passaggio corto tra due giocatori della squadra in attacco.
 *
 * Novità:
 * - Difendente scelto in modo plausibile con DefensiveMatchup:
 *   * ~35%: esce in PRESSIONE sul PORTATORE (passatore)
 *   * ~65%: marca/intercetta in zona DESTINATARIO
 * - Pesi difensivi leggermente diversi tra pressione e intercetto.
 */
public class PassaggioCortoHelper {


    private static final Random random = new Random();

    // ==============================
    // Versione breve (retrocompat.)
    // ==============================
    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        // fallback alla versione estesa con ruoli null
        return genera(minuto, secondo, partita, squadraAttaccante, titolariAttacco, titolariDifesa, null, null);
    }

    // ======================================================
    // Versione estesa: specifica ruolo passatore/destinatario
    // ======================================================
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
                    .minuto(minuto).secondo(secondo).durataStimata(4)
                    .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                    .giocatorePrincipale(null).giocatoreSecondario(null)
                    .esito("NESSUN GIOCATORE DISPONIBILE")
                    .note("Impossibile eseguire passaggio corto: ruoli non trovati")
                    .partita(partita).squadra(squadraAttaccante)
                    .build();
        }

        Giocatore passatore = possibiliPassatori.get(random.nextInt(possibiliPassatori.size()));
        Giocatore destinatario = possibiliDestinatari.get(random.nextInt(possibiliDestinatari.size()));

        // Ruoli effettivi (se non specificati): dedotti dalla lista titolari
        Ruolo ruoloEffPassatore = (ruoloPassatore != null) ? ruoloPassatore : deduciRuoloDaTitolari(passatore, titolariAttacco);
        Ruolo ruoloEffDestinatario = (ruoloDestinatario != null) ? ruoloDestinatario : deduciRuoloDaTitolari(destinatario, titolariAttacco);

        // ---------- 2) Scelta difendente plausibile ----------
        // 35% pressione sul portatore, altrimenti marcatura/intercetto sul destinatario
        boolean pressioneSulPortatore = random.nextDouble() < 0.35;

        Giocatore difensore = pressioneSulPortatore
                ? DefensiveMatchup.scegliPressatoreSuPortatore(ruoloEffPassatore, titolariDifesa, random)
                : DefensiveMatchup.scegliIntercettoreSuDestinatario(ruoloEffDestinatario, titolariDifesa, random);

        // ---------- 3) Statistiche ----------
        StatisticheTecnicheGiocatore sp = passatore.getStatistiche();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        // Valutazione qualità passaggio (rimane invariata)
        double punteggioPassatore = sp.getTecnica() * 0.35 +
                sp.getVisione() * 0.25 +
                sp.getLetturaDelGioco() * 0.20 +
                sp.getGiocoDiSquadra() * 0.10 +
                random.nextInt(11);
        // Bias dolce a favore del passatore (SOLO per passaggio corto)
        punteggioPassatore += bonusPassaggioCorto(ruoloEffPassatore);
        // Momentum cumulativo per l'azione: applica il bonus
        punteggioPassatore += MomentumBonusManager.peek(partita, squadraAttaccante);



        // Valutazione difendente: pesi diversi per pressione vs intercetto
        double punteggioDifensore = pressioneSulPortatore
                ? (sd.getContrasti() * 0.40 +
                sd.getAggressivita()   * 0.25 +
                sd.getLetturaDelGioco() * 0.20 +
                sd.getIntercettazione() * 0.05 +
                random.nextInt(11))
                : (sd.getIntercettazione() * 0.50 +
                sd.getLetturaDelGioco()   * 0.35 +
                sd.getConcentrazione() * 0.10 +
                sd.getContrasti() * 0.05 +
                random.nextInt(11));

        // ---------- 4) Esiti ----------
        // Passaggio riuscito
        if (punteggioPassatore > punteggioDifensore) {
            //Evento riuscito -> accumula momentum per l'azione (+2)
            MomentumBonusManager.addSuccess(partita, squadraAttaccante);

            return EventoPartita.builder()
                    .minuto(minuto).secondo(secondo).durataStimata(4)
                    .tipoEvento(TipoEvento.PASSAGGIO)
                    .giocatorePrincipale(passatore)
                    .giocatoreSecondario(destinatario)
                    .esito("RIUSCITO")
                    .note("Passaggio corto riuscito")
                    .partita(partita).squadra(squadraAttaccante)
                    .build();
        }

        // Intercetto “netto” (gap evidente)
        if (punteggioDifensore - punteggioPassatore >= 10) {
            return EventoPartita.builder()
                    .minuto(minuto).secondo(secondo).durataStimata(4)
                    .tipoEvento(TipoEvento.INTERCETTO)
                    .giocatorePrincipale(difensore)
                    .giocatoreSecondario(passatore)
                    .esito("PALLA RECUPERATA")
                    .note(pressioneSulPortatore ? "Recupero in pressione sul portatore" : "Intercetto su linea di passaggio")
                    .partita(partita).squadra(difensore.getSquadra())
                    .build();
        }


        // Errore di misura
        return EventoPartita.builder()
                .minuto(minuto).secondo(secondo).durataStimata(4)
                .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                .giocatorePrincipale(passatore)
                .giocatoreSecondario(destinatario)
                .esito("FUORI MISURA")
                .note("Errore su passaggio corto")
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
                .orElse(g.getRuolo()); // fallback: ruolo naturale del giocatore
    }

    // ==========================================
    // Bonus "bias" per il passaggio corto (ruolo)
    // ------------------------------------------
    // i corti sono più sicuri per chi imposta da dietro.

    // ==========================================
    private static int bonusPassaggioCorto(Ruolo ruolo) {
        switch (ruolo) {
            case PORTIERE:                   return 20;
            case DIFENSORE_CENTRALE:         return 10;
            case TERZINO_DX:
            case TERZINO_SX:                 return 9;
            case CENTROCAMPISTA_DIFENSIVO:   return 9;
            case CENTROCAMPISTA_CENTRALE:    return 8;
            case CENTROCAMPISTA_OFFENSIVO:   return 7;
            case ALA_DX:
            case ALA_SX:                     return 6;
            case ATTACCANTE_ESTERNO_DX:
            case ATTACCANTE_ESTERNO_SX:      return 6;
            case SECONDA_PUNTA:              return 5;
            case BOMBER:                     return 4;
            default:                         return 0;
        }
    }

}


