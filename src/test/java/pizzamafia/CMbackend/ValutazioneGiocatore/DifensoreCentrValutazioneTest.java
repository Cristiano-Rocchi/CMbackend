package pizzamafia.CMbackend.ValutazioneGiocatore;

import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.StatisticheTecnicheGiocatore;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.valutazioni.ValutazioneGiocatoreHelper;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DifensoreCentrValutazioneTest {

    @Test
    void calcolaValoreTecnico_DifensoreCentrale_breakdown() {
        StatisticheTecnicheGiocatore s = new StatisticheTecnicheGiocatore();
        // Tecniche/Difensive
        s.setMarcatura(91);
        s.setContrasti(90);
        s.setIntercettazione(84);
        s.setLetturaDelGioco(88);
        s.setConcentrazione(86);
        s.setAggressivita(78);
        s.setColpoDiTesta(85);
        s.setElevazione(86);
        s.setCoraggio(82);
        s.setEquilibrio(80);
        s.setForzaFisica(84);
        s.setGiocoDiSquadra(79);
        s.setTecnica(70);
        // Marginali/Altri
        s.setVisione(65);
        s.setScatto(73);
        s.setAccelerazione(72);
        s.setResistenza(78);
        s.setCalciPiazzati(40);
        // Poco/irrilevanti per il ruolo
        s.setTiro(40);
        s.setAssist(38);
        s.setFinalizzazione(35);
        s.setDribbling(42);
        s.setCross(45);
        s.setFreddezza(60);
        s.setCreativita(50);
        s.setCarisma(82);
        s.setLeadership(84);
        // Portiere (irrilevanti)
        s.setTuffo(10); s.setRiflessi(10); s.setPosizione(10); s.setUscite(10); s.setPresa(10);

        // ===== Pesi DIFENSORE_CENTRALE (copiati dal tuo helper) =====
        Map<String, Double> pesi = new LinkedHashMap<>();
        // [A] PILASTRO — 60%
        pesi.put("marcatura",        14.0);
        pesi.put("contrasti",        14.0);
        pesi.put("letturaDelGioco",  14.0);
        pesi.put("concentrazione",   10.0);
        pesi.put("aggressivita",      8.0);
        // [B] SUPPORTO — 35%
        pesi.put("colpoDiTesta",      5.0);
        pesi.put("elevazione",        5.0);
        pesi.put("intercettazione",   5.0);
        pesi.put("coraggio",          5.0);
        pesi.put("equilibrio",        4.0);
        pesi.put("forzaFisica",       4.0);
        pesi.put("giocoDiSquadra",    4.0);
        pesi.put("tecnica",           3.0);
        // [C] MARGINALI — 5%
        pesi.put("visione",           1.5);
        pesi.put("scatto",            1.0);
        pesi.put("accelerazione",     1.0);
        pesi.put("resistenza",        1.0);
        pesi.put("calciPiazzati",     0.5);
        // [D] IRRILEVANTI — 0% (non servono in mappa)

        // ===== Breakdown manuale =====
        double sommaPesi = 0.0;
        double punteggioGrezzo = 0.0;

        System.out.println("=== DC: Breakdown contributi ===");
        for (Map.Entry<String, Double> e : pesi.entrySet()) {
            String attr = e.getKey();
            double peso = e.getValue();
            double val = getStatByName(s, attr);
            double contrib = val * peso;
            sommaPesi += peso;
            punteggioGrezzo += contrib;
            System.out.printf("%-17s = %5.1f * %5.2f = %7.2f%n", attr, val, peso, contrib);
        }
        System.out.printf("Somma pesi = %.2f%n", sommaPesi);
        System.out.printf("Punteggio grezzo = %.2f%n", punteggioGrezzo);

        double maxPunteggio = 99.0 * sommaPesi;
        double normalizzato = (punteggioGrezzo / maxPunteggio) * 99.0;

        // Bonus Carisma/Leadership (stessa logica del helper)
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

        // ===== Chiamata helper e asserzione =====
        int calcolato = ValutazioneGiocatoreHelper.calcolaValoreTecnico(Ruolo.DIFENSORE_CENTRALE, s);
        System.out.println("Valutazione Giocatore                  = " + calcolato);

        assertEquals(atteso, calcolato);
    }

    // — util per leggere l’attributo per nome —
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

