package pizzamafia.CMbackend.Eventi;

import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.enums.TipoEvento;
import pizzamafia.CMbackend.helpers.eventi.TiroHelper;
import pizzamafia.CMbackend.helpers.utility.DefensiveMatchup;
import pizzamafia.CMbackend.helpers.utility.MomentumBonusManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TiroTest {

    @Test
    void tiro_simulazioneConDettagli() {
        // ====== Setup tiratore (serve: Lettura, Tecnica, Equilibrio, Finalizzazione, Tiro, Freddezza) ======
        StatisticheTecnicheGiocatore st = new StatisticheTecnicheGiocatore();
        st.setLetturaDelGioco(91);
        st.setTecnica(90);
        st.setEquilibrio(79);
        st.setFinalizzazione(89);
        st.setTiro(87);
        st.setFreddezza(81);
        Giocatore tiratore = Giocatore.builder()
                .id(UUID.randomUUID())
                .nome("Attaccante")
                .cognome("1")
                .ruolo(Ruolo.BOMBER)
                .statistiche(st)
                .build();

        // ====== Setup difensori (serve: Marcatura, Contrasti, Lettura) + Portiere (serve: Tuffo, Riflessi, Posizione) ======
        // Difensore 1
        StatisticheTecnicheGiocatore sd1 = new StatisticheTecnicheGiocatore();
        sd1.setMarcatura(96);
        sd1.setContrasti(93);
        sd1.setLetturaDelGioco(95);
        Giocatore dif1 = Giocatore.builder()
                .id(UUID.randomUUID())
                .nome("Difensore")
                .cognome("Forte")
                .ruolo(Ruolo.DIFENSORE_CENTRALE)
                .statistiche(sd1)
                .build();

        // Difensore 2
        StatisticheTecnicheGiocatore sd2 = new StatisticheTecnicheGiocatore();
        sd2.setMarcatura(74);
        sd2.setContrasti(77);
        sd2.setLetturaDelGioco(73);
        Giocatore dif2 = Giocatore.builder()
                .id(UUID.randomUUID())
                .nome("Difensore")
                .cognome("Debole")
                .ruolo(Ruolo.DIFENSORE_CENTRALE)
                .statistiche(sd2)
                .build();

        // Portiere
        StatisticheTecnicheGiocatore sp = new StatisticheTecnicheGiocatore();
        sp.setTuffo(85);
        sp.setRiflessi(86);
        sp.setPosizione(84);
        Giocatore portiere = Giocatore.builder()
                .id(UUID.randomUUID())
                .nome("Portiere")
                .cognome("1")
                .ruolo(Ruolo.PORTIERE)
                .statistiche(sp)
                .build();

        // Lista titolari difesa (deve contenere il portiere + difensori)
        List<Titolari> titolariDifesa = new ArrayList<>();
        titolariDifesa.add(Titolari.builder().giocatore(portiere).ruolo(Ruolo.PORTIERE).build());
        titolariDifesa.add(Titolari.builder().giocatore(dif1).ruolo(Ruolo.DIFENSORE_CENTRALE).build());
        titolariDifesa.add(Titolari.builder().giocatore(dif2).ruolo(Ruolo.DIFENSORE_CENTRALE).build());

        // ====== Partita e squadre ======
        Partita partita = Partita.builder().id(UUID.randomUUID()).build();

        Squadra att = Squadra.builder()
                .id(UUID.randomUUID())
                .nome("Attacco")
                .build();

        Squadra dif = Squadra.builder()
                .id(UUID.randomUUID())
                .nome("Difesa")
                .build();

        // ====== Seed variabile: risultati diversi ad ogni Play ======
        long seed = System.nanoTime();
        TiroHelper.setRandomForTest(new Random(seed));

        // ====== Genera evento (consuma random internamente) ======
        EventoPartita evento = TiroHelper.genera(
                63,  // minuto
                11,  // secondo
                partita,
                att,
                dif,
                tiratore,
                titolariDifesa
        );

        // ====== REPLAY dei random con lo stesso seed e nella STESSA sequenza ======
        Random replay = new Random(seed);

        // 0) Scelta difensore che esce sul tiratore (consuma random dentro DefensiveMatchup)
        Giocatore pressatore = DefensiveMatchup.scegliPressatoreSuPortatore(tiratore.getRuolo(), titolariDifesa, replay);

        // 1) Duello pre-tiro: stabilità tiratore vs contest difensore (ognuno + random 0..10)
        //    Momentum: lo prendiamo dagli stessi oggetti (se la tua Momentum usa stato reale, qui risulterà lo stesso valore)
        double momentum = MomentumBonusManager.peek(partita, att);

        double stabilitaTiratoreBase =
                st.getLetturaDelGioco() * 0.50 +
                        st.getTecnica()         * 0.30 +
                        st.getEquilibrio()      * 0.20;
        int rndStabilita = replay.nextInt(11);
        double stabilitaTiratoreTot = stabilitaTiratoreBase + rndStabilita + momentum;

        StatisticheTecnicheGiocatore sdPress = pressatore.getStatistiche();
        double contestDifensoreBase =
                sdPress.getMarcatura()      * 0.50 +
                        sdPress.getContrasti()      * 0.30 +
                        sdPress.getLetturaDelGioco()* 0.20;
        int rndContest = replay.nextInt(11);
        double contestDifensoreTot = contestDifensoreBase + rndContest;

        // Se contest vince di >= 8, tiro murato
        boolean murato = (contestDifensoreTot - stabilitaTiratoreTot) >= 8;

        // 2) Se non murato: calcolo punteggio tiro (con momentum)
        double punteggioTiroBase =
                st.getFinalizzazione() * 0.40 +
                        st.getTiro()           * 0.20 +
                        st.getLetturaDelGioco()* 0.20 +
                        st.getFreddezza()      * 0.20;
        int rndTiro = 0;
        double punteggioTiroTot = punteggioTiroBase + momentum;
        if (!murato) {
            rndTiro = replay.nextInt(11);
            punteggioTiroTot += rndTiro;
        }

        // 3) Parata portiere: se non murato e non fuori
        double parataBase =
                sp.getTuffo()   * 0.50 +
                        sp.getRiflessi()* 0.30 +
                        sp.getPosizione()* 0.20;
        int rndParata = 0;
        double parataTot = parataBase;
        boolean fuori = false;
        if (!murato) {
            // in helper: se punteggioTiro < 60 => fuori
            fuori = (punteggioTiroTot < 60);
            if (!fuori) {
                rndParata = replay.nextInt(11);
                parataTot += rndParata;
            }
        }

        // ====== Assert minimi sull'evento ======
        assertNotNull(evento.getTipoEvento());
        assertNotNull(evento.getEsito());

        // ====== Stampa dettagli ======
        System.out.println("=== SIMULAZIONE TIRO ===");
        System.out.println("Seed: " + seed);
        System.out.printf("Tiratore: %s %s  | Ruolo=%s%n", tiratore.getNome(), tiratore.getCognome(), tiratore.getRuolo());
        System.out.printf("Pressatore: %s %s | Ruolo=%s%n", pressatore.getNome(), pressatore.getCognome(), pressatore.getRuolo());
        System.out.println("--- Duello pre-tiro ---");

// Breakdown Stabilità Tiratore = Lettura*0.50 + Tecnica*0.30 + Equilibrio*0.20
        double stabLettura   = st.getLetturaDelGioco() * 0.50;
        double stabTecnica   = st.getTecnica()         * 0.30;
        double stabEquilibrio= st.getEquilibrio()      * 0.20;
        System.out.printf("Stabilità Tiratore:%n");
        System.out.printf("  LetturaDelGioco=%d * 0.50 = %.2f%n", st.getLetturaDelGioco(), stabLettura);
        System.out.printf("  Tecnica        =%d * 0.30 = %.2f%n", st.getTecnica(),         stabTecnica);
        System.out.printf("  Equilibrio     =%d * 0.20 = %.2f%n", st.getEquilibrio(),      stabEquilibrio);
        System.out.printf("  Somma base                 = %.2f%n", stabilitaTiratoreBase);
        System.out.printf("  + random (0..10)          = %d%n",   rndStabilita);
        System.out.printf("  + momentum                 = %.2f%n", momentum);
        System.out.printf("  => Stabilità TOT          = %.2f%n%n", stabilitaTiratoreTot);

// Breakdown Contest Difensore = Marcatura*0.50 + Contrasti*0.30 + Lettura*0.20
        int marc = sdPress.getMarcatura(), contr = sdPress.getContrasti(), lett = sdPress.getLetturaDelGioco();
        double contMarc   = marc  * 0.50;
        double contContr  = contr * 0.30;
        double contLett   = lett  * 0.20;
        System.out.printf("Contest Difensore (%s %s):%n", pressatore.getNome(), pressatore.getCognome());
        System.out.printf("  Marcatura=%d * 0.50 = %.2f%n", marc,  contMarc);
        System.out.printf("  Contrasti=%d * 0.30 = %.2f%n", contr, contContr);
        System.out.printf("  Lettura  =%d * 0.20 = %.2f%n", lett,  contLett);
        System.out.printf("  Somma base            = %.2f%n", contestDifensoreBase);
        System.out.printf("  + random (0..10)      = %d%n",   rndContest);
        System.out.printf("  => Contest TOT        = %.2f%n%n", contestDifensoreTot);

// Delta e soglia murato
        double deltaMurato = contestDifensoreTot - stabilitaTiratoreTot;
        System.out.printf("Delta contest - stabilità = %.2f (soglia murato = 8.00)%n", deltaMurato);
        System.out.println("Murato? " + murato);


        if (!murato) {
            System.out.println("--- Tiro ---");
// Punteggio tiro = Finalizzazione*0.40 + Tiro*0.20 + Lettura*0.20 + Freddezza*0.20
            double tiroFinal  = st.getFinalizzazione() * 0.40;
            double tiroTiro   = st.getTiro()           * 0.20;
            double tiroLett   = st.getLetturaDelGioco()* 0.20;
            double tiroFred   = st.getFreddezza()      * 0.20;
            System.out.printf("  Finalizzazione=%d * 0.40 = %.2f%n", st.getFinalizzazione(), tiroFinal);
            System.out.printf("  Tiro          =%d * 0.20 = %.2f%n", st.getTiro(),           tiroTiro);
            System.out.printf("  Lettura       =%d * 0.20 = %.2f%n", st.getLetturaDelGioco(), tiroLett);
            System.out.printf("  Freddezza     =%d * 0.20 = %.2f%n", st.getFreddezza(),       tiroFred);
            System.out.printf("  Somma base                 = %.2f%n", punteggioTiroBase);
            System.out.printf("  + random (0..10)          = %d%n",   rndTiro);
            System.out.printf("  + momentum                 = %.2f%n", momentum);
            System.out.printf("  => Tiro TOT               = %.2f%n", punteggioTiroTot);


            if (fuori) {
                System.out.println("Esito calcolo: FUORI (punteggioTiro < 60)");
            } else {
                System.out.println("--- Parata ---");
// Parata = Tuffo*0.50 + Riflessi*0.30 + Posizione*0.20
                System.out.printf("  Tuffo     =%d * 0.50 = %.2f%n", sp.getTuffo(),     sp.getTuffo()     * 0.50);
                System.out.printf("  Riflessi  =%d * 0.30 = %.2f%n", sp.getRiflessi(),  sp.getRiflessi()  * 0.30);
                System.out.printf("  Posizione =%d * 0.20 = %.2f%n", sp.getPosizione(), sp.getPosizione() * 0.20);
                System.out.printf("  Somma base            = %.2f%n", parataBase);
                System.out.printf("  + random (0..10)      = %d%n",   rndParata);
                System.out.printf("  => Parata TOT         = %.2f%n", parataTot);

                System.out.printf("Confronto finale: %s (ParataTot=%.2f vs TiroTot=%.2f)%n",
                        (parataTot > punteggioTiroTot ? "PARATA" : "GOL"), parataTot, punteggioTiroTot);

            }
        }

        System.out.println("--- ESITO EVENTO ---");
        System.out.println("Tipo evento: " + evento.getTipoEvento());
        System.out.println("Esito: " + evento.getEsito());
        System.out.println("Note: " + evento.getNote());
        if (evento.getGiocatorePrincipale() != null)
            System.out.println("Giocatore principale: " + evento.getGiocatorePrincipale().getNome());
        if (evento.getGiocatoreSecondario() != null)
            System.out.println("Giocatore secondario: " + evento.getGiocatoreSecondario().getNome());

        // Coerenza di base
        if (murato) {
            assertEquals(TipoEvento.INTERCETTO, evento.getTipoEvento());
            assertEquals("TIRO MURATO", evento.getEsito());
        } else if (fuori) {
            assertEquals(TipoEvento.TIRO, evento.getTipoEvento());
            assertEquals("FUORI", evento.getEsito());
        } else {
            assertTrue(evento.getTipoEvento() == TipoEvento.PARATA || evento.getTipoEvento() == TipoEvento.GOL);
        }
        System.out.println("==============================\n");
    }
}
