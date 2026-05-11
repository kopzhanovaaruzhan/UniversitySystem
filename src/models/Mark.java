package models;

import java.io.Serializable;

public class Mark implements Serializable {
    private static final long serialVersionUID = 1L;

    private double att1;
    private double att2;
    private double finalExam;

    public Mark() {}

    public Mark(double att1, double att2, double finalExam) {
        this.att1 = att1;
        this.att2 = att2;
        this.finalExam = finalExam;
    }

    public double getTotal() {
        return att1 + att2 + finalExam;
    }

    public double convertToGpa() {
        double total = getTotal();
        if (total >= 95) return 4.0;
        if (total >= 90) return 3.67;
        if (total >= 85) return 3.33;
        if (total >= 80) return 3.0;
        if (total >= 75) return 2.67;
        if (total >= 70) return 2.33;
        if (total >= 65) return 2.0;
        if (total >= 60) return 1.67;
        if (total >= 55) return 1.33;
        if (total >= 50) return 1.0;
        return 0.0;
    }

    /** Сдан ли курс (итог >= 50) */
    public boolean isPassing() {
        return getTotal() >= 50;
    }

    // Геттеры и сеттеры
    public double getAtt1()              { return att1; }
    public void   setAtt1(double att1)   { this.att1 = att1; }
    public double getAtt2()              { return att2; }
    public void   setAtt2(double att2)   { this.att2 = att2; }
    public double getFinalExam()             { return finalExam; }
    public void   setFinalExam(double f)     { this.finalExam = f; }

    @Override
    public String toString() {
        return String.format("Att1=%.1f | Att2=%.1f | Final=%.1f | Total=%.1f | GPA=%.2f",
                att1, att2, finalExam, getTotal(), convertToGpa());
    }
}
