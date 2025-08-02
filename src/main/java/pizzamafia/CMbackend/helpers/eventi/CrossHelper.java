package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.List;
import java.util.Random;
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
        // ===== 1. Estrai i giocatori coinvolti =====
        List<Giocatore> possibiliCrossatori = (ruoloCrossatore == null)
                ? titolariAttacco.stream().map(Titolari::getGiocatore).toList()
                : titolariAttacco.stream()
                .filter(t -> t.getRuolo() == ruoloCrossatore)
                .map(Titolari::getGiocatore)
                .toList();

        List<Giocatore> possibiliDestinatari = (ruoloDestinatario == null)
                ? titolariAttacco.stream().map(Titolari::getGiocatore).toList()
                : titolariAttacco.stream()
                .filter(t -> t.getRuolo() == ruoloDestinatario)
                .map(Titolari::getGiocatore)
                .toList();

        if (possibiliCrossatori.isEmpty() || possibiliDestinatari.isEmpty()) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                    .giocatorePrincipale(null)
                    .giocatoreSecondario(null)
                    .esito("NESSUN GIOCATORE DISPONIBILE")
                    .note("Cross non eseguito: ruoli mancanti")
                    .durataStimata(3)
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();
        }

        Giocatore crossatore = possibiliCrossatori.get(random.nextInt(possibiliCrossatori.size()));
        Giocatore destinatario = possibiliDestinatari.get(random.nextInt(possibiliDestinatari.size()));
        Giocatore difensore = titolariDifesa.get(random.nextInt(titolariDifesa.size())).getGiocatore();

        // ===== 2. Statistiche =====
        StatisticheTecnicheGiocatore sa = crossatore.getStatistiche();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        double punteggioCross = sa.getTecnica() * 0.4 +
                sa.getGiocoDiSquadra() * 0.25 +
                sa.getCreativita() * 0.2 +
                sa.getAssist() * 0.15 +
                random.nextInt(11);

        double punteggioDifensore = sd.getPosizione() * 0.4 +
                sd.getIntuito() * 0.3 +
                sd.getAggressivita() * 0.2 +
                sd.getContrasti() * 0.1 +
                random.nextInt(11);

        // ===== 3. Esito: RIUSCITO / INTERCETTO / ERRORE_PASSAGGIO =====
        if (punteggioCross > punteggioDifensore) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .tipoEvento(TipoEvento.PASSAGGIO)
                    .giocatorePrincipale(crossatore)
                    .giocatoreSecondario(destinatario)
                    .esito("RIUSCITO")
                    .note("Cross riuscito")
                    .durataStimata(3)
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();

        } else if (punteggioDifensore - punteggioCross >= 10) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .tipoEvento(TipoEvento.INTERCETTO)
                    .giocatorePrincipale(difensore)
                    .giocatoreSecondario(crossatore)
                    .esito("PALLA RECUPERATA")
                    .note("Intercetto su cross")
                    .durataStimata(3)
                    .partita(partita)
                    .squadra(difensore.getSquadra())
                    .build();

        } else {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                    .giocatorePrincipale(crossatore)
                    .giocatoreSecondario(null)
                    .esito("FUORI MISURA")
                    .note("Cross sbagliato")
                    .durataStimata(3)
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();
        }
    }
}
