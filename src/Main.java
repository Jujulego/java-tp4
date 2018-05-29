import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Iterator;

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

        //Scanner pour récupérer les entrées clavie
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

        //menu interaction  avec utilisateur
        do {
            System.out.println("Choisissez l'action voulue :");
            System.out.println("entrez 1 pour chercher un fond et connaitre son montant");
            System.out.println("entrez 2 pour chercher un instrument et connaitre ses fonds associés");
            System.out.println("entrez 3 pour ajouter un nouvel Instrument et son fond associé");
            System.out.println("entrez 4 pour ajouter un Fond à un Instrument");
            System.out.println("entrez 5 pour supprimer un Fond"); //ne fonctionne pas
            System.out.println("entrez 6 pour supprimer un Instrument");
            System.out.println("entrez 7 pour afficher tous les Instruments"); // à faire
            System.out.println("entrez 8 pour afficher tous les Fonds"); // à faire
            System.out.println("entrez 9 pour afficher pourcentage que représente un fond dans chaque Instrument");
            System.out.println("entrez 0 pour quitter le programme");

            try{
                 choix = scan.nextInt();

            }catch(Exception e){
                choix=-1;
            }


            if((choix >= 0) && (choix <= 10))
            {
                test1 = true;
            }else{
                System.out.println("Erreur choix \n\n");
            }

            }while(test1 == false);





            //switch pour le choix
            switch (choix) {
                case 0:
                    System.out.println("Vous quittez le programme");
                    return;

                case 1: //afficher fond

                    test1 = false;

                    //on vide le buffer
                    scan.nextLine();
                    do {

                        //on demande le nom du fond
                        System.out.println("Entrez le nom du fond souhaité : \n");

                        try {

                            nom1 = scan.nextLine();

                            //recherche du fond
                            try {
                                Fonds a = portefeuille.rechercheFonds(nom1);


                               System.out.println("Fond : " + nom1 + " contient : " + a.getAmount() + "€");
                                //System.out.println("Appuyez sur entrée pour continuer \n");

                                test1 = true;

                            } catch (FondsInexistant fondsInexistant) {
                                System.out.println("Ce fond n'existe pas");
                                //System.out.println("Appuyez sur entrée pour une nouvelle selection de fond \n");
                            }

                        } catch (Exception e) {
                            nom1 = "zz";

                        }

                        if (nom1 == "zz") {
                            System.out.println("Entrez un nom valide ! \n\n");
                            nom1 = null;
                        }

                        // scan.nextLine();
                    } while (test1 == false);
                    break;

                case 2: //afficher instrument et ses fonds
                    test1 = false;
                    //on vide le buffer
                    scan.nextLine();

                    do {
                        //on demande le nom de l'instrument
                        System.out.println("Entrez le nom de l'instrument souhaité : \n");

                        try {

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
                                //System.out.println("Appuyez sur entrée pour une nouvelle selection d'instrument \n");
                            }

                        } catch (Exception e) {
                            nom1 = "zz";

                        }

                        if (nom1 == "zz") {
                            System.out.println("Entrez un nom valide ! \n\n");
                            nom1 = null;
                        }

                    } while (test1 == false);

                    break;

                case 3: //ajouter un Instrument
                    test1 = false;
                    //on vide le buffer
                    scan.nextLine();

                    do {

                        try {    //on demande le nom de l'instrument
                            System.out.println("Entrez le nom de l'Instrument à ajouter : \n");

                            nom2 = scan.nextLine();


                            //on vide le buffer
                            //scan.nextLine();

                            //on demande le nom du fond
                            System.out.println("Entrez le nom du fond à ajouter : \n");

                            try {


                                nom1 = scan.nextLine();

                                //ajout de son montant
                                try {
                                    //on vide le buffer
                                    //scan.nextLine();
                                    System.out.println("Entrez le montant du fond : \n");

                                    montant = scan.nextDouble();

                                    try {

                                        //création d'un fond contenant le montant
                                        portefeuille.ajouterFonds(nom1, montant);

                                        //on créé le nouvel Instrument avec son montant
                                        portefeuille.ajouterFondInstrument(nom2, portefeuille.rechercheFonds(nom1));

                                        test1 = true;

                                    } catch (Exception e) {

                                    }

                                } catch (Exception e) {
                                    System.out.println("Entrez un montant valide. Il s'agit d'un nombre séparé de sa");
                                    System.out.println("valeur décimale par une virgule ',' \n");
                                }


                            } catch (Exception e) {
                                System.out.println("Erreur nom fond \n");
                            }

                        } catch (Exception e) {
                            System.out.println("Erreur nom Instrument \n");
                        }

                        //scan.nextLine();
                    } while (test1 == false);

                    break;

                case 4: //ajouter un fond
                    test1 = false;
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

                            try {
                                scan.nextLine();
                                //on demande à quel Instrument le lier
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

                            }

                        } catch (Exception e) {
                            System.out.println("Entrez un montant valide. Il s'agit d'un nombre séparé de sa");
                            System.out.println("valeur décimale par une virgule ',' \n");
                        }


                    } while (test1 == false);


                    break;

                case 5: //supprimer un fond
                  test1 = false;
                    //on vide le buffer
                    scan.nextLine();

                    do{
                        //demander nom du fond
                        System.out.println("Entrez le nom du fond à supprimer : \n");

                        nom1 = scan.nextLine();


                        //test pour voir si le fond existe
                        HashMap<String, Fonds> fond = portefeuille.getFonds();


                        try{

                            try {

                                if (fond.get(nom1) != null) {


                                    portefeuille.supprimerFonds(nom1);


                                }

                                //on supprime le fond des Instruments
                                portefeuille.getFonds().remove(nom1);



                                test1 = true;
                            } catch (FondsInexistant FondsInexistant) {
                                System.out.println("Ce fond n'existe pas");
                            }
                        } catch (Exception e){

                        }

                    }while(test1 == false);


                break;

                case 6: //Supprimer un Instrument
                    test1 = false;
                    //on vide le buffer
                    scan.nextLine();

                    do{

                        //on demande le nom de l'instrument à supprimer
                        System.out.println("Entrez le nom de l'instrument à supprimer");

                        nom1 = scan.nextLine();

                        try{

                            HashMap<String, Instrument> map =  portefeuille.getInstruments();

                            if (map.get(nom1) != null) {

                                //on vide les fonds qu'il contient
                                map.get(nom1).viderFonds();

                                //on supprime l'instrument
                                portefeuille.supprimerInstrument(nom1);

                                test1 = true;
                            } else {
                                System.out.println("Cet Instrument n'existe pas \n");
                            }

                        }catch(InstrumentInexistant instrumentInexistant){

                        }

                    }while(test1 == false);
                break;

                case 7: //afficher tous les Instruments

                    console.afficherInstruments(portefeuille);

                break;

                case 8: // afficher tous les fonds

                    console.afficherFonds(portefeuille);

                break;
                case 9: //afficher stat sur les fonds
                    test1 = false;
                    //on vide le buffer
                    scan.nextLine();

                    do{

                        //on demande le fond que l'utilisateur veut afficher pour avoir ses stats
                        System.out.println("Pour quel fond voulez-vous savoir sa proportion pour les différents Instruments ?\n");
                        System.out.println("Entrez son nom :\n");



                        try{
                            nom1 = scan.nextLine();

                            console.afficherPourcentagFonds(nom1,portefeuille);
                            test1 = true;

                        }catch(Exception e)
                        {
                            System.out.println("Ce fond n'existe pas\n");
                        }

                    }while(test1 == false);

                break;
            }


            //on ré-initialise la valeur choix pour ne pas boucler à l'infini sur le meme case
            choix = 1000;

            //affichage
            console.afficherFonds(portefeuille);
            console.afficherInstruments(portefeuille);
            //console.afficherPourcentagFonds("A", portefeuille);


        }while(choix != 0);


    }


}
