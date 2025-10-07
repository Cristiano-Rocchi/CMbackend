package pizzamafia.CMbackend.ValutazioneGiocatore;


import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.StatisticheTecnicheGiocatore;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.valutazioni.ValutazioneGiocatoreHelper;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CentrocampCentraleValutazioneTest {

    @Test
    void calcolaValoreTecnico_CC_breakdown() {
        // ===== Stats esempio per CC =====
        StatisticheTecnicheGiocatore s = new StatisticheTecnicheGiocatore();
        // Pilastri
        s.setTecnica(88);
        s.setVisione(90);
        s.setLetturaDelGioco(85);
        s.setGiocoDiSquadra(86);
        // Supporto
        s.setConcentrazione(82);
        s.setCreativita(84);
        s.setResistenza(80);
        s.setEquilibrio(78);
        s.setAssist(81);
        s.setTiro(79);
        s.setAggressivita(70);
        s.setContrasti(72);
        s.setDribbling(83);
        s.setCalciPiazzati(76);
        // Marginali
        s.setFinalizzazione(68);
        s.setMarcatura(66);
        s.setColpoDiTesta(65);
        s.setIntercettazione(71);
        s.setForzaFisica(74);
        // Bonus C/L
        s.setCarisma(80);
        s.setLeadership(82);
        // Altri irrilevanti
        s.setScatto(75);
        s.setAccelerazione(76);
        s.setFreddezza(73);
        s.setCross(60);
        s.setElevazione(60);
        s.setPresa(10); s.setUscite(10); s.setTuffo(10); s.setRiflessi(10); s.setPosizione(10);

        // ===== Pesi CENTROCAMPISTA_CENTRALE =====
        Map<String, Double> pesi = new LinkedHashMap<>();
        // [A] PILASTRO — 55%
        pesi.put("tecnica",           15.0);
        pesi.put("visione",           14.0);
        pesi.put("letturaDelGioco",   13.0);
        pesi.put("giocoDiSquadra",    13.0);
        // [B] SUPPORTO — 40%
        pesi.put("concentrazione",     6.0);
        pesi.put("creativita",         6.0);
        pesi.put("resistenza",         5.0);
        pesi.put("equilibrio",         4.0);
        pesi.put("assist",             4.0);
        pesi.put("tiro",               4.0);
        pesi.put("aggressivita",       3.0);
        pesi.put("contrasti",          3.0);
        pesi.put("dribbling",          3.0);
        pesi.put("calciPiazzati",      2.0);
        // [C] MARGINALI — 5%
        pesi.put("finalizzazione",     1.0);
        pesi.put("marcatura",          1.0);
        pesi.put("colpoDiTesta",       1.0);
        pesi.put("intercettazione",    1.0);
        pesi.put("forzaFisica",        1.0);
        // [D] 0% non inclusi

        // ===== Breakdown manuale =====
        double sommaPesi = 0.0;
        double punteggioGrezzo = 0.0;

        System.out.println("=== CENTROCAMPISTA CENTRALE: Breakdown contributi ===");
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
        int calcolato = ValutazioneGiocatoreHelper.calcolaValoreTecnico(Ruolo.CENTROCAMPISTA_CENTRALE, s);
        System.out.println("Helper result                  = " + calcolato);

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

