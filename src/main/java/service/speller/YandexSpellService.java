package service.speller;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.entity.Message;
import service.entity.TimeStatistics;
import service.entity.User;
import service.repository.MessageRepository;
import service.repository.TimeStatisticsRepository;
import service.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class YandexSpellService extends Thread {
    private final Logger log = Logger.getLogger(YandexSpellService.class.getName());

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeStatisticsRepository timeStatisticsRepository;

    public final static String YANDEX_SPELLER_URI = "http://speller.yandex.net/services/spellservice.json/checkText";

    private BlockingQueue<Message> blockingQueue = new LinkedBlockingQueue<>();

    private JSONArray get(String text) throws IOException, ParseException {

        //TODO make split for 10k symbols queries
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

    public void asyncTask(Message message) throws InterruptedException {
        blockingQueue.put(message);
        log.debug("message in queue");
    }

    @PostConstruct
    private void startOfBean() {
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            Message message = null;
            try {
                message = blockingQueue.poll(10, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("start of handling message from " + message.getSender());
            User byUsername = userRepository.findByUsername(message.getSender());
            try {
                List<Integer> coutOfWrongMessagesInText = getCoutOfWrongMessagesInText(message.getContent());
                reportStatistics(coutOfWrongMessagesInText, byUsername, message.getTimestamp());
                log.debug("handled message from " + message.getSender());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 1st number is result is count of wrong words
     * second number is count of correct words
     * thirds is total amount of words
     *
     * @param text input text
     * @return list of results
     */
    public List<Integer> getCoutOfWrongMessagesInText(String text) throws IOException, ParseException {
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

    /**
     * Method to find all substrings in text
     *
     * @param text text
     * @return substrings
     */
    public List<String> getAllRU_ENWords(String text) {
        List<String> strings = new ArrayList<>();
        Pattern pattern = Pattern.compile("[a-zA-Z]+|[a-яА-Я]+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            strings.add(matcher.group());
        }
        return strings;
    }

    private void reportStatistics(List<Integer> messageState, User user, long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(time * 1000);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        TimeStatistics ts = null;
        List<TimeStatistics> ts2 = timeStatisticsRepository.findByYearAndMonthAndDay(year, month, day).stream()
                .filter(s -> s.getUser() != null && s.getUser().getUsername().equals(user.getUsername()))
                .collect(Collectors.toList());
        if (ts2.size() != 0)
            ts = ts2.get(0);
        if (ts == null) {
            ts = new TimeStatistics();
            ts.setCoutOfMistakes(messageState.get(0));
            ts.setTotalCoutOfWords(messageState.get(2));
            ts.setYear(year);
            ts.setMonth(month);
            ts.setDay(day);
            user.addTimeStatistics(ts);
        } else {
            ts.setCoutOfMistakes(ts.getCoutOfMistakes() + messageState.get(0));
            ts.setTotalCoutOfWords(ts.getTotalCoutOfWords() + messageState.get(2));
            user.addExist(ts);

        }
        timeStatisticsRepository.save(ts);
        userRepository.save(user);


    }

}

