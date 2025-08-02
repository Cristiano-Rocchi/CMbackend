package pizzamafia.CMbackend.helpers.azioni;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.eventi.*;
import pizzamafia.CMbackend.helpers.utility.TempoPartitaManager;

import java.util.ArrayList;
import java.util.List;

public class PallaLungaHelper {

    //1=DIFESA
    //2=CENTROCAMPO

    // Azione palla lunga 1:
    // Passaggio corto (difensore centrale → centrocampista centrale) →
    // Passaggio lungo (centrocampista centrale → seconda punta) →
    // Dribbling (seconda punta) →
    // Dribbling (seconda punta) →
    // Tiro (seconda punta)


    public static List<EventoPartita> eseguiPallaLunga1(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio corto (difensore centrale → centrocampista centrale) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.DIFENSORE_CENTRALE, Ruolo.CENTROCAMPISTA_CENTRALE);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio lungo (centrocampista centrale → seconda punta) =====
        EventoPartita p2 = PassaggioLungoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_CENTRALE, Ruolo.SECONDA_PUNTA);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(6);

        // ===== 3. Dribbling (seconda punta) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.SECONDA_PUNTA);
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 4. Dribbling (seconda punta) =====
        EventoPartita d2 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.SECONDA_PUNTA);
        eventi.add(d2);
        if (!d2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 5. Tiro (seconda punta) =====
        Giocatore tiratore = d2.getGiocatorePrincipale();
        EventoPartita tiro = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                tiratore, titolariDifesa);
        eventi.add(tiro);
        tempo.avanzaDi(tiro.getDurataStimata());

        return eventi;
    }


    // Azione palla lunga 2:
    // Passaggio corto (difensore centrale → centrocampista difensivo) →
    // Passaggio lungo (difensore centrale → attaccante esterno) →
    // Dribbling (attaccante esterno) →
    // Cross (attaccante esterno → bomber) →
    // Colpo di testa (bomber)


    public static List<EventoPartita> eseguiPallaLunga2(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio corto (difensore centrale → centrocampista difensivo) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.DIFENSORE_CENTRALE, Ruolo.CENTROCAMPISTA_DIFENSIVO);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Passaggio lungo (difensore centrale → attaccante esterno) =====
        EventoPartita p2 = PassaggioLungoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.DIFENSORE_CENTRALE, Ruolo.ATTACCANTE_ESTERNO);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(6);

        // ===== 3. Dribbling (attaccante esterno) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.ATTACCANTE_ESTERNO);
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 4. Cross (attaccante esterno → bomber) =====
        EventoPartita cross = CrossHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.ATTACCANTE_ESTERNO, Ruolo.BOMBER);
        eventi.add(cross);
        if (!cross.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(3);

        // ===== 5. Colpo di testa (bomber) =====
        Giocatore colpitore = cross.getGiocatoreSecondario();
        EventoPartita testa = ColpoDiTestaHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                colpitore, titolariDifesa);
        eventi.add(testa);
        tempo.avanzaDi(testa.getDurataStimata());

        return eventi;
    }


    // Azione palla lunga 3:
    // Passaggio corto (difensore centrale → centrocampista centrale) →
    // Dribbling (centrocampista centrale) →
    // Passaggio lungo (centrocampista centrale → seconda punta) →
    // Dribbling (seconda punta) →
    // Tiro (seconda punta)

    public static List<EventoPartita> eseguiPallaLunga3(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Passaggio corto (difensore centrale → centrocampista centrale) =====
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.DIFENSORE_CENTRALE, Ruolo.CENTROCAMPISTA_CENTRALE);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // ===== 2. Dribbling (centrocampista centrale) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_CENTRALE);
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 3. Passaggio lungo (centrocampista centrale → seconda punta) =====
        EventoPartita p2 = PassaggioLungoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_CENTRALE, Ruolo.SECONDA_PUNTA);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(6);

        // ===== 4. Dribbling (seconda punta) =====
        EventoPartita d2 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.SECONDA_PUNTA);
        eventi.add(d2);
        if (!d2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 5. Tiro (seconda punta) =====
        Giocatore tiratore = d2.getGiocatorePrincipale();
        EventoPartita tiro = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                tiratore, titolariDifesa);
        eventi.add(tiro);
        tempo.avanzaDi(tiro.getDurataStimata());

        return eventi;
    }


    // Azione palla lunga 4:
    // Dribbling (centrocampista offensivo) →
    // Passaggio lungo (centrocampista offensivo → seconda punta) →
    // Dribbling (seconda punta) →
    // Dribbling (seconda punta) →
    // Tiro (seconda punta)


    public static List<EventoPartita> eseguiPallaLunga4(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            List<Titolari> titolariAttacco,
            List<Titolari> titolariDifesa
    ) {
        List<EventoPartita> eventi = new ArrayList<>();
        TempoPartitaManager tempo = new TempoPartitaManager(minuto);

        // ===== 1. Dribbling (centrocampista offensivo) =====
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_OFFENSIVO);
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 2. Passaggio lungo (centrocampista offensivo → seconda punta) =====
        EventoPartita p1 = PassaggioLungoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.CENTROCAMPISTA_OFFENSIVO, Ruolo.SECONDA_PUNTA);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(6);

        // ===== 3. Dribbling (seconda punta) =====
        EventoPartita d2 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.SECONDA_PUNTA);
        eventi.add(d2);
        if (!d2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 4. Dribbling (seconda punta) =====
        EventoPartita d3 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, titolariAttacco, titolariDifesa,
                Ruolo.SECONDA_PUNTA);
        eventi.add(d3);
        if (!d3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5);

        // ===== 5. Tiro (seconda punta) =====
        Giocatore tiratore = d3.getGiocatorePrincipale();
        EventoPartita tiro = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(),
                partita, squadraAttaccante, squadraDifendente,
                tiratore, titolariDifesa);
        eventi.add(tiro);
        tempo.avanzaDi(tiro.getDurataStimata());

        return eventi;
    }








}
