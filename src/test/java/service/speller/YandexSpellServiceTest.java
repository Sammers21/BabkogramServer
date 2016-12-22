package service.speller;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class YandexSpellServiceTest {

   /* @Test
    public void getAllRU_ENWords() throws Exception {
        List<String> allRU_enWords = YandexSpellService.getAllRU_ENWords("hello привет");
        assertTrue(allRU_enWords.size() == 2);
        assertTrue(allRU_enWords.contains("привет"));
        assertTrue(allRU_enWords.contains("hello"));
    }

    @Test
    public void joinText() throws Exception {
        List<String> allRU_enWords = YandexSpellService.getAllRU_ENWords("hello привет");
        StringUtils stringUtils = new StringUtils();
        String join = StringUtils.join(allRU_enWords, '+');
        assertTrue(join.equals("hello+привет"));
        join = StringUtils.join(Collections.singletonList("hello"), '+');
        assertTrue(join.equals("hello"));
    }

    @Test
    public void getJSon() throws Exception {
        List<Integer> wmCount = YandexSpellService.getCoutOfWrongMessagesInText("hello паралельный");
        assertTrue(wmCount.get(0) == 1
                && wmCount.get(1) == 1
                && wmCount.get(2) == 2);

    }*/

}