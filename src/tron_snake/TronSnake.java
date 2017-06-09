package tron_snake;

import framework.*;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class TronSnake extends GameRootPane {

    private final int [][] onePixel = {{1}};
    private class SnakeSegment extends PixelSprite {
        public SnakeSegment(String name) {
            super(onePixel, 1, 1, name, Color.WHITE);
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
        public SnakeSegment getSegment() { return segment; }
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }

    private final PixelSprite leftWall = new PixelSprite(onePixel, 700, 1, "wall", Color.WHITE);
    private final PixelSprite topWall = new PixelSprite(onePixel, 1, 700, "wall", Color.WHITE);
    private final PixelSprite rightWall = new PixelSprite(onePixel, 700, 1, "wall", Color.WHITE);
    private final PixelSprite bottomWall = new PixelSprite(onePixel, 1, 700, "wall", Color.WHITE);

    private final ArrayList<SegmentCoordinateWrapper> firstPlayerSnake = new ArrayList<>();
    private final ArrayList<SegmentCoordinateWrapper> secondPlayerSnake = new ArrayList<>();
    private final ArrayList<SnakeSegment> cookieStack = new ArrayList<>();
    private Direction firstPlayerDirection = Direction.RIGHT;
    private Direction secondPlayerDirection = Direction.LEFT;

    private final Score firstPlayerScore;
    private final Score secondPlayerScore;

    public TronSnake() {
        super("tron_snake", "null-pointer.ttf", "loud_nigra.mp3", 100, 1);
        initMenu(20, 12, Color.WHITE, Color.BLACK, "snow_halation.mp3", "TRON Snake");
        firstPlayerScore = new Score(this, 11, Color.WHITE, Color.GRAY, Pos.TOP_LEFT);
        secondPlayerScore = new Score(this, 11, Color.WHITE, Color.GRAY, Pos.TOP_RIGHT);
    }

    protected void onGameStart() {
        // screen 600 x 600
        setBackground(Color.BLACK);

        // insert walls
        addSprite(leftWall, 0, 300);
        addSprite(topWall, 300, 0);
        addSprite(rightWall, 599, 300);
        addSprite(bottomWall, 300, 599);

        // first player snake
        firstPlayerSnake.add(new SegmentCoordinateWrapper("firstPlayerSnakeHead", new Coordinate(150, 300)));
        // second player snake
        secondPlayerSnake.add(new SegmentCoordinateWrapper("secondPlayerSnakeHead", new Coordinate(450, 300)));

        // render the snake heads
        for(SegmentCoordinateWrapper s : firstPlayerSnake) {
            addSprite(s.getSegment(), s.getCurrentCoordinate().x, s.getCurrentCoordinate().y);
        }
        for(SegmentCoordinateWrapper s: secondPlayerSnake) {
            addSprite(s.getSegment(), s.getCurrentCoordinate().x, s.getCurrentCoordinate().y);
        }

        // bind keys
        addKeyBinding(new KeyAction() {
            @Override
            public KeyCode getKey() {
                return KeyCode.W;
            }

            @Override
            public boolean fireOnce() {
                return false;
            }

            @Override
            public void action() {
                firstPlayerDirection = Direction.UP;
            }
        });
        addKeyBinding(new KeyAction() {
            @Override
            public KeyCode getKey() {
                return KeyCode.A;
            }

            @Override
            public boolean fireOnce() {
                return false;
            }

            @Override
            public void action() {
                firstPlayerDirection = Direction.LEFT;
            }
        });
        addKeyBinding(new KeyAction() {
            @Override
            public KeyCode getKey() {
                return KeyCode.S;
            }

            @Override
            public boolean fireOnce() {
                return false;
            }

            @Override
            public void action() {
                firstPlayerDirection = Direction.DOWN;
            }
        });
        addKeyBinding(new KeyAction() {
            @Override
            public KeyCode getKey() {
                return KeyCode.D;
            }

            @Override
            public boolean fireOnce() {
                return false;
            }

            @Override
            public void action() {
                firstPlayerDirection = Direction.RIGHT;
            }
        });

        // second player bindings
        addKeyBinding(new KeyAction() {
            @Override
            public KeyCode getKey() {
                return KeyCode.UP;
            }

            @Override
            public boolean fireOnce() {
                return false;
            }

            @Override
            public void action() {
                secondPlayerDirection = Direction.UP;
            }
        });
        addKeyBinding(new KeyAction() {
            @Override
            public KeyCode getKey() {
                return KeyCode.LEFT;
            }

            @Override
            public boolean fireOnce() {
                return false;
            }

            @Override
            public void action() {
                secondPlayerDirection = Direction.LEFT;
            }
        });
        addKeyBinding(new KeyAction() {
            @Override
            public KeyCode getKey() {
                return KeyCode.DOWN;
            }

            @Override
            public boolean fireOnce() {
                return false;
            }

            @Override
            public void action() {
                secondPlayerDirection = Direction.DOWN;
            }
        });
        addKeyBinding(new KeyAction() {
            @Override
            public KeyCode getKey() {
                return KeyCode.RIGHT;
            }

            @Override
            public boolean fireOnce() {
                return false;
            }

            @Override
            public void action() {
                secondPlayerDirection = Direction.RIGHT;
            }
        });

    }

    protected void update() {
        // spawn cookies until 2 are in the field
        while(cookieStack.size() <= 2) {
            Coordinate randomCoord = randomCoordinate();
            SnakeSegment newCookie = new SnakeSegment("cookie");
            addSprite(newCookie, randomCoord.x, randomCoord.y);
            if (!newCookie.collided("firstPlayerSnakeHead") && !newCookie.collided("firstPlayerSnakeBody")
                    && !newCookie.collided("secondPlayerSnakeHead") &&
                    !newCookie.collided("secondPlayerSnakeBody")) {
                cookieStack.add(newCookie);
            }
            else {
                removeSprite(newCookie);
            }
        }

        Coordinate firstPlayerSnakeTailCoordinate = firstPlayerSnake.get(firstPlayerSnake.size() - 1).currentCoordinate;
        Coordinate secondPlayerSnakeTailCoordinate = secondPlayerSnake.get(secondPlayerSnake.size() - 1).currentCoordinate;

        updateSnake(firstPlayerDirection, secondPlayerDirection);

        if(firstPlayerSnake.get(0).getSegment().collided("wall")) {
            firstPlayerScore.removeFromScore(firstPlayerScore.getScore());
            for(SegmentCoordinateWrapper s : firstPlayerSnake) removeSprite(s.getSegment());
            firstPlayerSnake.clear();
            firstPlayerSnake.add(new SegmentCoordinateWrapper("firstPlayerSnakeHead", new Coordinate(150, 300)));
        }
        if(secondPlayerSnake.get(0).getSegment().collided("wall")) {
            secondPlayerScore.removeFromScore(secondPlayerScore.getScore());
            for(SegmentCoordinateWrapper s : secondPlayerSnake) removeSprite(s.getSegment());
            secondPlayerSnake.clear();
            secondPlayerSnake.add(new SegmentCoordinateWrapper("secondPlayerSnakeHead", new Coordinate(450, 300)));
        }

        for(SnakeSegment c : cookieStack) {
            if(c.collided("firstPlayerSnakeHead")) {
                firstPlayerSnake.add(firstPlayerSnake.size() - 1,
                        new SegmentCoordinateWrapper("firstPlayerSnakeBody", firstPlayerSnakeTailCoordinate));
                removeSprite(firstPlayerSnake.get(0).getSegment().getCollided("cookie"));
                firstPlayerScore.addToScore(1);
            }
            else if(c.collided("secondPlayerSnakeHead")) {
                secondPlayerSnake.add(secondPlayerSnake.size() - 1,
                        new SegmentCoordinateWrapper("secondPlayerSnakeBody", secondPlayerSnakeTailCoordinate));
                removeSprite(secondPlayerSnake.get(0).getSegment().getCollided("cookie"));
                secondPlayerScore.addToScore(1);
            }
        }

        if(firstPlayerSnake.get(0).getSegment().collided("secondPlayerSnakeHead")
                || firstPlayerSnake.get(0).getSegment().collided("secondPlayerSnakeBody")
                || secondPlayerSnake.get(0).getSegment().collided("firstPlayerSnakeHead")
                || secondPlayerSnake.get(0).getSegment().collided("firstPlayerSnakeBody")) {
            firstPlayerScore.removeFromScore(firstPlayerScore.getScore());
            secondPlayerScore.removeFromScore(secondPlayerScore.getScore());
            for(SegmentCoordinateWrapper s : firstPlayerSnake) removeSprite(s.getSegment());
            firstPlayerSnake.clear();
            firstPlayerSnake.add(new SegmentCoordinateWrapper("firstPlayerSnakeHead", new Coordinate(150, 300)));
            for(SegmentCoordinateWrapper s : secondPlayerSnake) removeSprite(s.getSegment());
            secondPlayerSnake.clear();
            secondPlayerSnake.add(new SegmentCoordinateWrapper("secondPlayerSnakeHead", new Coordinate(450, 300)));
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
                        new Coordinate(firstPlayerSnakeHeadCoordinate.x, firstPlayerSnakeHeadCoordinate.y - 1)));
                break;
            case DOWN:
                firstPlayerSnake.add(0, new SegmentCoordinateWrapper("firstPlayerSnakeHead",
                        new Coordinate(firstPlayerSnakeHeadCoordinate.x, firstPlayerSnakeHeadCoordinate.y + 1)));
                break;
            case LEFT:
                firstPlayerSnake.add(0, new SegmentCoordinateWrapper("firstPlayerSnakeHead",
                        new Coordinate(firstPlayerSnakeHeadCoordinate.x - 1, firstPlayerSnakeHeadCoordinate.y)));
                break;
            case RIGHT:
                firstPlayerSnake.add(0, new SegmentCoordinateWrapper("firstPlayerSnakeHead",
                        new Coordinate(firstPlayerSnakeHeadCoordinate.x + 1, firstPlayerSnakeHeadCoordinate.y)));
                break;
        }
        firstPlayerSnake.set(1, new SegmentCoordinateWrapper("firstPlayerSnakeBody", firstPlayerSnakeHeadCoordinate));
        removeSprite(firstPlayerSnake.get(firstPlayerSnake.size() - 1).getSegment());
        firstPlayerSnake.remove(firstPlayerSnake.size() - 1);

        for(SegmentCoordinateWrapper s : firstPlayerSnake) {
            addSprite(s.getSegment(), s.getCurrentCoordinate().x, s.getCurrentCoordinate().y);
        }

        switch(d2) {
            case UP:
                secondPlayerSnake.add(0, new SegmentCoordinateWrapper("secondPlayerSnakeHead",
                        new Coordinate(secondPlayerSnakeHeadCoordinate.x, secondPlayerSnakeHeadCoordinate.y - 1)));
                break;
            case DOWN:
                secondPlayerSnake.add(0, new SegmentCoordinateWrapper("secondPlayerSnakeHead",
                        new Coordinate(secondPlayerSnakeHeadCoordinate.x, secondPlayerSnakeHeadCoordinate.y + 1)));
                break;
            case LEFT:
                secondPlayerSnake.add(0, new SegmentCoordinateWrapper("secondPlayerSnakeHead",
                        new Coordinate(secondPlayerSnakeHeadCoordinate.x - 1, secondPlayerSnakeHeadCoordinate.y)));
                break;
            case RIGHT:
                secondPlayerSnake.add(0, new SegmentCoordinateWrapper("secondPlayerSnakeHead",
                        new Coordinate(secondPlayerSnakeHeadCoordinate.x + 1, secondPlayerSnakeHeadCoordinate.y)));
                break;
        }
        secondPlayerSnake.set(1, new SegmentCoordinateWrapper("secondPlayerSnakeBody", secondPlayerSnakeHeadCoordinate));
        removeSprite(secondPlayerSnake.get(secondPlayerSnake.size() - 1).getSegment());
        secondPlayerSnake.remove(secondPlayerSnake.size() - 1);

        for(SegmentCoordinateWrapper s: secondPlayerSnake) {
            addSprite(s.getSegment(), s.getCurrentCoordinate().x, s.getCurrentCoordinate().y);
        }
    }

    private Coordinate randomCoordinate() {
        Random r = new Random();
        return new Coordinate(r.nextInt(600), r.nextInt(600));
    }

}