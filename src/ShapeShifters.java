import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

class CatCrawl extends PApplet {
    int gridSize = 20;
    int cols, rows;
    Caterpillar caterpillar;
    ArrayList<Leaf> leaves;
    boolean upPressed, downPressed, rightPressed, leftPressed;
    int frameRate = 10; // Adjust this value to increase the movement speed
    boolean gameOver = false;
    boolean gameStarted = false;

    public void settings() {
        size(400, 400);
        cols = width / gridSize;
        rows = height / gridSize;
    }

    public void setup() {
        frameRate(frameRate); // Set the frame rate
        caterpillar = new Caterpillar();
        leaves = new ArrayList<>();
        spawnLeaf();
    }

    public void draw() {
        if (gameStarted) {
            if (!gameOver) {
                background(51);
                caterpillar.move();
                caterpillar.display();
                for (Leaf leaf : leaves) {
                    leaf.display();
                }
                checkCollision();
            } else {
                background(255, 0, 0);
                textAlign(CENTER, CENTER);
                textSize(32);
                fill(255);
                text("Game Over!", width / 2, height / 2);
                textSize(20);
                text("Press spacebar to try again", width / 2, height / 2 + 40);
            }
        } else {
            background(0);
            textAlign(CENTER, CENTER);
            textSize(24);
            fill(255);
            text("Welcome to SHAPESHIFTERS!\n \nUse AWSD controls for movement.\n \nPlease wait a moment,\nthen press spacebar to begin", width / 2, height/2);
        }
    }

    void spawnLeaf() {
        int col = floor(random(cols));
        int row = floor(random(rows));
        Leaf leaf = new Leaf(col * gridSize, row * gridSize);
        leaves.add(leaf);
    }

    void checkCollision() {
        PVector head = caterpillar.segments.get(0);
        if (head.x < 0 || head.x >= width || head.y < 0 || head.y >= height) {
            gameOver = true;
        }

        for (int i = leaves.size() - 1; i >= 0; i--) {
            Leaf leaf = leaves.get(i);
            if (caterpillar.eat(leaf)) {
                leaves.remove(i);
                spawnLeaf();
            }
        }
    }

    public void keyPressed() {
        if (key == ' ') {
            if (gameOver) {
                resetGame();
            } else {
                gameStarted = true;
            }
        }

        if (gameStarted && !gameOver) {
            if (key == 'w' || key == 'W') {
                upPressed = true;
            } else if (key == 's' || key == 'S') {
                downPressed = true;
            } else if (key == 'd' || key == 'D') {
                rightPressed = true;
            } else if (key == 'a' || key == 'A') {
                leftPressed = true;
            }
        }
    }

    public void keyReleased() {
        if (gameStarted && !gameOver) {
            if (key == 'w' || key == 'W') {
                upPressed = false;
            } else if (key == 's' || key == 'S') {
                downPressed = false;
            } else if (key == 'd' || key == 'D') {
                rightPressed = false;
            } else if (key == 'a' || key == 'A') {
                leftPressed = false;
            }
        }
    }

    void resetGame() {
        caterpillar = new Caterpillar();
        leaves.clear();
        spawnLeaf();
        gameOver = false;
        gameStarted = false;
    }

    class Caterpillar {
        ArrayList<PVector> segments;
        int xdir = 0;
        int ydir = 0;

        Caterpillar() {
            segments = new ArrayList<>();
            segments.add(new PVector(width / 2, height / 2));
        }

        void move() {
            if (upPressed) {
                setDirection(0, -1);
            } else if (downPressed) {
                setDirection(0, 1);
            } else if (rightPressed) {
                setDirection(1, 0);
            } else if (leftPressed) {
                setDirection(-1, 0);
            }

            if (frameCount % frameRate == 0) { // Move only once every 'frameRate' frames
                PVector head = segments.get(0).copy();
                head.x += xdir * gridSize;
                head.y += ydir * gridSize;
                segments.add(0, head);
                segments.remove(segments.size() - 1);
            }
        }

        void display() {
            for (PVector segment : segments) {
                fill(255);
                rect(segment.x, segment.y, gridSize, gridSize);
            }
        }

        void setDirection(int x, int y) {
            xdir = x;
            ydir = y;
        }

        boolean eat(Leaf leaf) {
            PVector head = segments.get(0);
            if (head.x == leaf.pos.x && head.y == leaf.pos.y) {
                segments.add(leaf.pos.copy());
                return true;
            }
            return false;
        }
    }

    class Leaf {
        PVector pos;
        Leaf(float x, float y) {
            pos = new PVector(x, y);
        }

        void display() {
            fill(255, 0, 0);
            ellipse(pos.x + gridSize / 2, pos.y + gridSize / 2, gridSize, gridSize);
        }
    }

    public static void main(String[] args) {
        String[] processingArgs = {"CaterpillarCrawl"};
        CatCrawl catCrawl = new CatCrawl();
        PApplet.runSketch(processingArgs, catCrawl);
    }
}
