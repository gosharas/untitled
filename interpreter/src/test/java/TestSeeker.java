
import ru.gosha.interpreter.*;

import java.time.LocalDate;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestSeeker {

    @Test
    public void startTestSeeker() {

        System.out.println("Test Seeker start.");

        Seeker test = new Seeker("name", SeekerType.Teacher, LocalDate.of(2000, 5, 5), LocalDate.of(2000, 5, 10), +3 * 60 * 60, "Москва, проспект Вернадского, 78, РТУ МИРЭА");

        assertEquals("name", test.nameOfSeeker);
        assertEquals(SeekerType.Teacher, test.seekerType);
        assertEquals(LocalDate.of(2000, 5, 5), test.dateStart);
        assertEquals(LocalDate.of(2000, 5, 10), test.dateFinish);
        assertEquals(10800, test.timezone);
        assertEquals("Москва, проспект Вернадского, 78, РТУ МИРЭА", test.defaultAddress);

        PackageToClient cl = new PackageToClient(new byte[]{0, 0}, 0, "Всё ок");
        assertArrayEquals(new byte[]{0, 0}, cl.CalFile);
        assertEquals(0, cl.Count);
        assertEquals("Всё ок", cl.Messages);

        PackageToServer sv = new PackageToServer(new byte[][] {{0, 0}}, test);

        assertEquals(test, sv.QueryCriteria);
        assertArrayEquals(new byte[][] {{0, 0}}, sv.ExcelsFiles);
    }


}