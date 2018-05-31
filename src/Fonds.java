import java.io.Serializable;

public class Fonds implements Comparable<Fonds>, Serializable {
    // Attributs
    private String cle;
    private double amount;

    // Constructeur
    public Fonds(String cle, double amount) {
        this.cle = cle;
        this.amount = amount;
    }

    // Méthodes
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Fonds) {
            return cle.equals(((Fonds) obj).cle);
        }

        return super.equals(obj);
    }

    @Override
    public int compareTo(Fonds o) {
        return (int) (this.amount - o.amount);
    }

    // - accesseurs
    public double getAmount() {
        return amount;
    }
    public String getCle() {
        return cle;
    }
}
