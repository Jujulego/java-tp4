import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Vector;

/**
 * Sources :
 *     JTable : https://baptiste-wicht.developpez.com/tutoriels/java/swing/jtable/
 *     JMenuBar + JPopupMenu : https://openclassrooms.com/courses/apprenez-a-programmer-en-java/les-menus-et-boites-de-dialogue
 */
public class Fenetre extends JFrame {
    // Attributs
    private Portefeuille portefeuille = new Portefeuille();
    private FondsModel fondsModel;
    private JPopupMenu popupFondsMenu;

    private JFileChooser fileChooser;
    private JTable tableauFonds;
    private JTable tableauInstruments;

    // Constructeur
    public Fenetre() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Portefeuille");

        // Boite de dialogue
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getPath().endsWith(".obj");
            }

            @Override
            public String getDescription() {
                return "Portefeuille (.obj)";
            }
        });

        // Menus
        initMenu();

        // Onglets
        JTabbedPane tabs = new JTabbedPane();
        initFonds(tabs);
        initInstruments(tabs);

        // Ajout !
        Container containter = getContentPane();
        containter.add(tabs, BorderLayout.CENTER);

        pack();
    }

    // Méthodes
    private void initMenu() {
        // Actions de chargement / sauvegarde
        JMenuItem menuEnregistrer = new JMenuItem(new EnregistrerAction());
        menuEnregistrer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));

        JMenu menuFichier = new JMenu("Fichier");
        menuFichier.add(new ChargerAction());
        menuFichier.add(menuEnregistrer);
        menuFichier.setMnemonic('F');

        JMenuBar barreMenu = new JMenuBar();
        barreMenu.add(menuFichier);
        setJMenuBar(barreMenu);
    }
    private void initFonds(JTabbedPane tabs) {
        // Préparation menu contextuel
        popupFondsMenu = new JPopupMenu("");
        popupFondsMenu.add(new SupprimerFondAction());

        // Fonds
        fondsModel = new FondsModel(portefeuille.getFonds());

        tableauFonds = new JTable(fondsModel);
        tableauFonds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Boutons fond
        JPanel boutonsFonds = new JPanel();
        boutonsFonds.setLayout(new FlowLayout());
        new AjouterFondAction().addCard(boutonsFonds);

        // Page Fonds
        JPanel pageFonds = new JPanel();
        pageFonds.setLayout(new BorderLayout());
        pageFonds.add(new JScrollPane(tableauFonds), BorderLayout.CENTER);
        pageFonds.add(boutonsFonds, BorderLayout.SOUTH);

        tableauFonds.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (popupFondsMenu.isPopupTrigger(e)) {
                    popupFondsMenu.show(e.getComponent(), e.getX(), e.getY() + tableauFonds.getY());
                }
            }
        });

        tabs.add("Fonds", pageFonds);
    }
    private void initInstruments(JTabbedPane tabs) {
        // Instruments
        tableauInstruments = new JTable(new InstrumentsModel(portefeuille.getInstruments()));
        tableauInstruments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Page Instruments
        JPanel pageInstruments = new JPanel();
        pageInstruments.setLayout(new BorderLayout());
        pageInstruments.add(new JScrollPane(tableauInstruments), BorderLayout.CENTER);

        tabs.add("Instruments", pageInstruments);
    }

    // Classes
    private class FondsModel extends AbstractTableModel {
        // Attributs
        private final Vector<Fonds> fonds = new Vector<>();
        private final String[] entetes = {"Nom", "Somme"};

        // Constructeur
        public FondsModel(HashMap<String,Fonds> fonds) {
            fonds.forEach((String c, Fonds f) -> {
                this.fonds.add(f);
            });
        }

        // Méthodes
        @Override
        public int getRowCount() {
            return fonds.size();
        }

        @Override
        public int getColumnCount() {
            return entetes.length;
        }

        @Override
        public String getColumnName(int column) {
            return entetes[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Fonds fond = fonds.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return fond.getCle();

                case 1:
                    return String.format("%.2f €", fond.getAmount());
            }

            return null;
        }

        public void ajouter(Fonds f) {
            fonds.add(f);
            fireTableRowsInserted(fonds.size()-1, fonds.size()-1);
        }

        public void supprimer(int i) {
            try {
                Fonds f = fonds.remove(i);
                fireTableRowsDeleted(i, i);

                portefeuille.supprimerFonds(f.getCle());
            } catch (FondsInexistant fondsInexistant) {
                fondsInexistant.printStackTrace();
            }
        }
    }
    private class InstrumentsModel extends AbstractTableModel {
        // Attributs
        private final Vector<Instrument> instruments = new Vector<>();
        private final String[] entetes = {"Nom", "Nombre de fonds", "Somme"};

        // Constructeur
        public InstrumentsModel(HashMap<String,Instrument> instruments) {
            instruments.forEach((String c, Instrument i) -> {
                this.instruments.add(i);
            });
        }

        // Méthodes
        @Override
        public int getRowCount() {
            return instruments.size();
        }

        @Override
        public int getColumnCount() {
            return entetes.length;
        }

        @Override
        public String getColumnName(int column) {
            return entetes[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Instrument instrument = instruments.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return instrument.getCle();

                case 1:
                    return instrument.getFonds().size();

                case 2:
                    double somme = 0;
                    for (Fonds fonds : instrument.getFonds()) {
                        somme += fonds.getAmount();
                    }

                    return String.format("%.2f €", somme);
            }

            return null;
        }
    }

    private class ChargerAction extends AbstractAction {
        // Constructeur
        public ChargerAction() {
            super("Charger");
        }

        // Méthodes
        @Override
        public void actionPerformed(ActionEvent e) {
            if (fileChooser.showOpenDialog(Fenetre.this) == JFileChooser.APPROVE_OPTION) {
                try {
                    portefeuille = Portefeuille.charger(fileChooser.getSelectedFile());

                    tableauFonds.setModel(new FondsModel(portefeuille.getFonds()));
                    tableauInstruments.setModel(new InstrumentsModel(portefeuille.getInstruments()));

                } catch (IOException | ClassNotFoundException e1) {
                    JOptionPane.showMessageDialog(Fenetre.this,
                            "Erreur lors du chargement :\n" + e1.getLocalizedMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }
    private class EnregistrerAction extends AbstractAction {
        // Constructeur
        public EnregistrerAction() {
            super("Enregistrer");
        }

        // Méthodes
        @Override
        public void actionPerformed(ActionEvent e) {
            if (fileChooser.showSaveDialog(Fenetre.this) == JFileChooser.APPROVE_OPTION) {
                try {
                    portefeuille.sauvegarder(fileChooser.getSelectedFile());

                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(Fenetre.this,
                            "Erreur lors de l'enregistrement :\n" + e1.getLocalizedMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }

    private class AjouterFondAction extends AbstractAction {
        // Attributs
        private JPanel card = new JPanel();
        private JTextField champNom = new JTextField(15);
        private JFormattedTextField champSomme = new JFormattedTextField(NumberFormat.getNumberInstance());
        private JButton btn;

        // Constructeur
        public AjouterFondAction() {
            super("Ajouter");

            card.setLayout(new FlowLayout(FlowLayout.LEFT));

            JPanel panel = new JPanel();
            panel.add(new JLabel("Nom :"));
            panel.add(champNom);
            card.add(panel);

            panel.add(new JLabel("Somme :"));
            panel.add(champSomme);
            champSomme.setColumns(10);
            card.add(panel);

            btn = new JButton(this);
            card.add(btn);
        }

        // Méthodes
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Fonds f = portefeuille.ajouterFonds(
                        champNom.getText(),
                        Double.parseDouble(champSomme.getText())
                );

                fondsModel.ajouter(f);
            } catch (FondsExistant fondsExistant) {
                JOptionPane.showMessageDialog(
                        Fenetre.this,
                        "Fond déjà existant !",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        public void addCard(JPanel panel) {
            panel.add(card);
        }
    }
    private class SupprimerFondAction extends AbstractAction {
        // Constructeur
        public SupprimerFondAction() {
            super("Supprimer");
        }

        // Méthodes
        @Override
        public void actionPerformed(ActionEvent e) {
            int i = tableauFonds.getSelectedRow();

            if (i != -1) {
                fondsModel.supprimer(i);
            }
        }
    }
}
