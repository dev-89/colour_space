package org.cie;

public class CIELab {

    private static final double kC = 1.0;
    private static final double kH = 1.0;
    final private double l;
    final private double a;
    final private double b;

    private static final double[] D65 = { 0.95047, 1.0, 1.08883 };

    public CIELab(int red, int green, int blue) {
        double[] xyz = rgbToXYZ(red, green, blue);
        double[] lab = xyzToLab(xyz, D65);
        this.l = lab[0];
        this.a = lab[1];
        this.b = lab[2];
    }

    public double getL() {
        return l;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    private static double[] rgbToXYZ(int red, int green, int blue) {
        double r = red / 255.0;
        double g = green / 255.0;
        double b = blue / 255.0;

        r = r > 0.04045 ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92;
        g = g > 0.04045 ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92;
        b = b > 0.04045 ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92;

        double x = r * 0.4124 + g * 0.3576 + b * 0.1805;
        double y = r * 0.2126 + g * 0.7152 + b * 0.0722;
        double z = r * 0.0193 + g * 0.1192 + b * 0.9505;

        return new double[] { x, y, z };
    }

    public static double[] xyzToLab(double[] xyz, double[] whitePoint) {
        double x = xyz[0] / whitePoint[0];
        double y = xyz[1] / whitePoint[1];
        double z = xyz[2] / whitePoint[2];

        x = x > 0.008856 ? Math.pow(x, 1.0 / 3.0) : (7.787 * x) + (16.0 / 116.0);
        y = y > 0.008856 ? Math.pow(y, 1.0 / 3.0) : (7.787 * y) + (16.0 / 116.0);
        z = z > 0.008856 ? Math.pow(z, 1.0 / 3.0) : (7.787 * z) + (16.0 / 116.0);

        double l = (116.0 * y) - 16.0;
        double a = 500.0 * (x - y);
        double b = 200.0 * (y - z);

        return new double[] { l, a, b };
    }

    public double CIEDE2000(CIELab other) {
        double L1 = this.l;
        double a1 = this.a;
        double b1 = this.b;
        double L2 = other.l;
        double a2 = other.a;
        double b2 = other.b;

        double C1 = Math.sqrt(a1 * a1 + b1 * b1);
        double C2 = Math.sqrt(a2 * a2 + b2 * b2);
        double deltaC = C2 - C1;

        double deltaL = L2 - L1;
        double deltaA = a2 - a1;
        double deltaB = b2 - b1;

        double meanC = (C1 + C2) / 2;
        double meanL = (L1 + L2) / 2;

        double weightingFactorKl = 1;
        double weightingFactorKc = 1;
        double weightingFactorKh = 1;

        double deltaH = Math.sqrt(deltaA * deltaA + deltaB * deltaB - deltaC * deltaC);

        double meanC7_25 = Math.pow(meanC, 7) / 25;

        double T = 164 <= meanC ? Math.sqrt(meanC7_25 / (meanC7_25 + 6103515625.0)) : 0.0;

        double S_L = 1 + (0.015 * Math.pow(meanL - 50, 2)) / Math.sqrt(20 + Math.pow(meanL - 50, 2));
        double S_C = 1 + 0.045 * meanC;
        double S_H = 1 + 0.015 * meanC * T;

        double R_T = -2 * Math.sqrt(Math.pow(meanC, 7) / (Math.pow(meanC, 7) + 6103515625.0))
                * Math.sin(Math.toRadians(60.0 * Math.exp(-1 * Math.pow((meanC - 275) / 25, 2))));

        return Math.sqrt(Math.pow(deltaL / (weightingFactorKl * S_L), 2)
                + Math.pow(deltaC / (weightingFactorKc * S_C), 2)
                + Math.pow(deltaH / (weightingFactorKh * S_H), 2)
                + R_T * (deltaC / (weightingFactorKc * S_C)) * (deltaH / (weightingFactorKh * S_H)));
    }

    public double CIE94Graphic(CIELab other) {
        return this.CIE94(other, 1, 0.045, 0.015);
    }

    public double CIE94Textile(CIELab other) {
        return this.CIE94(other, 2, 0.048, 0.014);
    }

    public double CIE94(CIELab other, double kL, double K1, double K2) {
        double L1 = this.l;
        double a1 = this.a;
        double b1 = this.b;
        double L2 = other.l;
        double a2 = other.a;
        double b2 = other.b;

        double deltaL = L1 - L2;
        double deltaA = a1 - a2;
        double deltaB = b1 - b2;

        double C1 = Math.sqrt(a1 * a1 + b1 * b1);
        double C2 = Math.sqrt(a2 * a2 + b2 * b2);
        double deltaC = C1 - C2;

        double deltaH = 0.0;
        if (deltaA * deltaA + deltaB * deltaB > deltaC * deltaC) {
            deltaH = Math.sqrt(deltaA * deltaA + deltaB * deltaB - deltaC * deltaC);
        }

        double SL = 1;
        double SC = 1 + K1 * C1;
        double SH = 1 + K2 * C1;

        double deltaE = Math.sqrt(
                (deltaL / (kL * SL)) * (deltaL / (kL * SL)) +
                        (deltaC / (kC * SC)) * (deltaC / (kC * SC)) +
                        (deltaH / (kH * SH)) * (deltaH / (kH * SH))
        );

        return deltaE;
    }
}
