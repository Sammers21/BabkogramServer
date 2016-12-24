package service.objects;

public class TImeStatResponse {
    long total_words;
    long correct_words;

    public TImeStatResponse(long total_words, long correct_words) {
        this.total_words = total_words;
        this.correct_words = correct_words;
    }

    public TImeStatResponse() {

    }

    public long getTotal_words() {

        return total_words;
    }

    public void setTotal_words(long total_words) {
        this.total_words = total_words;
    }

    public long getCorrect_words() {
        return correct_words;
    }

    public void setCorrect_words(long correct_words) {
        this.correct_words = correct_words;
    }
}
