package service.repository;

import org.springframework.data.repository.CrudRepository;
import service.entity.TimeStatistics;


public interface TimeStatisticsRepository extends CrudRepository<TimeStatistics, Long> {
}
