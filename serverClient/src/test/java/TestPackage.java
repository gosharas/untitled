import ru.gosha.serverClient.PackageToClient;
import ru.gosha.serverClient.PackageToServer;
import org.junit.Test;
import ru.gosha.serverClient.Seeker;
import ru.gosha.serverClient.SeekerType;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class TestPackage {

    @Test
    public void StartTestClient() throws ClassNotFoundException {
        PackageToClient a = PackageToClient.fromByteArray(new PackageToClient("My Testing из изи".getBytes(), 777, "Всё ок!").toByteArray());

        assertArrayEquals("My Testing из изи".getBytes(), a.CalFile);
        assertEquals(777, a.Count);
        assertEquals("Всё ок!", a.Messages);
    }

    @Test
    public void StartTestServer() throws ClassNotFoundException {
        PackageToServer a = PackageToServer.fromByteArray
                (
                        new PackageToServer
                                (
                                        new byte[][]{"My Testing из изи".getBytes(),
                                                "VERY".getBytes()},
                                        new Seeker
                                                (
                                                        "1",
                                                        SeekerType.Teacher,
                                                        LocalDate.MIN,
                                                        LocalDate.MAX,
                                                        0,
                                                        "МУУУУУУ"
                                                )
                                ).
                                toByteArray()
                );

        assertArrayEquals(new byte[][]{"My Testing из изи".getBytes(), "VERY".getBytes()},
                a.ExcelsFiles);
        assertEquals(new Seeker
                (
                        "1",
                        SeekerType.Teacher,
                        LocalDate.MIN,
                        LocalDate.MAX,
                        0,
                        "МУУУУУУ"
                ), a.QueryCriteria);
    }
}
