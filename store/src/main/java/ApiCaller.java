import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import com.google.gson.Gson;


public class ApiCaller {
    public static void ApiSearch (String query) throws IOException { //esto generará un json que leeremos
    String cleanQuery = query.trim();
    String Base_Url1half = "https://apps.microsoft.com/api/products/search?query=";
    String Base_Url2half = "&mediaType=all&age=all&price=all&category=all&subscription=all&cursor=&gl=ES&hl=en-ES&exp=1811522136";

    if (cleanQuery.endsWith("\n")) {
    cleanQuery = StringUtils.chop(cleanQuery); //quita el caracter de enter si existe
    }

    cleanQuery = cleanQuery.replace(" ", "%20"); //lo que se usa en vez de espacios en URLs

    String Final_Url = (Base_Url1half.trim() + cleanQuery + Base_Url2half.trim()).trim();

    try { //Intentamos obtener la URL
    Document log = Jsoup.connect(Final_Url).get();
    String jsonText = log.body().text();
    Gson gson = new Gson();


    } catch (IOException e) {
        System.err.println("Error loading URL...");
        throw e;
        }
    }

}
