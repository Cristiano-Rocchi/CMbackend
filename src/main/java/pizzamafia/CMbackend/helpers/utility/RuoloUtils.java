package pizzamafia.CMbackend.helpers.utility;

import pizzamafia.CMbackend.enums.Ruolo;

public class RuoloUtils {

    public static boolean èDifensore(Ruolo ruolo) {
        return ruolo == Ruolo.DIFENSORE_CENTRALE || ruolo == Ruolo.TERZINO;
    }

    public static boolean èCentrocampista(Ruolo ruolo) {
        return ruolo == Ruolo.CENTROCAMPISTA_DIFENSIVO ||
                ruolo == Ruolo.CENTROCAMPISTA_CENTRALE ||
                ruolo == Ruolo.CENTROCAMPISTA_OFFENSIVO ||
                ruolo == Ruolo.ALA;
    }

    public static boolean èAttaccante(Ruolo ruolo) {
        return ruolo == Ruolo.BOMBER ||
                ruolo == Ruolo.SECONDA_PUNTA ||
                ruolo == Ruolo.ATTACCANTE_ESTERNO;

    }

    public static boolean èPortiere(Ruolo ruolo) {
        return ruolo == Ruolo.PORTIERE;
    }




    // usare questo per classi più compatte
    public static String getMacroRuolo(Ruolo ruolo) {
        if (èPortiere(ruolo)) return "PORTIERE";
        if (èDifensore(ruolo)) return "DIFENSORE";
        if (èCentrocampista(ruolo)) return "CENTROCAMPISTA";
        if (èAttaccante(ruolo)) return "ATTACCANTE";
        return "UNKNOWN";
    }
}
