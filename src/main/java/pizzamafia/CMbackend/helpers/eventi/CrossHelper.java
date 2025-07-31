package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.List;
import java.util.Random;

public class CrossHelper {

    private static final Random random = new Random();

    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        // ===== 1. Estrai i giocatori coinvolti =====
        Giocatore crossatore = titolariAttacco.get(random.nextInt(titolariAttacco.size())).getGiocatore();
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
                    .tipoEvento(TipoEvento.PASSAGGIO) // tipo generico
                    .giocatorePrincipale(crossatore)
                    .giocatoreSecondario(null)
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
