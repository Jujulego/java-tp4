import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Portefeuille implements Serializable {
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

    /**
     * Créee un nouveau fond et l'ajoute au portefeuille
     *
     * @param nom nom du fond
     * @param amount valeur du fond
     * @return Le fond nouvellement créé
     *
     * @throws FondsExistant En cas de fond préexistant
     */
    public Fonds ajouterFonds(String nom, double amount) throws FondsExistant {
        Fonds f = null;

        try {
            rechercheFonds(nom);
            throw new FondsExistant();

        } catch (FondsInexistant err) {
            f = new Fonds(nom, amount);
            fonds.put(nom, f);
        }

        return f;
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

    public Instrument ajouterFondInstrument(String nom, Fonds fonds) {
        Instrument i = instruments.get(nom);
        Instrument r = null;

        if (i == null) {
            r = i = new Instrument(nom);
            instruments.put(nom, i);
        }

        i.ajouterFonds(fonds);
        return r;
    }

    public void supprimerInstrument(String nom) throws InstrumentInexistant {
        if (instruments.remove(nom) == null) {
            throw new InstrumentInexistant();
        }
    }

    // - sérialization
    public static Portefeuille charger(File fichier) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier));
        Portefeuille portefeuille = (Portefeuille) ois.readObject();
        ois.close();

        return portefeuille;
    }

    public void sauvegarder(File fichier) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier));
        oos.writeObject(this);
        oos.close();
    }

    // - accesseurs
    public HashMap<String, Fonds> getFonds() {
        return fonds;
    }
    public HashMap<String, Instrument> getInstruments() {
        return instruments;
    }
}
