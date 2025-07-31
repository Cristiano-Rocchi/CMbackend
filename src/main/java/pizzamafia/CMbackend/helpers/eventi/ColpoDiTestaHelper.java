package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.List;
import java.util.Random;

public class ColpoDiTestaHelper {

    private static final Random random = new Random();

    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            Giocatore attaccante,
            List<Titolari> titolariDifesa
    ) {
        StatisticheTecnicheGiocatore sa = attaccante.getStatistiche();

        // =================== 1. Scontro aereo con il difensore ===================
        Giocatore difensore = titolariDifesa.get(random.nextInt(titolariDifesa.size())).getGiocatore();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        double punteggioAttaccante = sa.getColpoDiTesta() * 0.5 +
                sa.getElevazione() * 0.2 +
                sa.getPosizione() * 0.2 +
                sa.getIntuito() * 0.1 +
                random.nextInt(11);

        double punteggioDifensore = sd.getMarcatura() * 0.4 +
                sd.getPosizione() * 0.3 +
                sd.getAggressivita() * 0.2 +
                sd.getContrasti() * 0.1 +
                random.nextInt(11);

        if (punteggioDifensore > punteggioAttaccante) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .tipoEvento(TipoEvento.INTERCETTO)
                    .giocatorePrincipale(difensore)
                    .giocatoreSecondario(attaccante)
                    .esito("ANTICIPATO")
                    .note("Il difensore anticipa l'attaccante nel colpo di testa")
                    .durataStimata(3)
                    .partita(partita)
                    .squadra(squadraDifendente)
                    .build();
        }

        // =================== 2. Tiro fuori? ===================
        if (punteggioAttaccante < 60) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .secondo(secondo)
                    .tipoEvento(TipoEvento.TIRO)
                    .giocatorePrincipale(attaccante)
                    .giocatoreSecondario(null)
                    .esito("FUORI")
                    .note("Colpo di testa impreciso")
                    .durataStimata(3)
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();
        }

        // =================== 3. Confronto con il portiere ===================
        Giocatore portiere = titolariDifesa.stream()
                .map(Titolari::getGiocatore)
                .filter(g -> g.getRuolo() == Ruolo.PORTIERE)
                .findFirst()
                .orElse(titolariDifesa.get(0).getGiocatore());

        StatisticheTecnicheGiocatore sp = portiere.getStatistiche();

        double parata = sp.getRiflessi() * 0.45 +
                sp.getIntuito() * 0.25 +
                sp.getPosizione() * 0.2 +
                sp.getAgilita() * 0.1 +
                random.nextInt(11);

        boolean parato = parata > punteggioAttaccante;

        return EventoPartita.builder()
                .minuto(minuto)
                .secondo(secondo)
                .tipoEvento(parato ? TipoEvento.PARATA : TipoEvento.GOL)
                .giocatorePrincipale(parato ? portiere : attaccante)
                .giocatoreSecondario(parato ? attaccante : null)
                .esito(parato ? "RIUSCITA" : "RETE")
                .note(parato ? "Colpo di testa parato dal portiere" : "Colpo di testa in rete")
                .durataStimata(3)
                .partita(partita)
                .squadra(parato ? squadraDifendente : squadraAttaccante)
                .build();
    }
}
