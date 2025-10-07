package pizzamafia.CMbackend.ValutazioneGiocatore;
import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.StatisticheTecnicheGiocatore;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.valutazioni.ValutazioneGiocatoreHelper;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttaccanteEsternoValutazioneTest {

    @Test
    void calcolaValoreTecnico_ATT_ESTERNO_DX_breakdown() {
        eseguiTestAttaccanteEsterno(Ruolo.ATTACCANTE_ESTERNO_DX, "ATTACCANTE_ESTERNO_DX");
    }

    private void eseguiTestAttaccanteEsterno(Ruolo ruolo, String label) {
        // ===== Stats esempio per Attaccante Esterno =====
        StatisticheTecnicheGiocatore s = new StatisticheTecnicheGiocatore();

        // [A] PILASTRO — 60%
        s.setScatto(92);
        s.setAccelerazione(93);
        s.setTecnica(88);
        s.setLetturaDelGioco(82);
        s.setDribbling(90);
        s.setTiro(84);

        // [B] SUPPORTO — 35%
        s.setFinalizzazione(83);
        s.setAssist(80);
        s.setCreativita(86);
        s.setCoraggio(78);
        s.setResistenza(79);
        s.setFreddezza(81);
        s.setCross(77);

        // [C] MARGINALI — 5%
        s.setEquilibrio(75);
        s.setColpoDiTesta(70);
        s.setGiocoDiSquadra(79);
        s.setConcentrazione(76);
        s.setForzaFisica(73);

        // Bonus C/L
        s.setCarisma(76);
        s.setLeadership(82);

        // Altri irrilevanti a piacere (0%)
        s.setMarcatura(40);
        s.setContrasti(42);
        s.setIntercettazione(41);
        s.setCalciPiazzati(65);
        s.setAggressivita(60);
        s.setElevazione(55);
        s.setTuffo(5); s.setRiflessi(5); s.setPosizione(5); s.setUscite(5); s.setPresa(5);

        // ===== Pesi ATTACCANTE_ESTERNO_DX/SX =====
        Map<String, Double> pesi = new LinkedHashMap<>();
        // [A] 60%
        pesi.put("scatto",           13.0);
        pesi.put("accelerazione",    13.0);
        pesi.put("tecnica",          12.0);
        pesi.put("letturaDelGioco",   8.0);
        pesi.put("dribbling",         7.0);
        pesi.put("tiro",              7.0);
        // [B] 35%
        pesi.put("finalizzazione",    6.0);
        pesi.put("assist",            6.0);
        pesi.put("creativita",        6.0);
        pesi.put("coraggio",          5.0);
        pesi.put("resistenza",        4.0);
        pesi.put("freddezza",         4.0);
        pesi.put("cross",             4.0);
        // [C] 5%
        pesi.put("equilibrio",        1.0);
        pesi.put("colpoDiTesta",      1.0);
        pesi.put("giocoDiSquadra",    1.0);
        pesi.put("concentrazione",    1.0);
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
        System.out.println("Helper result                  = " + calcolato + " [" + label + "]");
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

