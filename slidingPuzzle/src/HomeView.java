import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeView extends JFrame {
    private JButton[] pokemonButtons = new JButton[5];//포켓몬 선택 버튼 배열
    private JLabel[] scoreLabels = new JLabel[5];//포켓몬 점수 라벨 배열
    private boolean[] pokemonAcquired = new boolean[5]; // 포켓몬 획득(최종진화) 여부
    private int[] scores = new int[5]; // 각 포켓몬의 점수 배열
    private int[] evolutions = new int[5]; // 포켓몬의 진화 단계 배열

    public HomeView() {
        setTitle("Pokemon Puzzle Game");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //메인 패널 설정
        JPanel mainPanel = new JPanel(new GridLayout(2, 3));
        for (int i = 0; i < 5; i++) {
            JPanel pokemonPanel = new JPanel(new BorderLayout());
            pokemonButtons[i] = new JButton(new ImageIcon("img/pokemon" + (i + 1) + "_stage1.PNG"));
            int index = i;
            pokemonButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startGame(index);//포켓몬 선택 시 게임 시작
                }
            });
            pokemonPanel.add(pokemonButtons[i], BorderLayout.CENTER);

            scoreLabels[i] = new JLabel(fetchStatus(scores[i], evolutions[i]), SwingConstants.CENTER);
            pokemonPanel.add(scoreLabels[i], BorderLayout.SOUTH);

            mainPanel.add(pokemonPanel);
        }

        add(mainPanel, BorderLayout.CENTER);

        JLabel clearLabel = new JLabel("Game Clear!", SwingConstants.CENTER);
        clearLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(clearLabel, BorderLayout.SOUTH);
        clearLabel.setVisible(false);

        if (allPokemonAcquired()) {
            clearLabel.setVisible(true);
        }
    }

    //모든 포켓몬을 획득했는지 확인
    private boolean allPokemonAcquired() {
        for (boolean acquired : pokemonAcquired) {
            if (!acquired) {
                return false;
            }
        }
        return true;
    }

    //게임 시작
    private void startGame(int pokemonIndex) {
        setVisible(false); // HomeView를 숨깁니다.
        new GameController(pokemonIndex, this).startGame();
    }

    //포켓몬 상태 정보 반환
    private String fetchStatus(int score, int evolution) {
        String evolutionText = (evolution == 2) ? "최종 진화" : (evolution + 1) + "단계";
        return "<html>획득점수: " + score + "<br>진화: " + evolutionText + "</html>";
    }

    //포켓몬 상태 업데이트
    public void displayStatus(int index, boolean acquired, int score, int evolution) {
        pokemonAcquired[index] = acquired;
        scores[index] = score;
        evolutions[index] = evolution;
        scoreLabels[index].setText(fetchStatus(score, evolution));

        pokemonButtons[index].setIcon(new ImageIcon("img/pokemon" + (index + 1) + "_stage" + (evolution + 1) + ".PNG"));

        if (allPokemonAcquired()) {
            ((JLabel) getContentPane().getComponent(1)).setVisible(true);
        }
    }
}
