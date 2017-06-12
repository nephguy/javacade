package tron_snake;

import framework.*;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class TronSnake extends GameRootPane {

    private final int [][] onePixel = {{1}};
    private class SnakeSegment extends PixelSprite {
        public SnakeSegment(String name) {
            super(onePixel, 5, 5, name, Color.WHITE);
        }
    }

    private class Coordinate {
        final int x;
        final int y;
        public Coordinate(int a, int b) {
            x = a;
            y = b;
        }
    }

    private class SegmentCoordinateWrapper {
        private final Coordinate currentCoordinate;
        private final SnakeSegment segment;
        public SegmentCoordinateWrapper(String id, Coordinate c) {
            segment = new SnakeSegment(id);
            currentCoordinate = c;
        }
        public Coordinate getCurrentCoordinate() { return currentCoordinate; }
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }

    private final PixelSprite leftWall = new PixelSprite(onePixel, 5, 700, "wall", Color.WHITE);
    private final PixelSprite topWall = new PixelSprite(onePixel, 700, 5, "wall", Color.WHITE);
    private final PixelSprite rightWall = new PixelSprite(onePixel, 5, 700, "wall", Color.WHITE);
    private final PixelSprite bottomWall = new PixelSprite(onePixel, 700, 5, "wall", Color.WHITE);

    private final ArrayList<SegmentCoordinateWrapper> firstPlayerSnake = new ArrayList<>();
    private final ArrayList<SegmentCoordinateWrapper> secondPlayerSnake = new ArrayList<>();
    private final ArrayList<SnakeSegment> cookieStack = new ArrayList<>();
    private Direction firstPlayerDirection = Direction.RIGHT;
    private Direction secondPlayerDirection = Direction.LEFT;

    private final Score firstPlayerScore;
    private final Score secondPlayerScore;

    public TronSnake() {
        super("tron_snake", "null-pointer.ttf", "eBeat.wav", 100, 5);
        initMenu(40, 30, Color.WHITE, Color.BLACK, "eBeat.wav", "TRON Snake");
        firstPlayerScore = new Score(this, 30, Color.WHITE, Color.GRAY, Pos.TOP_LEFT);
        secondPlayerScore = new Score(this, 30, Color.WHITE, Color.GRAY, Pos.TOP_RIGHT);
    }

    protected void onGameStart() {
        // screen 600 x 600
        setBackground(Color.BLACK);

        // insert walls
        addSprite(leftWall, 0, 300);
        addSprite(topWall, 300, 0);
        addSprite(rightWall, 600, 300);
        addSprite(bottomWall, 300, 600);

        // first player snake
        firstPlayerSnake.add(new SegmentCoordinateWrapper("firstPlayerSnakeHead", new Coordinate(150, 300)));
        // second player snake
        secondPlayerSnake.add(new SegmentCoordinateWrapper("secondPlayerSnakeHead", new Coordinate(450, 300)));

        // render the snake heads
        for(SegmentCoordinateWrapper s : firstPlayerSnake) {
            addSprite(s.segment, s.getCurrentCoordinate().x, s.getCurrentCoordinate().y);
        }
        for(SegmentCoordinateWrapper s: secondPlayerSnake) {
            addSprite(s.segment, s.getCurrentCoordinate().x, s.getCurrentCoordinate().y);
        }

        // bind keys
        this.getScene().setOnKeyPressed( event -> {
            switch (event.getCode()) {
                default:
                    return;
                case W:
                    firstPlayerDirection = Direction.UP;
                    break;
                case A:
                    firstPlayerDirection = Direction.LEFT;
                    break;
                case S:
                    firstPlayerDirection = Direction.DOWN;
                    break;
                case D:
                    firstPlayerDirection = Direction.RIGHT;
                    break;
                case UP:
                    secondPlayerDirection = Direction.UP;
                    break;
                case LEFT:
                    secondPlayerDirection = Direction.LEFT;
                    break;
                case DOWN:
                    secondPlayerDirection = Direction.DOWN;
                    break;
                case RIGHT:
                    secondPlayerDirection = Direction.RIGHT;
                    break;
            }
        });


    }

    protected void update() {
        // spawn cookies until 2 are in the field
        while(cookieStack.size() < 2) {
            Coordinate randomCoord = randomCoordinate();
            SnakeSegment newCookie = new SnakeSegment("cookie");
            addSprite(newCookie, randomCoord.x, randomCoord.y);
            if (!newCookie.collided("firstPlayerSnakeHead") && !newCookie.collided("secondPlayerSnakeHead")) {
                cookieStack.add(newCookie);
            }
            else if(firstPlayerSnake.size() > 1) {
                if(!newCookie.collided("firstPlayerSnakeBody")) {
                    cookieStack.add(newCookie);
                }
            }
            else if(secondPlayerSnake.size() > 1) {
                if(!newCookie.collided("secondPlayerSnakeBody")) {
                    cookieStack.add(newCookie);
                }
            }
            else {
                removeSprite(newCookie);
            }
        }

        Coordinate firstPlayerSnakeTailCoordinate = firstPlayerSnake.get(firstPlayerSnake.size() - 1).currentCoordinate;
        Coordinate secondPlayerSnakeTailCoordinate = secondPlayerSnake.get(secondPlayerSnake.size() - 1).currentCoordinate;

        updateSnake(firstPlayerDirection, secondPlayerDirection);

        for(SnakeSegment c : cookieStack) {
            if(c.collided("firstPlayerSnakeHead")) {
                System.out.println("1Nom");
                firstPlayerSnake.add(firstPlayerSnake.size() - 1,
                        new SegmentCoordinateWrapper("firstPlayerSnakeBody", firstPlayerSnakeTailCoordinate));
                removeSprite(firstPlayerSnake.get(0).segment.getCollided("cookie"));
                firstPlayerScore.addToScore(1);
            }
            else if(c.collided("secondPlayerSnakeHead")) {
                System.out.println("2Nom");
                secondPlayerSnake.add(secondPlayerSnake.size() - 1,
                        new SegmentCoordinateWrapper("secondPlayerSnakeBody", secondPlayerSnakeTailCoordinate));
                removeSprite(secondPlayerSnake.get(0).segment.getCollided("cookie"));
                secondPlayerScore.addToScore(1);
            }
        }

        if(firstPlayerSnake.get(0).segment.collided("wall")) {
            firstPlayerScore.removeFromScore(firstPlayerScore.getScore());
            for(SegmentCoordinateWrapper s : firstPlayerSnake) removeSprite(s.segment);
            firstPlayerSnake.clear();
            firstPlayerSnake.add(new SegmentCoordinateWrapper("firstPlayerSnakeHead", new Coordinate(150, 300)));
        }
        if(secondPlayerSnake.get(0).segment.collided("wall")) {
            secondPlayerScore.removeFromScore(secondPlayerScore.getScore());
            for(SegmentCoordinateWrapper s : secondPlayerSnake) removeSprite(s.segment);
            secondPlayerSnake.clear();
            secondPlayerSnake.add(new SegmentCoordinateWrapper("secondPlayerSnakeHead", new Coordinate(450, 300)));
        }
        try {
            if (firstPlayerSnake.get(0).segment.collided("secondPlayerSnakeHead")
                    || secondPlayerSnake.get(0).segment.collided("firstPlayerSnakeHead")) {
                resetSnakes();
            }
        } catch(NullPointerException e) {
            System.out.println(firstPlayerSnake.get(0).segment.getId());
            System.out.println(secondPlayerSnake.get(0).segment.getId());
        }
        if(secondPlayerSnake.size() > 1) {
            if(firstPlayerSnake.get(0).segment.collided("secondPlayerSnakeBody")) {
                resetSnakes();
            }
        }
        if(firstPlayerSnake.size() > 1) {
            if(secondPlayerSnake.get(0).segment.collided("firstPlayerSnakeBody")) {
                resetSnakes();
            }
        }

    }

    protected void onPause() {}
    protected void onResume() {}

    private void updateSnake(Direction d1, Direction d2) {
        Coordinate firstPlayerSnakeHeadCoordinate = firstPlayerSnake.get(0).getCurrentCoordinate();
        Coordinate secondPlayerSnakeHeadCoordinate = secondPlayerSnake.get(0).getCurrentCoordinate();

        switch(d1) {
            case UP:
                firstPlayerSnake.add(0, new SegmentCoordinateWrapper("firstPlayerSnakeHead",
                        new Coordinate(firstPlayerSnakeHeadCoordinate.x, firstPlayerSnakeHeadCoordinate.y - 5)));
                break;
            case DOWN:
                firstPlayerSnake.add(0, new SegmentCoordinateWrapper("firstPlayerSnakeHead",
                        new Coordinate(firstPlayerSnakeHeadCoordinate.x, firstPlayerSnakeHeadCoordinate.y + 5)));
                break;
            case LEFT:
                firstPlayerSnake.add(0, new SegmentCoordinateWrapper("firstPlayerSnakeHead",
                        new Coordinate(firstPlayerSnakeHeadCoordinate.x - 5, firstPlayerSnakeHeadCoordinate.y)));
                break;
            case RIGHT:
                firstPlayerSnake.add(0, new SegmentCoordinateWrapper("firstPlayerSnakeHead",
                        new Coordinate(firstPlayerSnakeHeadCoordinate.x + 5, firstPlayerSnakeHeadCoordinate.y)));
                break;
        }
        removeSprite(firstPlayerSnake.get(firstPlayerSnake.size() - 1).segment);
        firstPlayerSnake.remove(firstPlayerSnake.size() - 1);
        if(firstPlayerSnake.size() >= 2) {
            firstPlayerSnake.set(1, new SegmentCoordinateWrapper("firstPlayerSnakeBody", firstPlayerSnakeHeadCoordinate));
        }


        for(SegmentCoordinateWrapper s : firstPlayerSnake) {
            addSprite(s.segment, s.getCurrentCoordinate().x, s.getCurrentCoordinate().y);
        }

        switch(d2) {
            case UP:
                secondPlayerSnake.add(0, new SegmentCoordinateWrapper("secondPlayerSnakeHead",
                        new Coordinate(secondPlayerSnakeHeadCoordinate.x, secondPlayerSnakeHeadCoordinate.y - 5)));
                break;
            case DOWN:
                secondPlayerSnake.add(0, new SegmentCoordinateWrapper("secondPlayerSnakeHead",
                        new Coordinate(secondPlayerSnakeHeadCoordinate.x, secondPlayerSnakeHeadCoordinate.y + 5)));
                break;
            case LEFT:
                secondPlayerSnake.add(0, new SegmentCoordinateWrapper("secondPlayerSnakeHead",
                        new Coordinate(secondPlayerSnakeHeadCoordinate.x - 5, secondPlayerSnakeHeadCoordinate.y)));
                break;
            case RIGHT:
                secondPlayerSnake.add(0, new SegmentCoordinateWrapper("secondPlayerSnakeHead",
                        new Coordinate(secondPlayerSnakeHeadCoordinate.x + 5, secondPlayerSnakeHeadCoordinate.y)));
                break;
        }
        removeSprite(secondPlayerSnake.get(secondPlayerSnake.size() - 1).segment);
        secondPlayerSnake.remove(secondPlayerSnake.size() - 1);
        if(secondPlayerSnake.size() >= 2) {
            secondPlayerSnake.set(1, new SegmentCoordinateWrapper("secondPlayerSnakeBody", secondPlayerSnakeHeadCoordinate));
        }


        for(SegmentCoordinateWrapper s: secondPlayerSnake) {
            addSprite(s.segment, s.getCurrentCoordinate().x, s.getCurrentCoordinate().y);
        }
    }

    private Coordinate randomCoordinate() {
        Random r = new Random();
        return new Coordinate(r.nextInt(120) * 5, r.nextInt(120) * 5);
    }

    private void resetSnakes() {
        firstPlayerScore.removeFromScore(firstPlayerScore.getScore());
        secondPlayerScore.removeFromScore(secondPlayerScore.getScore());
        for(SegmentCoordinateWrapper s : firstPlayerSnake) removeSprite(s.segment);
        firstPlayerSnake.clear();
        firstPlayerSnake.add(new SegmentCoordinateWrapper("firstPlayerSnakeHead", new Coordinate(150, 300)));
        for(SegmentCoordinateWrapper s : secondPlayerSnake) removeSprite(s.segment);
        secondPlayerSnake.clear();
        secondPlayerSnake.add(new SegmentCoordinateWrapper("secondPlayerSnakeHead", new Coordinate(450, 300)));
    }

}