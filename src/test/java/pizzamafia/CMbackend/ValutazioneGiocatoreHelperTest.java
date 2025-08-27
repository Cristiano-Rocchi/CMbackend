package pizzamafia.CMbackend;

import org.junit.jupiter.api.Test;
import pizzamafia.CMbackend.entities.Giocatore;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.ValutazioneGiocatoreHelper;

import static org.junit.jupiter.api.Assertions.*;

class ValutazioneGiocatoreHelperTest {

    @Test
    void testSenzaPenalita() {
        Giocatore g = new Giocatore();
        g.setRuolo(Ruolo.CENTROCAMPISTA_OFFENSIVO);
        g.setValoreTecnico(80);

        int valoreEffettivo = ValutazioneGiocatoreHelper.calcolaValoreEffettivo(
                g,
                Ruolo.CENTROCAMPISTA_OFFENSIVO // stesso ruolo → nessuna penalità
        );

        assertEquals(80, valoreEffettivo);
    }

   


}
