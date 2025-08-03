package pizzamafia.CMbackend.helpers.azioni.tikitaka;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.eventi.*;
import pizzamafia.CMbackend.helpers.utility.TempoPartitaManager;

import java.util.ArrayList;
import java.util.List;

public class TikiTakaHelper {


    // Azione tiki-taka 1:
    // Passaggio (difensore → difensore) →
    // Passaggio (difensore → centrocampista) →
    // Passaggio (centrocampista → centrocampista) →
    // Dribbling (centrocampista) →
    // Passaggio (centrocampista → seconda punta) →
    // Tiro (seconda punta)


    public static List<EventoPartita> eseguiTikiTaka1(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            List<Ruolo> ruoli
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio (difensore → difensore) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0), ruoli.get(1));
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio difensore → centrocampista) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(2));
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Passaggio (centrocampista → centrocampista) =====
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2), ruoli.get(3));
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 4. Dribbling (centrocampista) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(3));
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 5. Passaggio (centrocampista → attaccante) =====
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(3), ruoli.get(4));
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 6. Tiro (attaccante) =====
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
    // Passaggio (difensore→ centrocampista) →
    // Passaggio (centrocampista → difensore) →
    // Passaggio (difensore → centrocampista) →
    // Passaggio (centrocampista → attaccante) →
    // Passaggio (attaccante → centrocampista) →
    // Tiro (centrocampista)


    public static List<EventoPartita> eseguiTikiTaka2(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            List<Ruolo> ruoli
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio (difensore → centrocampista) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0), ruoli.get(1));
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio (centrocampista→ difensore) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(2));
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Passaggio (difensore → centrocampista) =====
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2), ruoli.get(3));
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 4. Passaggio (centrocampista → attaccante) =====
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(3), ruoli.get(4));
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 5. Passaggio (attaccante → centrocampista) =====
        EventoPartita p5 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(4), ruoli.get(3));
        eventi.add(p5);
        if (!p5.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 6. Tiro (centrocampista) =====
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
    // Passaggio (difensore → centrocampista) →
    // Passaggio (centrocampista → difensore) →
    // Passaggio (difensore → difensore) →
    // Passaggio (difensore → centrocampista) →
    // Passaggio lungo (centrocampista → attaccante) →
    // Cross (attaccante → bomber) →
    // Colpo di testa (bomber)


    public static List<EventoPartita> eseguiTikiTaka3(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            List<Ruolo> ruoli
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio (difensore → centrocampista) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0), ruoli.get(1));
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio (centrocampista → difensore) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(2));
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Passaggio (difensore → difensore) =====
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2), ruoli.get(0));
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);


        // ===== 4. Passaggio (difensore → centrocampista) =====
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0), ruoli.get(3));
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 5. Passaggio lungo (centrocampista → attaccante) =====
        EventoPartita p5 = PassaggioLungoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(3), ruoli.get(4));
        eventi.add(p5);
        if (!p5.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(6);
        // ===== 6. Cross (attaccante → bomber) =====
        EventoPartita cross = CrossHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(4), ruoli.get(5));
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
    // Passaggio (centrocampista offensivo → seconda punta) →
    // Dribbling (seconda punta) →
    // Tiro (seconda punta)


    public static List<EventoPartita> eseguiTikiTaka4(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            List<Ruolo> ruoli
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio (centrocampista → difensore) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0), ruoli.get(1));
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio (difensore → centrocampista) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(2));
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Passaggio (centrocampista → difensore) =====
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2), ruoli.get(3));
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 4. Passaggio (difensore → centrocampista) =====
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(3), ruoli.get(4));
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 5. Passaggio (centrocampista offensivo → centrocampista offensivo) =====
        EventoPartita p5 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(4), ruoli.get(4));
        eventi.add(p5);
        if (!p5.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 6. Passaggio (centrocampista offensivo → seconda punta) =====
        EventoPartita p6 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(4), ruoli.get(5));
        eventi.add(p6);
        if (!p6.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 7. Dribbling (seconda punta) =====
        EventoPartita dribbling = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(5));
        eventi.add(dribbling);
        if (!dribbling.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 8. Tiro (seconda punta) =====
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
    // Passaggio (centrocampista → centrocampista ) →
    // Passaggio (centrocampista  → attaccante) →
    // Passaggio (attaccante → centrocampista) →
    // Passaggio (centrocampista → centrocampista) →
    // Dribbling (centrocampista) →
    // Passaggio (centrocampista → centrocampista) →
    // Tiro (centrocampista)


    public static List<EventoPartita> eseguiTikiTaka5(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa,
            List<Ruolo> ruoli
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio (centrocampista → centrocampista) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0), ruoli.get(1));
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio (centrocampista → attaccante) =====
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(1), ruoli.get(2));
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 3. Passaggio (attaccante → centrocampista) =====
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(2), ruoli.get(0));
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 4. Passaggio (centrocampista → centrocampista ) =====
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(0), ruoli.get(3));
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 5. Dribbling (centrocampista) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(3));
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 6. Passaggio (centrocampista → centrocampista) =====
        EventoPartita p5 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                ruoli.get(3), ruoli.get(0));
        eventi.add(p5);
        if (!p5.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);


        // ===== 7. Tiro (centrocampista) =====
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
