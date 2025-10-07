package pizzamafia.CMbackend.ValutazioneGiocatore;
import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.StatisticheTecnicheGiocatore;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.valutazioni.ValutazioneGiocatoreHelper;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortiereValutazioneTest {

    @Test
    void calcolaValoreTecnico_PORTIERE_breakdown() {
        // ===== Stats esempio per Portiere =====
        StatisticheTecnicheGiocatore s = new StatisticheTecnicheGiocatore();

        // [A] PILASTRO — 75%
        s.setTuffo(91);
        s.setRiflessi(93);
        s.setPosizione(90);
        s.setUscite(88);
        s.setPresa(89);

        // [B] SUPPORTO — 25%
        s.setConcentrazione(86);
        s.setElevazione(78);
        s.setCoraggio(82);
        s.setEquilibrio(80);
        s.setTecnica(70);

        // Bonus C/L
        s.setCarisma(84);
        s.setLeadership(88);

        // Irrilevanti (0%)
        s.setDribbling(50);
        s.setTiro(40);
        s.setColpoDiTesta(55);
        s.setAssist(30);
        s.setFinalizzazione(35);
        s.setContrasti(45);
        s.setMarcatura(46);
        s.setIntercettazione(44);
        s.setCreativita(60);
        s.setAggressivita(50);
        s.setScatto(52);
        s.setAccelerazione(53);
        s.setCalciPiazzati(20);
        s.setCross(25);
        s.setGiocoDiSquadra(70);
        s.setVisione(68);
        s.setResistenza(72);
        s.setFreddezza(76);
        s.setForzaFisica(74);

        // ===== Pesi PORTIERE (come nel tuo helper) =====
        Map<String, Double> pesi = new LinkedHashMap<>();
        // [A] 75%
        pesi.put("tuffo",       18.0);
        pesi.put("riflessi",    18.0);
        pesi.put("posizione",   18.0);
        pesi.put("uscite",      12.0);
        pesi.put("presa",       12.0);
        // [B] 25%
        pesi.put("concentrazione", 5.0);
        pesi.put("elevazione",     5.0);
        pesi.put("coraggio",       5.0);
        pesi.put("equilibrio",     5.0);
        pesi.put("tecnica",        2.0);
        // [D] 0% non inclusi

        // ===== Breakdown manuale =====
        double sommaPesi = 0.0;
        double punteggioGrezzo = 0.0;

        System.out.println("=== PORTIERE : Breakdown contributi ===");
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
        int calcolato = ValutazioneGiocatoreHelper.calcolaValoreTecnico(Ruolo.PORTIERE, s);
        System.out.println("Helper result                  = " + calcolato + " [PORTIERE]");
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

