import java.io.*;

public class Main {
    public static void main(String[] args) {
        /*Portefeuille portefeuille;

        File fichier = new File("portefeuille.obj");
        Console console = new Console();

        // Ouverture !
        if (fichier.exists()) {
            try {
                portefeuille = Portefeuille.charger(fichier);

            } catch (IOException | ClassNotFoundException err) {
                portefeuille = new Portefeuille();
            }
        } else {
            portefeuille = new Portefeuille();
        }

        // Ajouts d'instrument
        try {
            portefeuille.ajouterFonds("A", 50);
            portefeuille.ajouterFonds("M", 150);
            portefeuille.ajouterFonds("T", 2);
            portefeuille.ajouterFonds("P", 1000);

            portefeuille.ajouterFondInstrument("Dead", portefeuille.rechercheFonds("M"));
            portefeuille.ajouterFondInstrument("Dead", portefeuille.rechercheFonds("A"));
            portefeuille.ajouterFondInstrument("Dead", portefeuille.rechercheFonds("T"));
            portefeuille.ajouterFondInstrument("Dead", portefeuille.rechercheFonds("P"));

            portefeuille.ajouterFondInstrument("Pool", portefeuille.rechercheFonds("T"));
            portefeuille.ajouterFondInstrument("Pool", portefeuille.rechercheFonds("A"));
            portefeuille.ajouterFondInstrument("Pool", portefeuille.rechercheFonds("M"));

        } catch (FondsExistant | FondsInexistant err) {
            System.out.println("Existe déjà !");
        }

        console.afficherFonds(portefeuille);
        console.afficherInstruments(portefeuille);
        console.afficherPourcentagFonds("A", portefeuille);

        // Enregistrement
        fichier.delete();

        try {
            fichier.createNewFile();
            portefeuille.sauvegarder(fichier);

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Fenetre fenetre = new Fenetre();
        fenetre.setVisible(true);
    }
}
