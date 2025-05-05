package org.fastX.models;

import lombok.Getter;
import org.fastX.models.events.BallCompleteEvent;

import java.util.*;
import java.util.function.Consumer;

@Getter
public class Balls implements Iterable<BallCompleteEvent> {
    private final List<BallCompleteEvent> balls;
    private Score score;

    private Balls(List<BallCompleteEvent> balls, Score score) {
        this.balls = balls;
        this.score = score;
    }

    public static Balls newBalls() {
        return new Balls(new ArrayList<>(), Score.EMPTY);
    }

    public void add(BallCompleteEvent ball) {
        score = score.add(ball.getRunScored());

        balls.add(ball);
    }

    public BallCompleteEvent last() {
        return balls.stream()
                .max(Comparator.comparingInt((BallCompleteEvent b) -> b.getOverNo() * 6 + b.getBallNo()))
                .orElse(null);
    }

    @Override
    public Iterator<BallCompleteEvent> iterator() {
        return balls.iterator();
    }

    public int size(){
        return balls.size();
    }

    @Override
    public void forEach(Consumer<? super BallCompleteEvent> action) {
        balls.forEach(action);
    }

    @Override
    public Spliterator<BallCompleteEvent> spliterator() {
        return balls.spliterator();
    }
}
