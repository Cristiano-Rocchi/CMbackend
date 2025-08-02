package pizzamafia.CMbackend.helpers.azioni;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.eventi.*;
import pizzamafia.CMbackend.helpers.utility.TempoPartitaManager;
import pizzamafia.CMbackend.helpers.utility.TempoPartitaManager;

import java.util.ArrayList;
import java.util.List;

public class TikiTakaHelper {


    // Azione tiki-taka 1:
    // Passaggio (difensore centrale → terzino) →
    // Passaggio (terzino → centrocampista difensivo) →
    // Passaggio (centrocampista difensivo → centrocampista offensivo) →
    // Dribbling (centrocampista offensivo) →
    // Passaggio (centrocampista offensivo → seconda punta) →
    // Tiro (seconda punta)


    public static List<EventoPartita> eseguiTikiTaka1(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio (difensore centrale → terzino) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.DIFENSORE_CENTRALE, Ruolo.TERZINO);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio (terzino → centrocampista difensivo) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.TERZINO, Ruolo.CENTROCAMPISTA_DIFENSIVO);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Passaggio (centrocampista difensivo → centrocampista offensivo) =====
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_DIFENSIVO, Ruolo.CENTROCAMPISTA_OFFENSIVO);
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 4. Dribbling (centrocampista offensivo) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_OFFENSIVO);
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 5. Passaggio (centrocampista offensivo → seconda punta) =====
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_OFFENSIVO, Ruolo.SECONDA_PUNTA);
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 6. Tiro (seconda punta) =====
        Giocatore tiratore = p4.getGiocatoreSecondario();
        EventoPartita t1 = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                tiratore, titolariDifesa);
        eventi.add(t1);
        tempo.avanzaDi(t1.getDurataStimata());

        return eventi;
    }





    // Azione tiki-taka 2:
    // Passaggio (difensore centrale → centrocampista centrale) →
    // Passaggio (centrocampista centrale → terzino) →
    // Passaggio (terzino → centrocampista offensivo) →
    // Passaggio (centrocampista offensivo → seconda punta) →
    // Passaggio (seconda punta → centrocampista offensivo) →
    // Tiro (centrocampista offensivo)


    public static List<EventoPartita> eseguiTikiTaka2(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio (difensore centrale → centrocampista centrale) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.DIFENSORE_CENTRALE, Ruolo.CENTROCAMPISTA_CENTRALE);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio (centrocampista centrale → terzino) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_CENTRALE, Ruolo.TERZINO);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Passaggio (terzino → centrocampista offensivo) =====
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.TERZINO, Ruolo.CENTROCAMPISTA_OFFENSIVO);
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 4. Passaggio (centrocampista offensivo → seconda punta) =====
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_OFFENSIVO, Ruolo.SECONDA_PUNTA);
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 5. Passaggio (seconda punta → centrocampista offensivo) =====
        EventoPartita p5 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.SECONDA_PUNTA, Ruolo.CENTROCAMPISTA_OFFENSIVO);
        eventi.add(p5);
        if (!p5.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 6. Tiro (centrocampista offensivo) =====
        Giocatore tiratore = p5.getGiocatoreSecondario();
        EventoPartita t1 = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                tiratore, titolariDifesa);
        eventi.add(t1);
        tempo.avanzaDi(t1.getDurataStimata());

        return eventi;
    }



    // Azione tiki-taka 3:
    // Passaggio (difensore centrale → centrocampista centrale) →
    // Passaggio (centrocampista centrale → terzino) →
    // Passaggio (terzino → difensore centrale) →
    // Passaggio (difensore centrale → centrocampista offensivo) →
    // Passaggio lungo (centrocampista offensivo → attaccante esterno) →
    // Cross (attaccante esterno → bomber) →
    // Colpo di testa (bomber)


    public static List<EventoPartita> eseguiTikiTaka3(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio (difensore centrale → centrocampista centrale) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.DIFENSORE_CENTRALE, Ruolo.CENTROCAMPISTA_CENTRALE);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio (centrocampista centrale → terzino) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_CENTRALE, Ruolo.TERZINO);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Passaggio (terzino → difensore centrale) =====
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.TERZINO, Ruolo.DIFENSORE_CENTRALE);
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 4. Passaggio (difensore centrale → centrocampista difensivo) =====
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.DIFENSORE_CENTRALE, Ruolo.CENTROCAMPISTA_DIFENSIVO);
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 5. Passaggio lungo (centrocampista difensivo → attaccante esterno) =====
        EventoPartita p5 = PassaggioLungoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_DIFENSIVO, Ruolo.ATTACCANTE_ESTERNO);
        eventi.add(p5);
        if (!p5.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(6);

        // ===== 6. Cross (attaccante esterno → bomber) =====
        EventoPartita cross = CrossHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.ATTACCANTE_ESTERNO, Ruolo.BOMBER);
        eventi.add(cross);
        if (!cross.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(3);

        // ===== 7. Colpo di testa (bomber) =====
        Giocatore bomber = cross.getGiocatoreSecondario();
        EventoPartita testa = ColpoDiTestaHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                bomber, titolariDifesa);
        eventi.add(testa);
        tempo.avanzaDi(testa.getDurataStimata());

        return eventi;
    }



    //2--------------------------CENTROCAMPO------------------
    // Azione tiki-taka 4:
    // Passaggio (centrocampista centrale → terzino) →
    // Passaggio (terzino → centrocampista difensivo) →
    // Passaggio (centrocampista difensivo → difensore centrale) →
    // Passaggio (difensore centrale → centrocampista offensivo) →
    // Passaggio (centrocampista offensivo → centrocampista offensivo) →
    // Dribbling (seconda punta) →
    // Tiro (seconda punta)


    public static List<EventoPartita> eseguiTikiTaka4(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio (centrocampista centrale → terzino) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_CENTRALE, Ruolo.TERZINO);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio (terzino → centrocampista difensivo) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.TERZINO, Ruolo.CENTROCAMPISTA_DIFENSIVO);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Passaggio (centrocampista difensivo → difensore centrale) =====
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_DIFENSIVO, Ruolo.DIFENSORE_CENTRALE);
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 4. Passaggio (difensore centrale → centrocampista offensivo) =====
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.DIFENSORE_CENTRALE, Ruolo.CENTROCAMPISTA_OFFENSIVO);
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 5. Passaggio (centrocampista offensivo → centrocampista offensivo) =====
        EventoPartita p5 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_OFFENSIVO, Ruolo.CENTROCAMPISTA_OFFENSIVO);
        eventi.add(p5);
        if (!p5.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 6. Dribbling (seconda punta) =====
        EventoPartita dribbling = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.SECONDA_PUNTA);
        eventi.add(dribbling);
        if (!dribbling.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 7. Tiro (seconda punta) =====
        Giocatore tiratore = dribbling.getGiocatorePrincipale();
        EventoPartita tiro = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                tiratore, titolariDifesa);
        eventi.add(tiro);
        tempo.avanzaDi(tiro.getDurataStimata());

        return eventi;
    }



    // Azione tiki-taka 5:
    // Passaggio (centrocampista centrale → centrocampista offensivo) →
    // Passaggio (centrocampista offensivo → seconda punta) →
    // Passaggio (seconda punta → centrocampista centrale) →
    // Passaggio (centrocampista centrale → centrocampista offensivo) →
    // Dribbling (centrocampista offensivo) →
    // Passaggio (centrocampista offensivo → centrocampista centrale) →
    // Tiro (centrocampista centrale)


    public static List<EventoPartita> eseguiTikiTaka5(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio (centrocampista centrale → centrocampista offensivo) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_CENTRALE, Ruolo.CENTROCAMPISTA_OFFENSIVO);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio (centrocampista offensivo → seconda punta) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_OFFENSIVO, Ruolo.SECONDA_PUNTA);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Passaggio (seconda punta → centrocampista centrale) =====
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.SECONDA_PUNTA, Ruolo.CENTROCAMPISTA_CENTRALE);
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 4. Passaggio (centrocampista centrale → centrocampista offensivo) =====
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_CENTRALE, Ruolo.CENTROCAMPISTA_OFFENSIVO);
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 5. Dribbling (centrocampista offensivo) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_OFFENSIVO);
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 6. Passaggio (centrocampista offensivo → centrocampista centrale) =====
        EventoPartita p5 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_OFFENSIVO, Ruolo.CENTROCAMPISTA_CENTRALE);
        eventi.add(p5);
        if (!p5.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 7. Tiro (centrocampista centrale) =====
        Giocatore tiratore = p5.getGiocatoreSecondario();
        EventoPartita tiro = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                tiratore, titolariDifesa);
        eventi.add(tiro);
        tempo.avanzaDi(tiro.getDurataStimata());

        return eventi;
    }








}
