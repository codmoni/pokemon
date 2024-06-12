import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

public class GameController {
    private int pokemonIndex;//선택된 포켓몬의 인덱스
    private HomeView homeView;
    private int score;
    private int stage;
    private int evolution;
    private GameView gameView;

    public GameController(int pokemonIndex, HomeView homeView) {
        this.pokemonIndex = pokemonIndex;
        this.homeView = homeView;
        this.score = 0;
        this.stage = 0;
        this.evolution = 0;
    }

    //게임 시작
    public void startGame() {
        try {
            nextStage();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while starting the game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    //다음 스테이지로 진행
    private void nextStage() {
        if (stage < 3) {
            int size = stage + 3; // 3*3, 4*4, 5*5 순서대로
            gameView = new GameView(size, "img/pokemon" + (pokemonIndex + 1) + "_stage" + (stage + 1) + ".PNG", this);
            gameView.setVisible(true);
        } else {
            finishGame();
        }
    }

    //스테이지 완료
    public void completeStage(int stageScore) {
        score += stageScore;
        stage++;
        evolution = stage - 1;
        if (stage == 3) {
            homeView.displayStatus(pokemonIndex, true, score, evolution);
        } else {
            homeView.displayStatus(pokemonIndex, false, score, evolution);
        }
        nextStage();
    }

    //홈 화면으로 이동
    public void returnToHome() {
        homeView.displayStatus(pokemonIndex, false, score, Math.max(0, stage - 1));
        homeView.setVisible(true);
    }

    //게임 종료
    private void finishGame() {
        homeView.displayStatus(pokemonIndex, true, score, evolution);
        JOptionPane.showMessageDialog(null, "포켓몬을 획득했습니다! 총 점수: " + score);
        playSound("sounds/game_clear.wav");
        homeView.setVisible(true);
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
