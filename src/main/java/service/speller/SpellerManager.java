package service.speller;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpellerManager {

    public final static String YANDEX_SPELLER_URI = "http://speller.yandex.net/services/spellservice.json/checkText";

    private static JSONArray get(String text) throws IOException, ParseException {

        //TODO make split for 10k symbols queeis
        Client client = Client.create();
        WebResource webResource = client
                .resource(YANDEX_SPELLER_URI + "?text=" + text);

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);
        String output = response.getEntity(String.class);
        //   System.out.println(output);
        JSONParser jsonParser = new JSONParser();
        JSONArray parse = (JSONArray) jsonParser.parse(output);
        return parse;
    }

    /**
     * 1st number is result is count of wrong words
     * second number is count of correct words
     * thirds is total amount of words
     *
     * @param text input text
     * @return list of results
     */
    public static List<Integer> getCoutOfWrongMessagesInText(String text) throws IOException, ParseException {
        List<Integer> integers = new ArrayList<>();
        List<String> allRU_enWords = getAllRU_ENWords(text);
        //encode all words in UTF8
        for (int i = 0; i < allRU_enWords.size(); i++) {
            allRU_enWords.set(i, URLEncoder.encode(allRU_enWords.get(i), "UTF8"));
        }
        //{word}+{word}+...+{word}
        String join = StringUtils.join(allRU_enWords, '+');

        //get all errors in grammar
        JSONArray jsonArray = get(join);


        integers.addAll(Arrays.asList(jsonArray.size(), allRU_enWords.size() - jsonArray.size(), allRU_enWords.size()));
        return integers;
    }

    public static List<String> getAllRU_ENWords(String text) {
        List<String> strings = new ArrayList<>();
        Pattern pattern = Pattern.compile("[a-zA-Z]+|[a-яА-Я]+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            strings.add(matcher.group());
        }
        return strings;
    }


}

