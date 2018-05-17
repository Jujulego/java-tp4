import java.util.HashMap;

public class Console {
    // Méthodes
    public void afficherFonds(Portefeuille portefeuille) {
        System.out.println("+---------------------------+-------------+");
        System.out.println("|            Nom            |    Amount   |");
        System.out.println("+---------------------------+-------------+");

        portefeuille.getFonds().forEach((String nom, Fonds fonds) -> {
            System.out.println(String.format(
                    "| %25s | %9.2f € |",
                    nom, fonds.getAmount()
            ));
        });

        System.out.println("+---------------------------+-------------+");
    }

    public void afficherInstruments(Portefeuille portefeuille) {
        System.out.println("+---------------------------+-------+-------------+");
        System.out.println("|            Nom            | Fonds |    Somme    |");
        System.out.println("+---------------------------+-------+-------------+");

        portefeuille.getInstruments().forEach((String nom, Instrument instr) -> {
            double somme = 0;
            for (Fonds fonds : instr.getFonds()) {
                somme += fonds.getAmount();
            }

            System.out.println(String.format(
                    "| %25s | %5d | %9.2f € |",
                    nom, instr.getFonds().size(), somme
            ));
        });

        System.out.println("+---------------------------+-------+-------------+");
    }

    public void afficherPourcentagFonds(String fonds, Portefeuille portefeuille) {
        System.out.println("Pourcentage pour le fond " + fonds + " :");
        System.out.println("+---------------------------+-------------+");
        System.out.println("|            Nom            | Pourcentage |");
        System.out.println("+---------------------------+-------------+");

        portefeuille.getInstruments().forEach((String nom, Instrument instr) -> {
            System.out.println(String.format(
                    "| %25s | %9.2f %% |",
                    nom, instr.pourcentageFonds(fonds)
            ));
        });

        System.out.println("+---------------------------+-------------+");
    }
}
