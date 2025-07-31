package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.List;
import java.util.Random;

public class PassaggioLungoHelper {

    private static final Random random = new Random();

    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        // ===== 1. Estrai giocatori coinvolti =====
        Giocatore passatore = titolariAttacco.get(random.nextInt(titolariAttacco.size())).getGiocatore();
        Giocatore difensore = titolariDifesa.get(random.nextInt(titolariDifesa.size())).getGiocatore();

        // ===== 2. Statistiche =====
        StatisticheTecnicheGiocatore sp = passatore.getStatistiche();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        // ===== 3. Calcola punteggi con random =====
        double punteggioPassatore = sp.getTecnica() * 0.4 +
                sp.getCreativita() * 0.3 +
                sp.getGiocoDiSquadra() * 0.15 +
                sp.getCarisma() * 0.15 +
                random.nextInt(11); // +0–10

        double punteggioDifensore = sd.getPosizione() * 0.4 +
                sd.getIntuito() * 0.3 +
                sd.getAggressivita() * 0.2 +
                sd.getContrasti() * 0.1 +
                random.nextInt(11);

        // ===== 4. Esito: RIUSCITO / INTERCETTO / ERRORE_PASSAGGIO =====
        if (punteggioPassatore > punteggioDifensore) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .durataStimata(6)
                    .tipoEvento(TipoEvento.PASSAGGIO)
                    .giocatorePrincipale(passatore)
                    .giocatoreSecondario(null)
                    .esito("RIUSCITO")
                    .note("Passaggio lungo riuscito")
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();
        } else if (punteggioDifensore - punteggioPassatore >= 10) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .durataStimata(6)
                    .tipoEvento(TipoEvento.INTERCETTO)
                    .giocatorePrincipale(difensore)
                    .giocatoreSecondario(passatore)
                    .esito("PALLA RECUPERATA")
                    .note("Intercetto su passaggio lungo")
                    .partita(partita)
                    .squadra(difensore.getSquadra())
                    .build();
        } else {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .durataStimata(6)
                    .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                    .giocatorePrincipale(passatore)
                    .giocatoreSecondario(null)
                    .esito("FUORI MISURA")
                    .note("Passaggio lungo sbagliato")
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();
        }
    }
}
