import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AirPlay {

    public static void send(String file, String hostname) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            os.write(Files.readAllBytes(Paths.get(file)));

            send("PUT", hostname, 7000, "/photo", os);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void send(String method, String hostname, Integer port, String uri, ByteArrayOutputStream os) throws IOException {
        URL url = new URL("http://"+hostname+":"+port+uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod(method);
        conn.setReadTimeout(5000);
        conn.setRequestProperty("Content-Length", "" + os.toByteArray().length);
        conn.connect();
        os.writeTo(conn.getOutputStream());
        os.flush();
        os.close();
        conn.getInputStream();
    }


}
