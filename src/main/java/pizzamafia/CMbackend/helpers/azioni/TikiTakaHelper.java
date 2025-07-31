package pizzamafia.CMbackend.helpers.azioni;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.helpers.eventi.*;
import pizzamafia.CMbackend.helpers.utility.TempoPartitaManager;
import pizzamafia.CMbackend.helpers.utility.TempoPartitaManager;

import java.util.ArrayList;
import java.util.List;

public class TikiTakaHelper {

    // Azione tiki-taka 1:
// Passaggio (difensore → difensore) → Passaggio (difensore → centrocampista) → Passaggio (centrocampista → centrocampista) → Dribbling → Passaggio → Tiro

// importa la classe

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

        // =================== 1. Passaggio difensore → difensore ===================
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4); // stimato: 4 secondi

        // =================== 2. Passaggio difensore → centrocampista ===================
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // =================== 3. Passaggio centrocampista → centrocampista ===================
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // =================== 4. Dribbling ===================
        EventoPartita d1 = DribblingHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(d1);
        if (!d1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(5); // leggermente più lunga

        // =================== 5. Passaggio centrocampista → attaccante ===================
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // =================== 6. Tiro ===================
        Giocatore tiratore = p4.getGiocatorePrincipale();
        EventoPartita t1 = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, squadraDifendente, tiratore, titolariDifesa);
        eventi.add(t1);
        tempo.avanzaDi(5); // anche il tiro leggermente più lungo

        return eventi;
    }




    // Azione tiki-taka 2:
// Passaggio (difensore → centrocampista) →
// Passaggio (centrocampista → difensore) →
// Passaggio (difensore → centrocampista) →
// Passaggio (centrocampista → attaccante) →
// Passaggio (attaccante → centrocampista) →
// Tiro

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

        // =================== 1. Passaggio difensore → centrocampista ===================
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(p1.getDurataStimata());

        // =================== 2. Passaggio centrocampista → difensore ===================
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(p2.getDurataStimata());

        // =================== 3. Passaggio difensore → centrocampista ===================
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(p3.getDurataStimata());

        // =================== 4. Passaggio centrocampista → attaccante ===================
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(p4.getDurataStimata());

        // =================== 5. Passaggio attaccante → centrocampista ===================
        EventoPartita p5 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p5);
        if (!p5.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(p5.getDurataStimata());

        // =================== 6. Tiro ===================
        Giocatore tiratore = p5.getGiocatorePrincipale();
        EventoPartita t1 = TiroHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, squadraDifendente, tiratore, titolariDifesa);
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
// Cross (attaccante) →
// Colpo di testa (attaccante)

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

        // =================== 1. Passaggio difensore → centrocampista ===================
        EventoPartita p1 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p1);
        if (!p1.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // =================== 2. Passaggio centrocampista → difensore ===================
        EventoPartita p2 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p2);
        if (!p2.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // =================== 3. Passaggio difensore → difensore ===================
        EventoPartita p3 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p3);
        if (!p3.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // =================== 4. Passaggio difensore → centrocampista ===================
        EventoPartita p4 = PassaggioCortoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p4);
        if (!p4.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(4);

        // =================== 5. Passaggio lungo centrocampista → attaccante ===================
        EventoPartita p5 = PassaggioLungoHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(p5);
        if (!p5.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(6);

        // =================== 6. Cross ===================
        EventoPartita cross = CrossHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, titolariAttacco, titolariDifesa);
        eventi.add(cross);
        if (!cross.getEsito().equals("RIUSCITO")) return eventi;
        tempo.avanzaDi(3);

        // =================== 7. Colpo di testa ===================
        Giocatore attaccante = cross.getGiocatorePrincipale();
        EventoPartita testa = ColpoDiTestaHelper.genera(
                tempo.getMinuto(), tempo.getSecondo(), partita, squadraAttaccante, squadraDifendente, attaccante, titolariDifesa);
        eventi.add(testa);
        tempo.avanzaDi(testa.getDurataStimata());

        return eventi;
    }





}
