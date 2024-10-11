package ee.gert.hi_lo_card_game;

public class StopWatch {
    private long startTime;
    private long elapsedTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        elapsedTime += System.nanoTime() - startTime;
    }

    public void reset() {
        elapsedTime = 0;
        startTime = 0;
    }

    public double getElapsedTime() {
        return elapsedTime / 1_000_000_000.0; // Convert to seconds
    }
}
