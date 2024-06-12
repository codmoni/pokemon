import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameView extends JFrame {
    private PuzzlePanel puzzlePanel;
    private int size;//퍼즐 크기
    private int moves;//이동 횟수
    private long startTime;//게임 시작 시간
    private GameController controller;
    private Timer timer;//타이머
    private long elapsedTime;//경과시간

    public GameView(int size, String imagePath, GameController controller) {
        this.size = size;
        this.controller = controller;
        this.moves = 0;
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = 0;
        loadImages(imagePath);
        initializeUI();
    }

    //이미지 로드하고 퍼즐 조각으로 분할
    private void loadImages(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            System.out.println("Loading image from: " + imageFile.getAbsolutePath());
            if (!imageFile.exists()) {
                throw new IOException("Image file does not exist: " + imageFile.getAbsolutePath());
            }
            BufferedImage image = ImageIO.read(imageFile);
            if (image == null) {
                throw new IOException("Failed to read image file: " + imageFile.getAbsolutePath());
            }
            int pieceWidth = image.getWidth() / size;
            int pieceHeight = image.getHeight() / size;
            Image[] images = new Image[size * size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    images[i * size + j] = image.getSubimage(j * pieceWidth, i * pieceHeight, pieceWidth, pieceHeight);
                }
            }
            PuzzleModel model = new PuzzleModel(size);
            puzzlePanel = new PuzzlePanel(images, model);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //게임 UI 초기화
    private void initializeUI() {
        setTitle("Pokemon Puzzle Game - Stage " + size + "x" + size);
        setLayout(new BorderLayout());

        JLabel movesLabel = new JLabel("Moves: 0");
        JLabel timeLabel = new JLabel("Time: 0 s");
        JButton skipButton = new JButton("Skip Stage");
        JButton exitButton = new JButton("Exit to Home");

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                timeLabel.setText("Time: " + elapsedTime + " s");
            }
        });
        timer.start();

        skipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int stageScore = calculateScore(moves, elapsedTime);
                timer.stop();
                controller.completeStage(stageScore);
                dispose();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                controller.returnToHome();
                dispose();
            }
        });

        add(puzzlePanel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(1, 4));
        infoPanel.add(movesLabel);
        infoPanel.add(timeLabel);
        infoPanel.add(skipButton);
        infoPanel.add(exitButton);
        add(infoPanel, BorderLayout.NORTH);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        puzzlePanel.addTileListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moves++;
                movesLabel.setText("Moves: " + moves);
                if (puzzlePanel.isSolved()) {
                    int stageScore = calculateScore(moves, elapsedTime);
                    timer.stop();
                    controller.completeStage(stageScore);
                    dispose();
                }
            }
        });
    }

    //점수 계산
    private int calculateScore(int moves, long time) {
        return 10000 - (moves * 10 + (int)time * 5);
    }
}
