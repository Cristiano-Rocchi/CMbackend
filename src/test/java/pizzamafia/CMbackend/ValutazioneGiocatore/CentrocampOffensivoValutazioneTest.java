package pizzamafia.CMbackend.ValutazioneGiocatore;
import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.StatisticheTecnicheGiocatore;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.valutazioni.ValutazioneGiocatoreHelper;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CentrocampOffensivoValutazioneTest {

    @Test
    void calcolaValoreTecnico_CCO_breakdown() {
        // ===== Stats esempio per CCO =====
        StatisticheTecnicheGiocatore s = new StatisticheTecnicheGiocatore();
        // Pilastri
        s.setTecnica(90);
        s.setVisione(92);
        s.setCreativita(91);
        s.setLetturaDelGioco(86);
        s.setDribbling(89);
        // Supporto
        s.setGiocoDiSquadra(84);
        s.setAssist(88);
        s.setResistenza(80);
        s.setCoraggio(78);
        s.setTiro(82);
        s.setEquilibrio(79);
        s.setCalciPiazzati(85);
        s.setConcentrazione(81);
        // Marginali
        s.setColpoDiTesta(65);
        s.setContrasti(60);
        s.setFreddezza(83);
        s.setForzaFisica(72);
        s.setFinalizzazione(80);
        // Bonus C/L
        s.setCarisma(82);
        s.setLeadership(79);
        // Altri irrilevanti
        s.setScatto(76);
        s.setAccelerazione(77);
        s.setCross(70);
        s.setMarcatura(58);
        s.setIntercettazione(62);
        s.setElevazione(60);
        s.setPresa(10); s.setUscite(10); s.setTuffo(10); s.setRiflessi(10); s.setPosizione(10);

        // ===== Pesi CENTROCAMPISTA_OFFENSIVO =====
        Map<String, Double> pesi = new LinkedHashMap<>();
        // [A] PILASTRO — 55%
        pesi.put("tecnica",           13.0);
        pesi.put("visione",           13.0);
        pesi.put("creativita",        13.0);
        pesi.put("letturaDelGioco",    8.0);
        pesi.put("dribbling",          8.0);
        // [B] SUPPORTO — 40%
        pesi.put("giocoDiSquadra",     6.0);
        pesi.put("assist",             6.0);
        pesi.put("resistenza",         5.0);
        pesi.put("coraggio",           5.0);
        pesi.put("tiro",               5.0);
        pesi.put("equilibrio",         4.0);
        pesi.put("calciPiazzati",      4.0);
        pesi.put("concentrazione",     5.0);
        // [C] MARGINALI — 5%
        pesi.put("colpoDiTesta",       1.0);
        pesi.put("contrasti",          1.0);
        pesi.put("freddezza",          1.0);
        pesi.put("forzaFisica",        1.0);
        pesi.put("finalizzazione",     1.0);
        // [D] 0% non inclusi

        // ===== Breakdown manuale =====
        double sommaPesi = 0.0;
        double punteggioGrezzo = 0.0;

        System.out.println("=== CENTROCAMPISTA OFFENSIVO: Breakdown contributi ===");
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

        // Bonus Carisma/Leadership (come helper)
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
        int calcolato = ValutazioneGiocatoreHelper.calcolaValoreTecnico(Ruolo.CENTROCAMPISTA_OFFENSIVO, s);
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

