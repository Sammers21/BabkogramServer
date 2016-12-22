package service.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import service.entity.TimeStatistics;

import java.util.List;


public interface TimeStatisticsRepository extends CrudRepository<TimeStatistics, Long> {
    List<TimeStatistics> findByUser(String user);


    List<TimeStatistics> findByYearAndMonthAndDay(int year, int month, int day);
}
