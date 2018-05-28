import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Sources :
 *     JTable : https://baptiste-wicht.developpez.com/tutoriels/java/swing/jtable/
 *     JMenuBar + JPopupMenu : https://openclassrooms.com/courses/apprenez-a-programmer-en-java
 */
public class Fenetre extends JFrame {
    // Attributs
    private Portefeuille portefeuille = new Portefeuille();

    private FondsModel fondsModel;
    private TauxFondsModel tauxFondsModel;
    private InstrumentsModel instrumentsModel;

    private JPopupMenu popupFondsMenu;
    private JPopupMenu popupInstrumentsMenu;

    private JFileChooser fileChooser;
    private JTable tableauFonds;
    private JTable tableauTauxFonds;
    private JTable tableauInstruments;
    private JPanel pageInstruments;
    private JPanel pageFonds;
    private ChartPanel statsPanel;

    private boolean tauxFonds = false;

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
        setMinimumSize(getSize());
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

        tableauFonds.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (popupFondsMenu.isPopupTrigger(e)) {
                    popupFondsMenu.show(tableauFonds, e.getX(), e.getY());
                }
            }
        });

        tauxFondsModel = new TauxFondsModel();

        tableauTauxFonds = new JTable(tauxFondsModel);
        tableauTauxFonds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableauFonds.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                tauxFondsModel.setFond(fondsModel.getFond(tableauFonds.getSelectedRow()).getCle());
                if (!tauxFonds) {
                    pageFonds.add(new JScrollPane(tableauTauxFonds), BorderLayout.EAST);
                    Fenetre.this.pack();

                    tauxFonds = true;
                }
            }
        });

        // Boutons fond
        JPanel btns = new JPanel();
        btns.setLayout(new FlowLayout());
        new AjouterFondAction().ajouterPanel(btns);

        // Page Fonds
        pageFonds = new JPanel();
        pageFonds.setLayout(new BorderLayout());
        pageFonds.add(new JScrollPane(tableauFonds), BorderLayout.CENTER);
        pageFonds.add(btns, BorderLayout.SOUTH);

        tabs.add("Fonds", pageFonds);
    }
    private void initInstruments(JTabbedPane tabs) {
        // Préparation menu contextuel
        popupInstrumentsMenu = new JPopupMenu("");
        popupInstrumentsMenu.add(new SupprimerInstrumentAction());

        // Instruments
        instrumentsModel = new InstrumentsModel(portefeuille.getInstruments());

        tableauInstruments = new JTable(instrumentsModel);
        tableauInstruments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableauInstruments.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (popupInstrumentsMenu.isPopupTrigger(e)) {
                    popupInstrumentsMenu.show(tableauInstruments, e.getX(), e.getY());
                }
            }
        });

        tableauInstruments.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                Instrument instr = instrumentsModel.getInstrument(tableauInstruments.getSelectedRow());

                DefaultPieDataset dataset = new DefaultPieDataset();
                for (Fonds f : instr.getFonds()) {
                    dataset.setValue(f.getCle(), f.getAmount());
                }

                setGraphique(ChartFactory.createPieChart(
                        "Fonds", dataset
                ));
            }
        });

        // Boutons instruments
        JPanel btns = new JPanel();
        btns.setLayout(new FlowLayout());
        new AjouterInstrumentAction().ajouterPanel(btns);

        // Page Instruments
        pageInstruments = new JPanel();
        pageInstruments.setLayout(new BorderLayout());
        pageInstruments.add(new JScrollPane(tableauInstruments), BorderLayout.CENTER);
        pageInstruments.add(btns, BorderLayout.SOUTH);

        tabs.add("Instruments", pageInstruments);
    }

    private void setGraphique(JFreeChart chart) {
        if (statsPanel == null) {
            statsPanel = new ChartPanel(chart, true);
            pageInstruments.add(statsPanel, BorderLayout.EAST);

            pack();
        } else {
            statsPanel.setChart(chart);
        }
    }

    // Classes
    private class FondsModel extends AbstractTableModel {
        // Attributs
        private final Vector<Fonds> fonds = new Vector<>();
        private final String[] entetes = {"Nom", "Somme"};
        private LinkedList<JComboBox<String>> fondsCombobox = new LinkedList<>();

        // Constructeur
        public FondsModel(HashMap<String,Fonds> fonds) {
            setFonds(fonds);
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
                    return NumberFormat.getCurrencyInstance().format(fond.getAmount());
            }

            return null;
        }

        public void setFonds(HashMap<String,Fonds> fonds) {
            while (this.fonds.size() > 0) {
                supprimer(0);
            }

            fonds.forEach((String c, Fonds f) -> {
                ajouter(f);
            });
        }

        public void ajouter(Fonds f) {
            fonds.add(f);
            fireTableRowsInserted(fonds.size()-1, fonds.size()-1);

            for (JComboBox<String> comboBox : fondsCombobox) {
                comboBox.addItem(f.getCle());
            }
        }

        public Fonds getFond(int i) {
            return fonds.get(i);
        }

        public void supprimer(int i) {
            try {
                Fonds f = fonds.get(i);
                portefeuille.supprimerFonds(f.getCle());

                fonds.remove(i);
                fireTableRowsDeleted(i, i);

                for (JComboBox<String> comboBox : fondsCombobox) {
                    comboBox.removeItem(f.getCle());
                }
            } catch (FondsInexistant err) {
                err.printStackTrace();
            }
        }

        public void ajouterCombobox(JComboBox<String> combobox) {
            for (Fonds f : fonds) {
                combobox.addItem(f.getCle());
            }

            fondsCombobox.add(combobox);
        }
    }
    private class TauxFondsModel extends AbstractTableModel {
        // Attributs
        private String fond = null;
        private Vector<String> instruments = new Vector<>();
        private Vector<Double> taux = new Vector<>();
        private final String[] entetes = {"Instrument", "Taux"};

        // Méthodes
        @Override
        public int getRowCount() {
            return instruments.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            return entetes[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return instruments.get(rowIndex);

                case 1:
                    return String.format("%.2f %%", taux.get(rowIndex));
            }

            return null;
        }

        public void setFond(final String cleFonds) {
            this.fond = cleFonds;

            portefeuille.getInstruments().forEach((String cle, Instrument instr) -> {
                double tx = instr.pourcentageFonds(cleFonds);

                if (tx != 0) {
                    int i = instruments.indexOf(cle);

                    if (i == -1) {
                        instruments.add(cle);
                        taux.add(tx);

                        fireTableRowsInserted(instruments.size()-1, instruments.size()-1);
                    } else {
                        taux.set(i, tx);
                        fireTableCellUpdated(i, 1);
                    }
                } else {
                    int i = instruments.indexOf(cle);

                    if (i != -1) {
                        instruments.remove(i);
                        taux.remove(i);

                        fireTableRowsDeleted(i, i);
                    }
                }
            });
        }
    }
    private class InstrumentsModel extends AbstractTableModel {
        // Attributs
        private final Vector<Instrument> instruments = new Vector<>();
        private final String[] entetes = {"Nom", "Nombre de fonds", "Somme"};
        private LinkedList<JComboBox<String>> instrumentsCombobox = new LinkedList<>();

        // Constructeur
        public InstrumentsModel(HashMap<String,Instrument> instruments) {
            setInstruments(instruments);
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

                    return NumberFormat.getCurrencyInstance().format(somme);
            }

            return null;
        }

        public void setInstruments(HashMap<String,Instrument> instruments) {
            while (this.instruments.size() > 0) {
                supprimer(0);
            }

            instruments.forEach((String c, Instrument i) -> {
                ajouter(i);
            });
        }

        public void ajouter(Instrument instr) {
            instruments.add(instr);
            fireTableRowsInserted(instruments.size()-1, instruments.size()-1);

            for (JComboBox<String> comboBox : instrumentsCombobox) {
                comboBox.addItem(instr.getCle());
            }
        }

        public Instrument getInstrument(int i) {
            return instruments.get(i);
        }

        public void maj(String cle) {
            for (int i = 0; i < instruments.size(); ++i) {
                if (instruments.get(i).getCle().equals(cle)) {
                    fireTableCellUpdated(i, 1);
                    fireTableCellUpdated(i, 2);

                    if (tableauInstruments.getSelectedRow() == i) {
                        DefaultPieDataset dataset = new DefaultPieDataset();
                        for (Fonds f : instruments.get(i).getFonds()) {
                            dataset.setValue(f.getCle(), f.getAmount());
                        }

                        setGraphique(ChartFactory.createPieChart(
                                "Fonds", dataset
                        ));
                    }
                }
            }
        }

        public void supprimer(int i) {
            try {
                Instrument instr = instruments.get(i);
                portefeuille.supprimerInstrument(instr.getCle());

                instruments.remove(i);
                fireTableRowsDeleted(i, i);

                for (JComboBox<String> comboBox : instrumentsCombobox) {
                    comboBox.removeItem(instr.getCle());
                }
            } catch (InstrumentInexistant err) {
                err.printStackTrace();
            }
        }

        public void ajouterCombobox(JComboBox<String> combobox) {
            for (Instrument instr : instruments) {
                combobox.addItem(instr.getCle());
            }

            instrumentsCombobox.add(combobox);
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
                    Portefeuille tmp = Portefeuille.charger(fileChooser.getSelectedFile());

                    fondsModel.setFonds(tmp.getFonds());
                    instrumentsModel.setInstruments(tmp.getInstruments());

                    portefeuille = tmp;

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
                    File fichier = fileChooser.getSelectedFile();
                    if (!fichier.getPath().endsWith(".obj")) {
                        fichier.renameTo(new File(fichier.getPath() + ".obj"));
                    }

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
        private JFormattedTextField champSomme = new JFormattedTextField(DecimalFormat.getNumberInstance());
        private JButton btn;

        // Constructeur
        public AjouterFondAction() {
            super("Ajouter");

            card.setLayout(new FlowLayout(FlowLayout.LEFT));

            JPanel panel = new JPanel();
            panel.add(new JLabel("Nom :"));
            panel.add(champNom);
            card.add(panel);

            panel = new JPanel();
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
                        ((Number) champSomme.getValue()).doubleValue()
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

        public void ajouterPanel(JPanel panel) {
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

    private class AjouterInstrumentAction extends AbstractAction {
        // Attributs
        private JPanel card = new JPanel();
        private JComboBox<String> champNom = new JComboBox<>();
        private JComboBox<String> champFonds = new JComboBox<>();
        private JButton btn;

        // Constructeur
        public AjouterInstrumentAction() {
            super("Ajouter");

            // Init panel
            card.setLayout(new FlowLayout(FlowLayout.LEFT));

            JPanel panel = new JPanel();
            panel.add(new JLabel("Nom :"));
            instrumentsModel.ajouterCombobox(champNom);
            champNom.setEditable(true);
            panel.add(champNom);
            card.add(panel);

            panel = new JPanel();
            panel.add(new JLabel("Fond :"));
            fondsModel.ajouterCombobox(champFonds);
            panel.add(champFonds);
            card.add(panel);

            btn = new JButton(this);
            card.add(btn);
        }

        // Méthodes
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Instrument instr = portefeuille.ajouterFondInstrument(
                        (String) champNom.getSelectedItem(),
                        portefeuille.rechercheFonds((String) champFonds.getSelectedItem())
                );

                if (instr != null) {
                    instrumentsModel.ajouter(instr);
                } else {
                    instrumentsModel.maj((String) champNom.getSelectedItem());
                }
            } catch (FondsInexistant err) {
                err.printStackTrace();
            }
        }

        public void ajouterPanel(JPanel panel) {
            panel.add(card);
        }
    }
    private class SupprimerInstrumentAction extends AbstractAction {
        // Constructeur
        public SupprimerInstrumentAction() {
            super("Supprimer");
        }

        // Méthodes
        @Override
        public void actionPerformed(ActionEvent e) {
            int i = tableauInstruments.getSelectedRow();

            if (i != -1) {
                instrumentsModel.supprimer(i);
            }
        }
    }
}
