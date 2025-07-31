package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.List;
import java.util.Random;

public class DribblingHelper {

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
        Giocatore attaccante = titolariAttacco.get(random.nextInt(titolariAttacco.size())).getGiocatore();
        Giocatore difensore = titolariDifesa.get(random.nextInt(titolariDifesa.size())).getGiocatore();

        // =================== 2. Ottieni le statistiche ===================
        StatisticheTecnicheGiocatore sa = attaccante.getStatistiche();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        // =================== 3. Calcola il punteggio di entrambi ===================
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

        // =================== 4. Verifica se l'attaccante ha superato il difensore ===================
        boolean superato = punteggioAttaccante > punteggioDifensore;

        // =================== 5. Se superato, può fare fallo o subire il dribbling ===================
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

        // =================== 6. Se NON superato → intercetto ===================
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
}
