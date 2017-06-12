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
        private Coordinate currentCoordinate;
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

    //private final PixelSprite leftWall = new PixelSprite(onePixel, 5, 590, "wall", Color.WHITE);
    //private final PixelSprite topWall = new PixelSprite(onePixel, 590, 5, "wall", Color.WHITE);
    //private final PixelSprite rightWall = new PixelSprite(onePixel, 5, 590, "wall", Color.WHITE);
    //private final PixelSprite bottomWall = new PixelSprite(onePixel, 590, 5, "wall", Color.WHITE);

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
        firstPlayerScore = new Score(this, "Player 1: ", 30, Color.WHITE, Color.GRAY, Pos.TOP_LEFT);
        secondPlayerScore = new Score(this, "Player 2: ", 30, Color.WHITE, Color.GRAY, Pos.TOP_RIGHT);
    }

    protected void onGameStart() {
        // screen 600 x 600
        setBackground(Color.BLACK);

        // insert walls
        /*addSprite(leftWall, 5, 295);
        addSprite(topWall, 295, 5);
        addSprite(rightWall, 595, 295);
        addSprite(bottomWall, 295, 595);*/

        // first player snake
        firstPlayerSnake.add(new SegmentCoordinateWrapper("firstPlayerSnakeHead", new Coordinate(150, 300)));
        // second player snake
        secondPlayerSnake.add(new SegmentCoordinateWrapper("secondPlayerSnakeHead", new Coordinate(450, 300)));

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
                case ESCAPE: 
                	togglePause();
                	break;
            }
        });


    }

    protected void update() {
        // spawn cookies until 5 are in the field
        while(cookieStack.size() < 5) {
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

        //Coordinate firstPlayerSnakeTailCoordinate = firstPlayerSnake.get(firstPlayerSnake.size() - 1).currentCoordinate;
        //Coordinate secondPlayerSnakeTailCoordinate = secondPlayerSnake.get(secondPlayerSnake.size() - 1).currentCoordinate;


        for(SnakeSegment c : cookieStack) {
            if(c.collided(firstPlayerSnake.get(0).segment)) {
                removeSprite(c);
                cookieStack.remove(c);
                firstPlayerScore.addToScore(1);
                firstPlayerSnake.add(new SegmentCoordinateWrapper("firstPlayerSnakeBody", new Coordinate(1000, 1000)));
            }
            else if(c.collided(secondPlayerSnake.get(0).segment)) {
                removeSprite(c);
                cookieStack.remove(c);
                secondPlayerScore.addToScore(1);
                secondPlayerSnake.add(new SegmentCoordinateWrapper("secondPlayerSnakeBody", new Coordinate(1001, 1000)));
            }
        }

        updateSnake(firstPlayerDirection, secondPlayerDirection);

        /*if(firstPlayerSnake.get(0).segment.collided("wall")) {
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
        }*/
        for(SegmentCoordinateWrapper s : firstPlayerSnake) {
            if(secondPlayerSnake.get(0).segment.collided(s.segment)) resetSnakes();
        }
        for(SegmentCoordinateWrapper s: secondPlayerSnake) {
            if(firstPlayerSnake.get(0).segment.collided(s.segment)) resetSnakes();
        }

    }

    protected void onPause() {}
    protected void onResume() {}

    private void updateSnake(Direction d1, Direction d2) {
        Coordinate firstPlayerSnakeHeadCoordinate = firstPlayerSnake.get(0).getCurrentCoordinate();
        Coordinate secondPlayerSnakeHeadCoordinate = secondPlayerSnake.get(0).getCurrentCoordinate();

        switch(d1) {
            case UP:
                firstPlayerSnakeHeadCoordinate = new Coordinate(firstPlayerSnakeHeadCoordinate.x, firstPlayerSnakeHeadCoordinate.y - 5);
                break;
            case DOWN:
                firstPlayerSnakeHeadCoordinate = new Coordinate(firstPlayerSnakeHeadCoordinate.x, firstPlayerSnakeHeadCoordinate.y + 5);
                break;
            case LEFT:
                firstPlayerSnakeHeadCoordinate = new Coordinate(firstPlayerSnakeHeadCoordinate.x - 5, firstPlayerSnakeHeadCoordinate.y);
                break;
            case RIGHT:
                firstPlayerSnakeHeadCoordinate = new Coordinate(firstPlayerSnakeHeadCoordinate.x + 5, firstPlayerSnakeHeadCoordinate.y);
                break;
        }

        for(SegmentCoordinateWrapper s : firstPlayerSnake) {
            removeSprite(s.segment);
        }

        for(int i = firstPlayerSnake.size() - 1; i > 0; i--) {
            firstPlayerSnake.get(i).currentCoordinate = firstPlayerSnake.get(i - 1).currentCoordinate;
        }
        firstPlayerSnake.get(0).currentCoordinate = firstPlayerSnakeHeadCoordinate;


        for(SegmentCoordinateWrapper s : firstPlayerSnake) {
            addSprite(s.segment, s.getCurrentCoordinate().x, s.getCurrentCoordinate().y);
        }

        switch(d2) {
            case UP:
                secondPlayerSnakeHeadCoordinate = new Coordinate(secondPlayerSnakeHeadCoordinate.x, secondPlayerSnakeHeadCoordinate.y - 5);
                break;
            case DOWN:
                secondPlayerSnakeHeadCoordinate = new Coordinate(secondPlayerSnakeHeadCoordinate.x, secondPlayerSnakeHeadCoordinate.y + 5);
                break;
            case LEFT:
                secondPlayerSnakeHeadCoordinate = new Coordinate(secondPlayerSnakeHeadCoordinate.x - 5, secondPlayerSnakeHeadCoordinate.y);
                break;
            case RIGHT:
                secondPlayerSnakeHeadCoordinate = new Coordinate(secondPlayerSnakeHeadCoordinate.x + 5, secondPlayerSnakeHeadCoordinate.y);
                break;
        }

        for(SegmentCoordinateWrapper s : secondPlayerSnake) {
            removeSprite(s.segment);
        }

        for(int i = secondPlayerSnake.size() - 1; i > 0; i--) {
            secondPlayerSnake.get(i).currentCoordinate = secondPlayerSnake.get(i - 1).currentCoordinate;
        }
        secondPlayerSnake.get(0).currentCoordinate = secondPlayerSnakeHeadCoordinate;

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