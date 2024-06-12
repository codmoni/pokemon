import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PuzzlePanel extends JPanel {
    private Image[] images;//퍼즐 조각 이미지 배열
    private int[][] board;//퍼즐 보드 상태 배열
    private int size;
    private int pieceWidth;//퍼즐 조각 하나의 너비
    private int pieceHeight;//퍼즐 조각 하나의 높이
    private PuzzleModel model;
    private ActionListener listener;//퍼즐 조각 클릭 리스너

    public PuzzlePanel(Image[] images, PuzzleModel model) {
        this.images = images;
        this.model = model;
        this.size = model.getBoard().length;
        this.board = model.getBoard();
        this.pieceWidth = images[0].getWidth(null);//퍼즐 조각의 너비. 첫번째 조각을 기준으로 설정
        this.pieceHeight = images[0].getHeight(null);//퍼즐 조각의 높이

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / pieceWidth;
                int row = e.getY() / pieceHeight;
                if (model.move(row, col)) {
                    repaint();
                    if (listener != null) {
                        listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, row + "," + col));
                    }
                    playSound("sounds/tile_click.wav");
                }
            }
        });
    }

    //퍼즐 조각 클릭 리스너 추가
    public void addTileListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int pieceIndex = board[i][j];
                if (pieceIndex != -1) { // 빈 칸이 아닌 경우에만 그리기
                    g.drawImage(images[pieceIndex], j * pieceWidth, i * pieceHeight, this);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(j * pieceWidth, i * pieceHeight, pieceWidth, pieceHeight);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(pieceWidth * size, pieceHeight * size);
    }

    //퍼즐이 해결되었는지 확인
    public boolean isSolved() {
        return model.isSolved();
    }

     // 효과음 재생
    private void playSound(String soundFile) {
        try {
            File file = new File(soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
