package pizzamafia.CMbackend.ValutazioneGiocatore;
import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.StatisticheTecnicheGiocatore;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.valutazioni.ValutazioneGiocatoreHelper;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlaValutazioneTest {

    @Test
    void calcolaValoreTecnico_ALA_DX_breakdown() {
        eseguiTestAla(Ruolo.ALA_DX, "ALA_DX");
    }

    @Test
    void calcolaValoreTecnico_ALA_SX_breakdown() {
        eseguiTestAla(Ruolo.ALA_SX, "ALA_SX");
    }

    private void eseguiTestAla(Ruolo ruolo, String label) {
        // ===== Stats esempio per ALA =====
        StatisticheTecnicheGiocatore s = new StatisticheTecnicheGiocatore();

        // Pilastri (55%)
        s.setCross(86);
        s.setContrasti(72);
        s.setLetturaDelGioco(84);
        s.setConcentrazione(80);
        s.setResistenza(85);

        // Supporto (40%)
        s.setAssist(83);
        s.setMarcatura(68);
        s.setDribbling(88);
        s.setGiocoDiSquadra(82);
        s.setScatto(90);
        s.setAccelerazione(91);
        s.setAggressivita(70);
        s.setTecnica(85);
        s.setIntercettazione(69);

        // Marginali (5%)
        s.setTiro(79);
        s.setEquilibrio(76);
        s.setVisione(81);
        s.setCreativita(80);
        s.setForzaFisica(74);

        // Bonus C/L
        s.setCarisma(79);
        s.setLeadership(83);

        // Altri irrilevanti (0%)
        s.setColpoDiTesta(60);
        s.setElevazione(60);
        s.setFinalizzazione(77);
        s.setCalciPiazzati(65);
        s.setFreddezza(78);
        s.setUscite(10); s.setPresa(10); s.setTuffo(10); s.setRiflessi(10); s.setPosizione(10);

        // ===== Pesi ALA_DX/ALA_SX =====
        Map<String, Double> pesi = new LinkedHashMap<>();
        // [A] PILASTRO — 55%
        pesi.put("cross",            11.0);
        pesi.put("contrasti",        11.0);
        pesi.put("letturaDelGioco",  11.0);
        pesi.put("concentrazione",   11.0);
        pesi.put("resistenza",       11.0);
        // [B] SUPPORTO — 40%
        pesi.put("assist",            6.0);
        pesi.put("marcatura",         5.0);
        pesi.put("dribbling",         5.0);
        pesi.put("giocoDiSquadra",    5.0);
        pesi.put("scatto",            5.0);
        pesi.put("accelerazione",     5.0);
        pesi.put("aggressivita",      3.0);
        pesi.put("tecnica",           3.0);
        pesi.put("intercettazione",   3.0);
        // [C] MARGINALI — 5%
        pesi.put("tiro",              1.0);
        pesi.put("equilibrio",        1.0);
        pesi.put("visione",           1.0);
        pesi.put("creativita",        1.0);
        pesi.put("forzaFisica",       1.0);
        // [D] 0% non inclusi

        // ===== Breakdown manuale =====
        double sommaPesi = 0.0;
        double punteggioGrezzo = 0.0;

        System.out.println("=== " + label + " : Breakdown contributi ===");
        for (Map.Entry<String, Double> e : pesi.entrySet()) {
            String attr = e.getKey();
            double peso = e.getValue();
            double val = getStatByName(s, attr);
            double contrib = val * peso;
            sommaPesi += peso;
            punteggioGrezzo += contrib;
            System.out.printf("%-18s = %5.1f * %5.2f = %7.2f%n", attr, val, peso, contrib);
        }
        System.out.printf("Somma pesi = %.2f%n", sommaPesi);
        System.out.printf("Punteggio grezzo = %.2f%n", punteggioGrezzo);

        double maxPunteggio = 99.0 * sommaPesi;
        double normalizzato = (punteggioGrezzo / maxPunteggio) * 99.0;

        // Bonus Carisma/Leadership
        double clAvg = (s.getCarisma() + s.getLeadership()) / 2.0;
        double bonus = (clAvg >= 95) ? 3.0 :
                (clAvg >= 90) ? 2.0 :
                        (clAvg >= 85) ? 1.5 :
                                (clAvg >= 80) ? 1.0 :
                                        (clAvg >= 75) ? 0.5 : 0.0;

        double finalCapped = Math.min(99.0, normalizzato + bonus);
        int atteso = (int) Math.round(finalCapped);

        System.out.printf("MaxPunteggio (99 * sommaPesi) = %.2f%n", maxPunteggio);
        System.out.printf("Normalizzato                    = %.2f%n", normalizzato);
        System.out.printf("Bonus(Carisma/Leadership)      = %.2f (CL avg=%.2f)%n", bonus, clAvg);
        System.out.printf("Finale (cap 99)                = %.2f -> arrotondato = %d%n", finalCapped, atteso);

        // ===== Confronto con helper =====
        int calcolato = ValutazioneGiocatoreHelper.calcolaValoreTecnico(ruolo, s);
        System.out.println("Valutazione Finale                  = " + calcolato + " [" + label + "]");
        assertEquals(atteso, calcolato);
    }

    private double getStatByName(StatisticheTecnicheGiocatore s, String name) {
        return switch (name) {
            case "tecnica" -> s.getTecnica();
            case "equilibrio" -> s.getEquilibrio();
            case "colpoDiTesta" -> s.getColpoDiTesta();
            case "tiro" -> s.getTiro();
            case "assist" -> s.getAssist();
            case "finalizzazione" -> s.getFinalizzazione();
            case "dribbling" -> s.getDribbling();
            case "visione" -> s.getVisione();
            case "calciPiazzati" -> s.getCalciPiazzati();
            case "cross" -> s.getCross();
            case "contrasti" -> s.getContrasti();
            case "marcatura" -> s.getMarcatura();
            case "intercettazione" -> s.getIntercettazione();
            case "carisma" -> s.getCarisma();
            case "concentrazione" -> s.getConcentrazione();
            case "coraggio" -> s.getCoraggio();
            case "leadership" -> s.getLeadership();
            case "letturaDelGioco" -> s.getLetturaDelGioco();
            case "giocoDiSquadra" -> s.getGiocoDiSquadra();
            case "creativita" -> s.getCreativita();
            case "freddezza" -> s.getFreddezza();
            case "aggressivita" -> s.getAggressivita();
            case "accelerazione" -> s.getAccelerazione();
            case "scatto" -> s.getScatto();
            case "elevazione" -> s.getElevazione();
            case "forzaFisica" -> s.getForzaFisica();
            case "resistenza" -> s.getResistenza();
            case "tuffo" -> s.getTuffo();
            case "riflessi" -> s.getRiflessi();
            case "posizione" -> s.getPosizione();
            case "uscite" -> s.getUscite();
            case "presa" -> s.getPresa();
            default -> 0.0;
        };
    }
}

