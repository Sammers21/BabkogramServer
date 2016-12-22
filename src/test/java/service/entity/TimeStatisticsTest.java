package service.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import service.BaseControllerForAllTests;
import service.repository.*;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TimeStatisticsTest extends BaseControllerForAllTests {

    @Autowired
    private TimeStatisticsRepository timeStatisticsRepository;


    @Test
    public void kek() {
        User pavell = userRepository.findByUsername("pavell");
        TimeStatistics timeStatistics = new TimeStatistics();
        timeStatistics.setCoutOfMistakes(1);
        timeStatistics.setTotalCoutOfWords(2);
        pavell.addTimeStatistics(timeStatistics);
        timeStatisticsRepository.save(timeStatistics);
        userRepository.save(pavell);
        User pavell1 = userRepository.findByUsername("pavell");
        Set<TimeStatistics> statistics = pavell1.getStatistics();


    }

    @Autowired
    public void setup(MockMvc mockMvc, UserRepository userRepository, TokenRepository tokenRepository, DialogRepository dialogRepository, MessageRepository messageRepository) {
        super.setup(mockMvc, userRepository, tokenRepository, dialogRepository, messageRepository);
    }
}