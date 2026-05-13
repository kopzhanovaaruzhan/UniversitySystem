package models;
import java.io.Serializable;
public class Mark implements Serializable {
    private double firstAttestation;
    private double secondAttestation;
    private double finalExam;

    public Mark(double firstAttestation, double secondAttestation, double finalExam) {
        this.firstAttestation = firstAttestation;
        this.secondAttestation = secondAttestation;
        this.finalExam = finalExam;
    }

    public double getTotal() {
        return firstAttestation + secondAttestation + finalExam;
    }

    public String toString() {
        return "Mark: " + getTotal();
    }
}