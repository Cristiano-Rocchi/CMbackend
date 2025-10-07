package pizzamafia.CMbackend.Eventi;

import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.TipoEvento;
import pizzamafia.CMbackend.helpers.eventi.PunizioneDirettaHelper;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PunizioneDirettaTest {

    @Test
    void punizioneDiretta_simulazione() {
        // ===== setup tiratore =====
        StatisticheTecnicheGiocatore sa = new StatisticheTecnicheGiocatore();
        sa.setCalciPiazzati(96);
        sa.setTiro(83);
        sa.setConcentrazione(85);

        Giocatore tiratore = Giocatore.builder()
                .id(UUID.randomUUID())
                .nome("Attaccante")
                .cognome("1")
                .statistiche(sa)
                .build();

        // ===== setup portiere =====
        StatisticheTecnicheGiocatore sp = new StatisticheTecnicheGiocatore();
        sp.setTuffo(90);
        sp.setPosizione(91);
        sp.setRiflessi(84);

        Giocatore portiere = Giocatore.builder()
                .id(UUID.randomUUID())
                .nome("Portiere")
                .cognome("1")
                .statistiche(sp)
                .build();

        // ===== partita e squadre =====
        Partita partita = Partita.builder().id(UUID.randomUUID()).build();
        Squadra att = Squadra.builder().nome("Italia").build();
        Squadra dif = Squadra.builder().nome("Francia").build();

        // ===== calcoli base (senza random) — solo per console =====
        double baseTiro = sa.getCalciPiazzati() * 0.45
                + sa.getTiro()          * 0.35
                + sa.getConcentrazione()* 0.20;

        double baseParata = sp.getTuffo()    * 0.50
                + sp.getPosizione()* 0.30
                + sp.getRiflessi() * 0.20;

        System.out.println("=== SIMULAZIONE PUNIZIONE DIRETTA ===");
        System.out.printf("Tiratore: %s %s | CP=%d, Tiro=%d, Concentrazione=%d%n",
                tiratore.getNome(), tiratore.getCognome(),
                sa.getCalciPiazzati(), sa.getTiro(), sa.getConcentrazione());
        System.out.printf("Portiere: %s %s | Tuffo=%d, Posizione=%d, Riflessi=%d%n",
                portiere.getNome(), portiere.getCognome(),
                sp.getTuffo(), sp.getPosizione(), sp.getRiflessi());
        System.out.printf("Base Tiro (senza random): %.2f%n", baseTiro);
        System.out.printf("Base Parata (senza random): %.2f%n", baseParata);

        // ===== seed variabile: risultati diversi ad ogni Play =====
        long seed = System.nanoTime();
        PunizioneDirettaHelper.setRandomForTest(new Random(seed));

        // ===== genera evento (usa Random interno con il seed sopra) =====
        EventoPartita evento = PunizioneDirettaHelper.genera(
                27, 14, partita, att, dif, tiratore, portiere
        );

        // ===== RANDOM  =====
        Random replay = new Random(seed);
        int rndTiro = replay.nextInt(21);
        int rndParata = replay.nextInt(31);

        double tiroTot   = baseTiro   + rndTiro;
        double parataTot = baseParata + rndParata;

        // ===== assert minimi =====
        assertEquals(TipoEvento.PUNIZIONE, evento.getTipoEvento());
        assertNotNull(evento.getEsito());
        assertTrue(evento.getEsito().equals("GOL") || evento.getEsito().equals("PARATA"));

        // ===== stampa dettagli =====
        System.out.println("--- RANDOM & TOTALI ---");
        System.out.printf("rndTiro=%d  -> TiroTot=%.2f%n", rndTiro, tiroTot);
        System.out.printf("rndParata=%d -> ParataTot=%.2f%n", rndParata, parataTot);

        System.out.println("--- ESITO ---");
        System.out.println("Tipo evento: " + evento.getTipoEvento());
        System.out.println("Esito: " + evento.getEsito());
        System.out.println("Note: " + evento.getNote());

    }
}
