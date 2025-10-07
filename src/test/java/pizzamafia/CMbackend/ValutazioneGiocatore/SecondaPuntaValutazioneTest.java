package pizzamafia.CMbackend.ValutazioneGiocatore;
import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.StatisticheTecnicheGiocatore;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.valutazioni.ValutazioneGiocatoreHelper;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecondaPuntaValutazioneTest {

    @Test
    void calcolaValoreTecnico_SECONDA_PUNTA_breakdown() {
        // ===== Stats esempio per Seconda Punta =====
        StatisticheTecnicheGiocatore s = new StatisticheTecnicheGiocatore();

        // [A] PILASTRO — 60%
        s.setTecnica(88);
        s.setCreativita(90);
        s.setVisione(89);
        s.setLetturaDelGioco(86);
        s.setAssist(84);

        // [B] SUPPORTO — 35%
        s.setDribbling(83);
        s.setTiro(82);
        s.setCoraggio(78);
        s.setFinalizzazione(81);
        s.setGiocoDiSquadra(80);
        s.setCalciPiazzati(76);
        s.setFreddezza(79);
        s.setConcentrazione(77);

        // [C] MARGINALI — 5%
        s.setEquilibrio(75);
        s.setColpoDiTesta(70);
        s.setScatto(74);
        s.setForzaFisica(72);
        s.setResistenza(73);

        // Bonus C/L
        s.setCarisma(78);
        s.setLeadership(82);

        // Altri irrilevanti (0%)
        s.setMarcatura(40);
        s.setContrasti(41);
        s.setIntercettazione(42);
        s.setCross(60);
        s.setAggressivita(55);
        s.setElevazione(50);
        s.setTuffo(5); s.setRiflessi(5); s.setPosizione(5); s.setUscite(5); s.setPresa(5);

        // ===== Pesi SECONDA_PUNTA =====
        Map<String, Double> pesi = new LinkedHashMap<>();
        // [A] 60%
        pesi.put("tecnica",           12.0);
        pesi.put("creativita",        12.0);
        pesi.put("visione",           12.0);
        pesi.put("letturaDelGioco",   12.0);
        pesi.put("assist",            12.0);
        // [B] 35%
        pesi.put("dribbling",          6.0);
        pesi.put("tiro",               5.0);
        pesi.put("coraggio",           5.0);
        pesi.put("finalizzazione",     5.0);
        pesi.put("giocoDiSquadra",     4.0);
        pesi.put("calciPiazzati",      4.0);
        pesi.put("freddezza",          3.0);
        pesi.put("concentrazione",     3.0);
        // [C] 5%
        pesi.put("equilibrio",         1.0);
        pesi.put("colpoDiTesta",       1.0);
        pesi.put("scatto",             1.0);
        pesi.put("forzaFisica",        1.0);
        pesi.put("resistenza",         1.0);
        // [D] 0% non inclusi

        // ===== Breakdown manuale =====
        double sommaPesi = 0.0;
        double punteggioGrezzo = 0.0;

        System.out.println("=== SECONDA_PUNTA : Breakdown contributi ===");
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
        int calcolato = ValutazioneGiocatoreHelper.calcolaValoreTecnico(Ruolo.SECONDA_PUNTA, s);
        System.out.println("Helper result                  = " + calcolato + " [SECONDA_PUNTA]");
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
