import java.util.Random;

public class PuzzleModel {
    private int[][] board;//퍼즐 보드를 나타내는 2차원 배열
    private int size;//퍼즐의 크기(3 ~ 5)
    private int emptyRow;//빈칸의 행 위치
    private int emptyCol;//빈칸의 열 위치
    private int moves;//이동 횟수
    private long startTime;//게임 시작 시간

    public PuzzleModel(int size) {
        this.size = size;
        board = new int[size][size];
        initializeBoard();
        shuffleBoard();
        startTime = System.currentTimeMillis();
    }

    //퍼즐 보드 초기화
    private void initializeBoard() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = count++;
            }
        }
        emptyRow = size - 1;
        emptyCol = size - 1;
        board[emptyRow][emptyCol] = -1; // 빈 칸 표시
    }

    //퍼즐 조각들 무작위로 섞기
    private void shuffleBoard() {
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {//1000번 반복하여 퍼즐 섞기
            int direction = rand.nextInt(4);//0~3사이의 무작위 정수 생성
            switch (direction) {
                case 0: move(emptyRow - 1, emptyCol); break;//빈칸 <-switch->빈칸 위 tile
                case 1: move(emptyRow + 1, emptyCol); break;//빈칸 <-switch->빈칸 아래 tile
                case 2: move(emptyRow, emptyCol - 1); break;//빈칸 <-switch->빈칸 왼쪽 tile
                case 3: move(emptyRow, emptyCol + 1); break;//빈칸 <-switch->빈칸 오른쪽 tile
            }
        }
        moves = 0;//이동 횟수 0으로 초기화
    }

    //퍼즐 조각 이동
    public boolean move(int row, int col) {
        if (0 <= row && row < size && 0 <= col && col < size && 
            (Math.abs(emptyRow - row) + Math.abs(emptyCol - col) == 1)) {
            board[emptyRow][emptyCol] = board[row][col];//
            board[row][col] = -1;
            emptyRow = row;
            emptyCol = col;
            moves++;
            return true;
        }
        return false;
    }

    //현재 퍼즐보드 상태 반환
    public int[][] getBoard() {
        return board;
    }

    //이동 횟수 반환
    public int getMoves() {
        return moves;
    }

    //경과 시간 반환
    public long getTimeElapsed() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    //퍼즐 해결 여부 반환
    public boolean isSolved() {
        int count = 0;
        //board 배열의 위치가 초기화했을 때의 상태와 같은 지 확인
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != (count == size * size - 1 ? -1 : count)) {
                    return false;
                }
                count++;
            }
        }
        return true;
    }
}
