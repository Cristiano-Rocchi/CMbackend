package pizzamafia.CMbackend;

import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.*;
import pizzamafia.CMbackend.helpers.SimulazionePartitaHelper;

import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class SimulazionePartitaHelperTest {

    // ------- util di costruzione minime ENTITÀ -------

    private Allenatore allenatore(IdeaDiGioco idea) {
        Allenatore a = new Allenatore();
        a.setNome("Pep");
        a.setCognome("Coach");
        a.setIdeaDiGioco(idea);
        return a;
    }

    private Squadra squadra(String nome, Allenatore allenatore) {
        Squadra s = new Squadra();
        s.setNome(nome);
        s.setAllenatore(allenatore);
        return s;
    }

    private Partita partita(Squadra casa, Squadra trasferta) {
        return Partita.builder()
                .id(UUID.randomUUID())
                .squadraCasa(casa)
                .squadraTrasferta(trasferta)
                .goalCasa(0)
                .goalTrasferta(0)
                .dataOra(LocalDateTime.now())
                .competizione(Competizione.AMICHEVOLE)
                .build();
    }

    private Formazione formazione(Partita p, Squadra s, Modulo modulo) {
        Formazione f = new Formazione();
        f.setPartita(p);
        f.setSquadra(s);
        f.setModulo(modulo);
        return f;
    }

    private StatisticheTecnicheGiocatore statsBasePerRuolo(Ruolo r, int base) {
        // setto tutte le stats a "base" (es. 70) e poi do piccoli bonus in base al ruolo
        StatisticheTecnicheGiocatore st = new StatisticheTecnicheGiocatore();
        st.setAccelerazione(base);
        st.setAgilita(base);
        st.setElevazione(base);
        st.setForza(base);
        st.setResistenza(base);
        st.setScatto(base);
        st.setAggressivita(base);
        st.setCarisma(base);
        st.setCoraggio(base);
        st.setCreativita(base);
        st.setDeterminazione(base);
        st.setGiocoDiSquadra(base);
        st.setImpegno(base);
        st.setIntuito(base);
        st.setPosizione(base);
        st.setCalciPiazzati(base);
        st.setColpoDiTesta(base);
        st.setContrasti(base);
        st.setDribbling(base);
        st.setFinalizzazione(base);
        st.setMarcatura(base);
        st.setRiflessi(base);
        st.setTecnica(base);
        st.setAssist(base);
        st.setTiriDaLontano(base);
        st.setInserimento(base);

        switch (r) {
            case PORTIERE -> {
                st.setRiflessi(base + 8);
                st.setPosizione(base + 6);
                st.setAgilita(base + 4);
            }
            case DIFENSORE_CENTRALE -> {
                st.setMarcatura(base + 8);
                st.setContrasti(base + 7);
                st.setColpoDiTesta(base + 5);
                st.setPosizione(base + 6);
            }
            case TERZINO_DX, TERZINO_SX -> {
                st.setAccelerazione(base + 6);
                st.setResistenza(base + 6);
                st.setContrasti(base + 3);
                st.setAssist(base + 2);
            }
            case CENTROCAMPISTA_DIFENSIVO -> {
                st.setContrasti(base + 7);
                st.setMarcatura(base + 5);
                st.setPosizione(base + 6);
            }
            case CENTROCAMPISTA_CENTRALE -> {
                st.setTecnica(base + 5);
                st.setGiocoDiSquadra(base + 6);
                st.setResistenza(base + 4);
            }
            case CENTROCAMPISTA_OFFENSIVO -> {
                st.setCreativita(base + 7);
                st.setTecnica(base + 6);
                st.setAssist(base + 5);
                st.setDribbling(base + 4);
            }
            case ALA_DX, ALA_SX, ATTACCANTE_ESTERNO_DX, ATTACCANTE_ESTERNO_SX -> {
                st.setAccelerazione(base + 7);
                st.setScatto(base + 6);
                st.setDribbling(base + 5);
                st.setAssist(base + 3);
            }
            case SECONDA_PUNTA -> {
                st.setTecnica(base + 5);
                st.setCreativita(base + 4);
                st.setFinalizzazione(base + 4);
                st.setAssist(base + 3);
            }
            case BOMBER -> {
                st.setFinalizzazione(base + 8);
                st.setPosizione(base + 6);
                st.setColpoDiTesta(base + 5);
            }
        }
        return st;
    }

    private Giocatore giocatore(String nome, Ruolo ruolo, int valoreTecnico) {
        Giocatore g = new Giocatore();
        g.setId(UUID.randomUUID());
        g.setNome(nome);
        g.setCognome("Test");
        g.setRuolo(ruolo);
        g.setValoreTecnico(valoreTecnico);

        StatisticheTecnicheGiocatore stats = statsBasePerRuolo(ruolo, 70);


        stats.setGiocatore(g);     // lato owning (StatisticheTecnicheGiocatore)
        g.setStatistiche(stats);   // lato inverso (Giocatore)

        return g;
    }

    private Titolari titolare(Formazione f, Giocatore g, Ruolo ruolo, int valoreEffettivo) {
        Titolari t = new Titolari();
        t.setFormazione(f);
        t.setGiocatore(g);
        t.setRuolo(ruolo);
        t.setValoreEffettivo(valoreEffettivo);
        return t;
    }

    // ----------------------- TEST -----------------------

    @Test
    void simulaPartita_matchLike_generatesRicherSequence() {
        // setup
        Squadra casa = squadra("Casa FC", allenatore(IdeaDiGioco.TIKITAKA));
        Squadra trasferta = squadra("Trasferta FC", allenatore(IdeaDiGioco.PALLA_LUNGA));
        Partita p = partita(casa, trasferta);

        Formazione fCasa = formazione(p, casa, Modulo._4_4_2_1);
        Formazione fTras = formazione(p, trasferta, Modulo._4_3_3_1);

        List<Titolari> titolariCasa = new ArrayList<>();
        titolariCasa.add(titolare(fCasa, giocatore("POR1", Ruolo.PORTIERE, 72), Ruolo.PORTIERE, 73));
        titolariCasa.add(titolare(fCasa, giocatore("DC1", Ruolo.DIFENSORE_CENTRALE, 70), Ruolo.DIFENSORE_CENTRALE, 72));
        titolariCasa.add(titolare(fCasa, giocatore("TDX", Ruolo.TERZINO_DX, 68), Ruolo.TERZINO_DX, 69));
        titolariCasa.add(titolare(fCasa, giocatore("TSX", Ruolo.TERZINO_SX, 68), Ruolo.TERZINO_SX, 69));
        titolariCasa.add(titolare(fCasa, giocatore("CCD", Ruolo.CENTROCAMPISTA_DIFENSIVO, 74), Ruolo.CENTROCAMPISTA_DIFENSIVO, 75));
        titolariCasa.add(titolare(fCasa, giocatore("CC1", Ruolo.CENTROCAMPISTA_CENTRALE, 74), Ruolo.CENTROCAMPISTA_CENTRALE, 75));
        titolariCasa.add(titolare(fCasa, giocatore("CO", Ruolo.CENTROCAMPISTA_OFFENSIVO, 76), Ruolo.CENTROCAMPISTA_OFFENSIVO, 77));
        titolariCasa.add(titolare(fCasa, giocatore("ADX", Ruolo.ATTACCANTE_ESTERNO_DX, 73), Ruolo.ATTACCANTE_ESTERNO_DX, 74));
        titolariCasa.add(titolare(fCasa, giocatore("ASX", Ruolo.ATTACCANTE_ESTERNO_SX, 73), Ruolo.ATTACCANTE_ESTERNO_SX, 74));
        titolariCasa.add(titolare(fCasa, giocatore("BOM", Ruolo.BOMBER, 80), Ruolo.BOMBER, 81));

        List<Titolari> titolariTras = new ArrayList<>();
        titolariTras.add(titolare(fTras, giocatore("POR2", Ruolo.PORTIERE, 71), Ruolo.PORTIERE, 72));
        titolariTras.add(titolare(fTras, giocatore("DC2", Ruolo.DIFENSORE_CENTRALE, 70), Ruolo.DIFENSORE_CENTRALE, 71));
        titolariTras.add(titolare(fTras, giocatore("TSX2", Ruolo.TERZINO_SX, 67), Ruolo.TERZINO_SX, 68));
        titolariTras.add(titolare(fTras, giocatore("TDX2", Ruolo.TERZINO_DX, 67), Ruolo.TERZINO_DX, 68));
        titolariTras.add(titolare(fTras, giocatore("CCD2", Ruolo.CENTROCAMPISTA_DIFENSIVO, 72), Ruolo.CENTROCAMPISTA_DIFENSIVO, 73));
        titolariTras.add(titolare(fTras, giocatore("CC2", Ruolo.CENTROCAMPISTA_CENTRALE, 72), Ruolo.CENTROCAMPISTA_CENTRALE, 74));
        titolariTras.add(titolare(fTras, giocatore("ADX2", Ruolo.ATTACCANTE_ESTERNO_DX, 74), Ruolo.ATTACCANTE_ESTERNO_DX, 75));
        titolariTras.add(titolare(fTras, giocatore("ASX2", Ruolo.ATTACCANTE_ESTERNO_SX, 74), Ruolo.ATTACCANTE_ESTERNO_SX, 75));
        titolariTras.add(titolare(fTras, giocatore("SP2", Ruolo.SECONDA_PUNTA, 75), Ruolo.SECONDA_PUNTA, 76));
        titolariTras.add(titolare(fTras, giocatore("BOM2", Ruolo.BOMBER, 78), Ruolo.BOMBER, 79));

        // act
        List<EventoPartita> eventi = SimulazionePartitaHelper.simulaPartita(p, titolariCasa, titolariTras);

        // assert
        assertNotNull(eventi);
        assertFalse(eventi.isEmpty(), "La simulazione dovrebbe generare almeno un evento");

        for (EventoPartita e : eventi) {
            assertTrue(e.getMinuto() >= 1 && e.getMinuto() <= 90, "Minuto fuori range");
            assertTrue(e.getSecondo() >= 0 && e.getSecondo() < 60, "Secondo fuori range");
        }

        // stampa diagnostica
        System.out.println("Eventi generati: " + eventi.size());
        eventi.stream().limit(30).forEach(ev ->
                System.out.printf("[%02d:%02d] %s - esito=%s%n",
                        ev.getMinuto(), ev.getSecondo(),
                        ev.getTipoEvento(), ev.getEsito())
        );
    }
}
