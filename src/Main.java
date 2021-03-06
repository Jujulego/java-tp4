import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Portefeuille portefeuille = new Portefeuille();
        Console console = new Console();

        // Scanner pour récupérer les entrées clavier
        Scanner scan = new Scanner(System.in);
        boolean test1 = false;
        double montant;
        int choix;
        String nom1;
        String nom2;

        System.out.println("On affiche les données pré-remplis pour pouvoir tester le code");
        console.afficherFonds(portefeuille);
        console.afficherInstruments(portefeuille);

        do {
            // menu interaction  avec utilisateur
            do {
                System.out.println("Choisissez l'action voulue :");
                System.out.println("    1 : chercher un fond et connaitre son montant");
                System.out.println("    2 : chercher un instrument et connaitre ses fonds associés");
                System.out.println("    3 : ajouter un nouvel Instrument et son fond associé");
                System.out.println("    4 : ajouter un Fond à un Instrument");
                System.out.println("    5 : supprimer un Fond"); //ne fonctionne pas
                System.out.println("    6 : supprimer un Instrument");
                System.out.println("    7 : afficher tous les Instruments"); // à faire
                System.out.println("    8 : afficher tous les Fonds"); // à faire
                System.out.println("    9 : afficher pourcentage que représente un fond dans chaque Instrument");
                System.out.println("   10 : charger un fichier");
                System.out.println("   11 : sauvegarder dans un fichier");
                System.out.println("   12 : le mode graphique !");
                System.out.println("    0 : quitter le programme");

                try {
                     choix = scan.nextInt();
                } catch(InputMismatchException e){
                    choix=-1;
                }

                if((choix >= 0) && (choix <= 12)) {
                    test1 = true;
                } else {
                    System.out.println("Erreur choix \n\n");
                }
            } while(!test1);

            // switch pour le choix
            switch (choix) {
                case 0:
                    System.out.println("Vous quittez le programme");
                    return;

                case 12:
                    Fenetre fenetre = new Fenetre(portefeuille);
                    fenetre.setVisible(true);
                    return;

                case 10:
                    //on vide le buffer
                    scan.nextLine();

                    while (true) {
                        File f = null;

                        do {
                            if (f != null) {
                                System.out.println("Ce fichier n'existe pas ou n'est pas un fichier !!!");
                            }

                            System.out.print("Entrez le nom du fichier (extension .obj) : ");
                            f = new File(scan.nextLine());
                            if (!f.getPath().endsWith(".obj")) {
                                f = new File(f.getPath() + ".obj");
                            }
                        } while (!f.exists() || !f.isFile());

                        // Chargement
                        try {
                            portefeuille = Portefeuille.charger(f);
                            break;
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("Erreur lors du chargement du fichier : " + e.getMessage());
                        }
                    }

                    break;

                case 11:
                    //on vide le buffer
                    scan.nextLine();

                    while (true) {
                        File f;

                        do {
                            System.out.print("Entrez le nom du fichier (extension .obj) : ");
                            f = new File(scan.nextLine());
                            if (!f.getPath().endsWith(".obj")) {
                                f = new File(f.getPath() + ".obj");
                            }

                            if (f.exists()) {
                                if (f.isFile()) {
                                    System.out.print("Le fichier existe déjà. ");
                                    String rep = "";

                                    // Confirmation
                                    do {
                                        System.out.println("Ecraser ? [O|N] ");
                                        rep = scan.nextLine();
                                    } while(!rep.equalsIgnoreCase("O") && !rep.equalsIgnoreCase("N"));

                                    if (rep.equalsIgnoreCase("O")) {
                                        f.delete();
                                    } else {
                                        f = null;
                                    }

                                } else {
                                    System.out.println("Ce n'est pas un fichier !");
                                    f = null;
                                }
                            }
                        } while (f == null);

                        // Sauvegarde
                        try {
                            f.createNewFile();
                            portefeuille.sauvegarder(f);
                            break;

                        } catch (IOException e) {
                            System.out.println("Erreur lors du chargement du fichier : " + e.getMessage());
                        }
                    }

                    break;

                case 1: //afficher fond
                    //on vide le buffer
                    scan.nextLine();

                    do {
                        //on demande le nom du fond
                        System.out.println("Entrez le nom du fond souhaité : \n");
                        nom1 = scan.nextLine();

                        //recherche du fond
                        try {
                            Fonds a = portefeuille.rechercheFonds(nom1);

                            System.out.println("Fond : " + nom1 + " contient : " + a.getAmount() + "€");
                            //System.out.println("Appuyez sur entrée pour continuer \n");

                            test1 = true;

                        } catch (FondsInexistant fondsInexistant) {
                            System.out.println("Ce fond n'existe pas");
                            test1 = false;
                            //System.out.println("Appuyez sur entrée pour une nouvelle selection de fond \n");
                        }
                    } while (!test1);

                    break;

                case 2: //afficher instrument et ses fonds
                    //on vide le buffer
                    scan.nextLine();

                    do {
                        //on demande le nom de l'instrument
                        System.out.println("Entrez le nom de l'instrument souhaité : \n");
                        nom1 = scan.nextLine();

                        //recherche de l'instrument
                        try {
                            ArrayList<Fonds> a = portefeuille.rechercheInstrument(nom1);

                            System.out.println("Instrument : " + nom1);
                            for (int i = 0; i < a.size(); i++) {
                                System.out.println("Fond : " + a.get(i).getCle() + " contient : " + a.get(i).getAmount() + "€");
                            }

                            System.out.println("Appuyez sur entrée pour continuer \n");

                            test1 = true;

                        } catch (InstrumentInexistant instrumentInexistant) {
                            System.out.println("Cet instrument n'existe pas");
                            test1 = false;
                            //System.out.println("Appuyez sur entrée pour une nouvelle selection d'instrument \n");
                        }
                    } while (!test1);

                    break;

                case 3: // ajouter un Instrument
                    //on vide le buffer
                    scan.nextLine();

                    do {
                        try { //on demande le nom de l'instrument
                            System.out.println("Entrez le nom de l'Instrument à ajouter : \n");
                            nom2 = scan.nextLine();

                            // on demande le nom du fond
                            System.out.println("Entrez le nom du fond à ajouter : \n");
                            nom1 = scan.nextLine();

                            //ajout de son montant
                            montant = -0f;
                            do {
                                System.out.println("Entrez le montant du fond : \n");
                                try {
                                    montant = scan.nextDouble();
                                } catch (InputMismatchException err) {
                                    System.out.println("Entrez un montant valide. Il s'agit d'un nombre séparé de sa");
                                    System.out.println("valeur décimale par une virgule ',' \n");
                                }
                            } while (montant < 0);

                            //création d'un fond contenant le montant
                            Fonds fonds = portefeuille.ajouterFonds(nom1, montant);

                            //on créé le nouvel Instrument avec son montant
                            portefeuille.ajouterFondInstrument(nom2, fonds);
                            test1 = true;

                        } catch (FondsExistant e) {
                            System.out.println("Fonds déjà existant !");
                            test1 = false;
                        }
                    } while (!test1);

                    break;

                case 4: //ajouter un fond
                    //on vide le buffer
                    scan.nextLine();

                    do {
                        //demander nom du fond
                        System.out.println("Entrez le nom du fond à ajouter : \n");

                        nom1 = scan.nextLine();

                        try {
                            //demander montant du fond
                            System.out.println("Entrez le montant du fond : \n");

                            montant = scan.nextDouble();
                            scan.nextLine();

                            // on demande à quel Instrument le lier
                            System.out.println("Entrez le nom de l'instrument auquel il appartient \n");
                            nom2 = scan.nextLine();

                            //test pour voir si l'instrument existe
                            HashMap<String, Instrument> instru = portefeuille.getInstruments();

                            if (instru.get(nom2) != null) {
                                //création du fond à utiliser
                                portefeuille.ajouterFonds(nom1, montant);

                                //ajout du fond dans l'instrument
                                portefeuille.ajouterFondInstrument(nom2, portefeuille.rechercheFonds(nom1));

                                test1 = true;
                            } else {
                                System.out.println("Cet Instrument n'existe pas \n");
                            }

                        } catch (Exception e) {
                            System.out.println("Entrez un montant valide. Il s'agit d'un nombre séparé de sa");
                            System.out.println("valeur décimale par une virgule ',' \n");
                            test1 = false;
                        }
                    } while (!test1);

                    break;

                case 5: // Supprimer un fond
                    // on vide le buffer
                    scan.nextLine();

                    do{
                        // demander nom du fond
                        System.out.println("Entrez le nom du fond à supprimer : \n");
                        nom1 = scan.nextLine();

                        try {
                            // on supprime le fond
                            portefeuille.supprimerFonds(nom1);
                            test1 = true;
                        } catch (FondsInexistant FondsInexistant) {
                            System.out.println("Ce fond n'existe pas");
                            test1 = false;
                        }

                    } while(!test1);

                    break;

                case 6: // Supprimer un Instrument
                    // on vide le buffer
                    scan.nextLine();

                    do {
                        //on demande le nom de l'instrument à supprimer
                        System.out.println("Entrez le nom de l'instrument à supprimer");
                        nom1 = scan.nextLine();

                        try {
                            //on supprime l'instrument
                            portefeuille.supprimerInstrument(nom1);
                            test1 = true;

                        } catch(InstrumentInexistant instrumentInexistant){
                            System.out.println("Cet Instrument n'existe pas \n");
                            test1 = false;
                        }
                    } while(!test1);

                    break;

                case 7: //afficher tous les Instruments
                    console.afficherInstruments(portefeuille);
                    break;

                case 8: // afficher tous les fonds
                    console.afficherFonds(portefeuille);
                    break;

                case 9: //afficher stat sur les fonds
                    //on vide le buffer
                    scan.nextLine();

                    do {
                        //on demande le fond que l'utilisateur veut afficher pour avoir ses stats
                        System.out.println("Pour quel fond voulez-vous savoir sa proportion pour les différents Instruments ?\n");
                        System.out.println("Entrez son nom :\n");

                        try{
                            nom1 = scan.nextLine();

                            console.afficherPourcentageFonds(nom1,portefeuille);
                            test1 = true;

                        } catch(Exception e) {
                            System.out.println("Ce fond n'existe pas\n");
                            test1 = false;
                        }
                    } while(!test1);

                    break;
            }


            //on ré-initialise la valeur choix pour ne pas boucler à l'infini sur le meme case
            choix = 1000;

            //affichage
            console.afficherFonds(portefeuille);
            console.afficherInstruments(portefeuille);
            //console.afficherPourcentageFonds("A", portefeuille);


        } while(true);
    }
}
