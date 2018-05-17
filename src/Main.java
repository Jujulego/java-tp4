public class Main {
    public static void main(String[] args) {
        // Instantiations
        Portefeuille portefeuille = new Portefeuille();
        Console console = new Console();

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
        }

        console.afficherFonds(portefeuille);
        console.afficherInstruments(portefeuille);
        console.afficherPourcentagFonds("A", portefeuille);
    }
}
