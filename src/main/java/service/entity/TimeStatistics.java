package service.entity;


import javax.persistence.*;

@Entity
public class TimeStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int month;

    private int day;

    private int year;

    private long coutOfMistakes;

    private long totalCoutOfWords;

    public long getCoutOfMistakes() {
        return coutOfMistakes;
    }

    public void setCoutOfMistakes(long coutOfMistakes) {
        this.coutOfMistakes = coutOfMistakes;
    }

    public long getTotalCoutOfWords() {
        return totalCoutOfWords;
    }

    public void setTotalCoutOfWords(long totalCoutOfWords) {
        this.totalCoutOfWords = totalCoutOfWords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeStatistics)) return false;

        TimeStatistics that = (TimeStatistics) o;

        if (getMonth() != that.getMonth()) return false;
        if (getDay() != that.getDay()) return false;
        if (getYear() != that.getYear()) return false;
        return getCoutOfMistakes() == that.getCoutOfMistakes();
    }

    @Override
    public int hashCode() {
        int result = getMonth();
        result = 31 * result + getDay();
        result = 31 * result + getYear();
        return result;
    }

    public int getMonth() {

        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TimeStatistics() {

    }
}
