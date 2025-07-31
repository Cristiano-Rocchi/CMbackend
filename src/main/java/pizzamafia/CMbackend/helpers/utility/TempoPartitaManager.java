package pizzamafia.CMbackend.helpers.utility;


import lombok.Getter;

@Getter
public class TempoPartitaManager {
    private int minuto;
    private int secondo;

    public TempoPartitaManager(int minutoIniziale) {
        this.minuto = minutoIniziale;
        this.secondo = 0;
    }

    public void avanzaDi(int secondi) {
        int totaleSecondi = minuto * 60 + secondo + secondi;
        this.minuto = totaleSecondi / 60;
        this.secondo = totaleSecondi % 60;
    }
}

