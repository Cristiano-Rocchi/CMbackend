package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.List;
import java.util.Random;

public class PassaggioCortoHelper {

    private static final Random random = new Random();

    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        // =================== 1. Estrai i giocatori coinvolti ===================
        Giocatore passatore = titolariAttacco.get(random.nextInt(titolariAttacco.size())).getGiocatore();
        Giocatore difensore = titolariDifesa.get(random.nextInt(titolariDifesa.size())).getGiocatore();

        // =================== 2. Statistiche ===================
        StatisticheTecnicheGiocatore sp = passatore.getStatistiche();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        // =================== 3. Punteggi ===================
        double punteggioPassatore = sp.getTecnica() * 0.45 +
                sp.getCreativita() * 0.25 +
                sp.getGiocoDiSquadra() * 0.2 +
                sp.getCarisma() * 0.1 +
                random.nextInt(11);

        double punteggioDifensore = sd.getPosizione() * 0.4 +
                sd.getIntuito() * 0.3 +
                sd.getAggressivita() * 0.2 +
                sd.getContrasti() * 0.1 +
                random.nextInt(11);

        // =================== 4. Esito: Riuscito ===================
        if (punteggioPassatore > punteggioDifensore) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .durataStimata(4)
                    .tipoEvento(TipoEvento.PASSAGGIO)
                    .giocatorePrincipale(passatore)
                    .giocatoreSecondario(null)
                    .esito("RIUSCITO")
                    .note("Passaggio corto riuscito")
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();
        }

        // =================== 5. Esito: Intercetto ===================
        if (punteggioDifensore - punteggioPassatore >= 10) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .durataStimata(4)
                    .tipoEvento(TipoEvento.INTERCETTO)
                    .giocatorePrincipale(difensore)
                    .giocatoreSecondario(passatore)
                    .esito("PALLA RECUPERATA")
                    .note("Intercetto su passaggio corto")
                    .partita(partita)
                    .squadra(difensore.getSquadra())
                    .build();
        }

        // =================== 6. Esito: Errore passaggio ===================
        return EventoPartita.builder()
                .minuto(minuto)
                .secondo(secondo)
                .durataStimata(4)
                .tipoEvento(TipoEvento.ERRORE_PASSAGGIO)
                .giocatorePrincipale(passatore)
                .giocatoreSecondario(null)
                .esito("FUORI MISURA")
                .note("Errore su passaggio corto")
                .partita(partita)
                .squadra(squadraAttaccante)
                .build();
    }
}
