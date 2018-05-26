import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Instrument implements Serializable {
    // Attributs
    private String cle;
    private ArrayList<Fonds> fonds = new ArrayList<>();

    // Constructeur
    public Instrument(String cle) {
        this.cle = cle;
    }

    // Méthodes
    public void ajouterFonds(Fonds fonds) {
        this.fonds.add(fonds);
    }

    public double pourcentageFonds(String cle) {
        double somme = 0;
        double amount = 0;

        for (Fonds fonds : fonds) {
            somme += fonds.getAmount();

            if (fonds.getCle().equals(cle)) {
                amount += fonds.getAmount();
            }
        }

        return (amount / somme) * 100;
    }

    public void trierFonds() {
        Collections.sort(fonds);
    }

    public void viderFonds() {
        fonds.clear();
    }

    // - accesseurs
    public String getCle() {
        return cle;
    }

    public ArrayList<Fonds> getFonds() {
        return fonds;
    }
}
