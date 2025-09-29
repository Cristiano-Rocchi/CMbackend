package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.enums.TipoEvento;
import pizzamafia.CMbackend.helpers.utility.DefensiveMatchup;
import pizzamafia.CMbackend.helpers.utility.MomentumBonusManager;

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
        // ===== 1) Duello aereo con il difensore sul destinatario =====
        Ruolo ruoloAttaccante = attaccante.getRuolo();
        Giocatore difensore = DefensiveMatchup
                .scegliIntercettoreSuDestinatario(ruoloAttaccante, titolariDifesa, random);
        StatisticheTecnicheGiocatore sa = attaccante.getStatistiche();
        StatisticheTecnicheGiocatore sd = difensore.getStatistiche();

        double punteggioAttaccanteDuello = scoreDuelAereoAttaccante(sa)
                + MomentumBonusManager.peek(partita, squadraAttaccante);
        double punteggioDifensoreDuello = scoreDuelAereoDifensore(sd);

        if (punteggioDifensoreDuello > punteggioAttaccanteDuello) {
            return EventoPartita.builder()
                    .minuto(minuto).secondo(secondo).durataStimata(3)
                    .tipoEvento(TipoEvento.INTERCETTO)
                    .giocatorePrincipale(difensore)
                    .giocatoreSecondario(attaccante)
                    .esito("ANTICIPATO")
                    .note("Il difensore anticipa l'attaccante nel colpo di testa")
                    .partita(partita).squadra(squadraDifendente)
                    .build();
        }

        // ===== 2)confronto col portiere con set statistiche dedicato =====
        double pericolositaTesta = scoreTestaVsPortiereAttaccante(sa)
                + MomentumBonusManager.peek(partita, squadraAttaccante);

        // ===== 3) Possibile "tiro fuori" se pericolosità bassa =====
        if (pericolositaTesta < 60) {
            return EventoPartita.builder()
                    .minuto(minuto).secondo(secondo).durataStimata(3)
                    .tipoEvento(TipoEvento.TIRO)
                    .giocatorePrincipale(attaccante)
                    .giocatoreSecondario(null)
                    .esito("FUORI")
                    .note("Colpo di testa impreciso")
                    .partita(partita).squadra(squadraAttaccante)
                    .build();
        }

        // ===== 4) Confronto con il portiere (calcolo indipendente dal duello) =====
        Giocatore portiere = titolariDifesa.stream()
                .map(Titolari::getGiocatore)
                .filter(g -> g.getRuolo() == Ruolo.PORTIERE)
                .findFirst()
                .orElse(titolariDifesa.get(0).getGiocatore());

        StatisticheTecnicheGiocatore sp = portiere.getStatistiche();
        double parata = scoreParataPortiere(sp);

        boolean parato = parata > pericolositaTesta;

        return EventoPartita.builder()
                .minuto(minuto).secondo(secondo).durataStimata(3)
                .tipoEvento(parato ? TipoEvento.PARATA : TipoEvento.GOL)
                .giocatorePrincipale(parato ? portiere : attaccante)
                .giocatoreSecondario(parato ? attaccante : null)
                .esito(parato ? "RIUSCITA" : "RETE")
                .note(parato ? "Colpo di testa parato dal portiere" : "Colpo di testa in rete")
                .partita(partita).squadra(parato ? squadraDifendente : squadraAttaccante)
                .build();
    }

    // =========================== HELPER SCORES ===========================

    // Duello aereo (attaccante vs difensore)
    private static double scoreDuelAereoAttaccante(StatisticheTecnicheGiocatore s) {
        return s.getElevazione() * 0.3 +
                s.getLetturaDelGioco()   * 0.3 +
                s.getColpoDiTesta()    * 0.3 +
                s.getEquilibrio()      * 0.1 +
                random.nextInt(11);
    }

    private static double scoreDuelAereoDifensore(StatisticheTecnicheGiocatore s) {
        return s.getMarcatura()   * 0.3 +
                s.getLetturaDelGioco()   * 0.3 +
                s.getContrasti() * 0.3 +
                s.getEquilibrio()   * 0.1 +
                random.nextInt(11);
    }

    // Confronto col portiere: pericolosità del colpo di testa (NUOVO set di stats)
    private static double scoreTestaVsPortiereAttaccante(StatisticheTecnicheGiocatore s) {
        return s.getColpoDiTesta()   * 0.5 +
                s.getLetturaDelGioco()        * 0.2 +
                s.getFinalizzazione()  * 0.3 +
                random.nextInt(11);
    }

    // Capacità di parata del portiere
    private static double scoreParataPortiere(StatisticheTecnicheGiocatore s) {
        return s.getRiflessi() * 0.45 +
                s.getTuffo()  * 0.25 +
                s.getPosizione()* 0.20 +
                s.getConcentrazione()  * 0.10 +
                random.nextInt(11);
    }
}
