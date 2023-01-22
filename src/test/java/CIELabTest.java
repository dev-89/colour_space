import org.junit.Test;
import static org.junit.Assert.*;
import org.cie.CIELab;

public class CIELabTest {
    @Test
    public void testConstructor() {
        CIELab color = new CIELab(255, 99, 71);
        assertEquals(62.2, color.getL(), 0.1);
        assertEquals(57.8, color.getA(), 0.1);
        assertEquals(46.4, color.getB(), 0.1);
    }

    @Test
    public void testCIEDE2000() {
        CIELab color1 = new CIELab(255, 99, 71);
        CIELab color2 = new CIELab(255, 99, 71);
        double deltaE = color1.CIEDE2000(color2);
        assertEquals(0.0, deltaE, 0.1);

        color2 = new CIELab(50, 50, 50);
        deltaE = color1.CIEDE2000(color2);
        assertEquals(46.4, deltaE, 0.1);

        color2 = new CIELab(100, 100, 100);
        deltaE = color1.CIEDE2000(color2);
        assertEquals(33.9, deltaE, 0.1);

        color2 = new CIELab(200, 200, 200);
        deltaE = color1.CIEDE2000(color2);
        assertEquals(31.1, deltaE, 0.1);
    }

    @Test
    public void testCIE94Graphic() {
        CIELab color1 = new CIELab(255, 120, 0);
        CIELab color2 = new CIELab(60, 60, 60);
        double deltaE = color1.CIE94Graphic(color2);
        assertEquals(43.9, deltaE, 0.1);

        color2 = new CIELab(0, 0, 0);
        deltaE = color1.CIE94Graphic(color2);
        assertEquals(67.8, deltaE, 0.1);

        color2 = new CIELab(8, 215, 99);
        deltaE = color1.CIE94Graphic(color2);
        assertEquals(52.5, deltaE, 0.1);

        color2 = new CIELab(140, 10, 240);
        deltaE = color1.CIE94Graphic(color2);
        assertEquals(73.0, deltaE, 0.1);
    }

    @Test
    public void testCIE94Textile() {
        CIELab color1 = new CIELab(255, 120, 0);
        CIELab color2 = new CIELab(60, 60, 60);
        double deltaE = color1.CIE94Textile(color2);
        assertEquals(26.1, deltaE, 0.1);

        color2 = new CIELab(0, 0, 0);
        deltaE = color1.CIE94Textile(color2);
        assertEquals(36.8, deltaE, 0.1);

        color2 = new CIELab(8, 215, 99);
        deltaE = color1.CIE94Textile(color2);
        assertEquals(53.7, deltaE, 0.1);

        color2 = new CIELab(140, 10, 240);
        deltaE = color1.CIE94Textile(color2);
        assertEquals(72.5, deltaE, 0.1);
    }
}
