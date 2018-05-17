import java.util.ArrayList;
import java.util.HashMap;

public class Portefeuille {
    // Attributs
    private HashMap<String,Fonds> fonds = new HashMap<>();
    private HashMap<String,Instrument> instruments = new HashMap<>();

    // Constructeur
    public Portefeuille() {
    }

    // Méthodes
    public Fonds rechercheFonds(String nom) throws FondsInexistant {
        Fonds f = fonds.get(nom);

        if (f == null) {
            throw new FondsInexistant();
        }

        return f;
    }

    public void ajouterFonds(String nom, double amount) throws FondsExistant {
        try {
            rechercheFonds(nom);
            throw new FondsExistant();

        } catch (FondsInexistant err) {
            fonds.put(nom, new Fonds(nom, amount));
        }
    }

    public void supprimerFonds(String nom) throws FondsInexistant {
        if (fonds.remove(nom) == null) {
            throw new FondsInexistant();
        }
    }

    public ArrayList<Fonds> rechercheInstrument(String nom) throws InstrumentInexistant {
        Instrument i = instruments.get(nom);

        if (i == null) {
            throw new InstrumentInexistant();
        }

        return i.getFonds();
    }

    public void ajouterFondInstrument(String nom, Fonds fonds) {
        Instrument i = instruments.get(nom);

        if (i == null) {
            i = new Instrument();
            instruments.put(nom, i);
        }

        i.ajouterFonds(fonds);
    }

    public void supprimerInstrument(String nom) throws InstrumentInexistant {
        if (instruments.remove(nom) == null) {
            throw new InstrumentInexistant();
        }
    }

    // - accesseurs
    public HashMap<String, Fonds> getFonds() {
        return fonds;
    }
    public HashMap<String, Instrument> getInstruments() {
        return instruments;
    }
}
