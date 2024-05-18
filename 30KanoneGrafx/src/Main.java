import at.wima.grafx.*;

import java.util.*;

import javafx.scene.paint.Color;

public class Main {

    public static void main(String[] args) throws GrafxException {

        String again;      // Neues Spiel
        int target;        // Entfernung des Ziels
        double dif;        // Abweichung vom Ziel
        double x, y;       // Aktuelle Position des Geschosses
        double dx, dy;     // Zuletzt zurückgelegter Weg/Höhe
        int anz;           // Anzahl der Schüsse
        double wGrd, vKmH; // Schusswinkel (Grad) und -geschwindigkeit (km/h)
        double w, v;       // Schusswinkel (Rad.) und -geschwindigkeit (m/s)

        final int RANGE = 10000; // Bereich für Zufallszahl: 1..RANGE
        final double G = 9.81;   // Erdbeschleunigung
        final int WINDOWHEIGHT = 500;
        final int WINDOWWIDTH = 1024;
        final int SCREENDIVIDE = 13; // Masstab 1:13

        Scanner sc = new Scanner(System.in);

        Grafx.createDrawing(WINDOWWIDTH, WINDOWHEIGHT, Color.BLACK);

        // Ein neues Spiel
        do {

            //Standort Spieler zeichnen
            Grafx.addLine(5, WINDOWHEIGHT-1, 5, WINDOWHEIGHT-20, Color.GREEN);

            // Ziel positionieren
            target = zufZa(RANGE) + 1;

            //Standort Ziel Zeichnen
            Grafx.addPolylinePoint(target / SCREENDIVIDE, WINDOWHEIGHT-1);
            Grafx.addPolylinePoint(target / SCREENDIVIDE, WINDOWHEIGHT-20);
            Grafx.addPolylinePoint(target / SCREENDIVIDE+5, WINDOWHEIGHT-15);
            Grafx.addPolylinePoint(target / SCREENDIVIDE, WINDOWHEIGHT-10, Color.RED);
            Grafx.refresh();

            // Ziel ausgeben
            System.out.println("Das Ziel befindet sich in " + target + " m Entfernung!");

            anz = 0;

            do {
                // Nächsten Schuss abfeuern lassen
                // - Mitzählen
                anz = anz + 1;

                // - Winkel
                // -- Erste Eingabe
                System.out.print("Schusswinkel (Grad):          ");
                wGrd = sc.nextDouble();

                // -- Eingabeprüfung
                while ((wGrd <= 0) || (wGrd >= 90)) {
                    System.out.println("Fehler: Winkel muss zwischen 0 und 90 Grad betragen!");
                    System.out.print("Schusswinkel (Grad):          ");
                    wGrd = sc.nextDouble();
                }

                // -- Auf Radiant umrechnen
                w = Math.toRadians(wGrd);

                // - Geschwindigkleit
                // -- Erste Eingabe
                System.out.print("Schussgeschwindigkeit (km/h): ");
                vKmH = sc.nextDouble();

                // -- Eingabeprüfung
                while ((vKmH <= 300) || (vKmH >= 999)) {
                    System.out.println("Fehler: Geschwindigkeit muss zwischen 300 und 999 km/h betragen!");
                    System.out.print("Schussgeschwindigkeit (km/h): ");
                    vKmH = sc.nextDouble();
                }

                // -- Auf m/s umrechnen
                v = vKmH * 1000 / 3600;

                // Flugbahn berechnen
                x = 0;
                y = 0.1;

                while (y > 0) {
                    // - Weg in der letzten Sekunde
                    dx = v * Math.cos(w);
                    dy = v * Math.sin(w) - G / 2;

                    // - Aktuelle Position
                    x = x + dx;
                    y = y + dy;

                    // - Aktuelle Wurfgeschwindigkeit und -winkel ermitteln
                    v = Math.pow(Math.pow(dx, 2) + Math.pow(dy, 2), 0.5);
                    w = Math.atan(dy / dx);


                    //Kurve des Schusses zeichnen
                    if (y > 0 && y < WINDOWHEIGHT * SCREENDIVIDE && x < WINDOWWIDTH * SCREENDIVIDE) {
                        Grafx.addPoint((int) x / SCREENDIVIDE, 499 - (int) y / SCREENDIVIDE, Color.WHITE);
                        Grafx.refresh();
                    }


                    //System.out.printf("************ %7.2f / %7.2f  / %7.2f  / %7.2f  / %7.2f  / %7.2f \n", x, dx, y, dy, v, Math.toDegrees(w)); // nur zum Testen
                } // while Y > 0

                // Abweichung ausgeben
                dif = target - x;

                System.out.printf("Du hast das Ziel um %.2f m verfehlt", Math.abs(dif));
                if (dif < 0) {
                    System.out.println(" (zu lang)!");
                } else {
                    System.out.println(" (zu kurz)!");
                }
                System.out.println("");

                // Solange Entfernung vom Ziel zu weit weg
            }
            while (Math.abs(dif) > 50);

            // Anzahl der benötigten Schüsse ausgeben
            System.out.println("! ! ! G E T R O F F E N ! ! ! Du hast dafür " + anz + " Schüsse benötigt!");

            Grafx.addText("treffer", "Getroffen!!!", 30, WINDOWWIDTH/2-80, WINDOWHEIGHT/2, Color.PURPLE);;
            Grafx.refresh();

            // Noch ein Spiel?
            System.out.println();
            System.out.println("Auf ein Neues? (J/N)");
            again = sc.next();
            if (again.equalsIgnoreCase("j")){
                Grafx.clear();
            }
        }
        while (again.equalsIgnoreCase("J"));

        Grafx.close();
    } // main


    //********************************************************************
    // ZufZa(Limit) liefert eine Zufallszahl im Bereich von 0 bis Limit-1;
    // wie das genau funktioniert ist jetzt vorerst einmal nicht wichtig!
    public static int zufZa(int limit) {
        return (int) (Math.random() * (double) limit);
    }
    //********************************************************************
}
