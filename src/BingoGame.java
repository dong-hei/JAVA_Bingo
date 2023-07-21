import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

public class BingoGame {

    static JPanel panelNorth; //Top View
    static JPanel panelCenter; // Game View
    static JLabel labelMessge;
    static JButton[] buttons = new JButton[16];
    static String[] images ={
            "f1.png", "f2.png", "f3.png",
            "f4.png", "f5.png", "f6.png",
            "f7.png", "f8.png",
            "f1.png", "f2.png", "f3.png",
            "f4.png", "f5.png", "f6.png",
            "f7.png", "f8.png",
    };
    static int openCount = 0; //열렸을때 늘어는 카운트
    static int buttonIndexSave1 = 0; // 처음 열린 인덱스 값 0~15중 하나
    static int buttonIndexSave2 = 0; // 두번째 열린 인덱스 값 0~15중 하나
    static Timer timer;
    static int tryCount = 0; // 몇번 시도해서 성공했는지?
    static int successCount = 0; //빙고 성공 카운트 0~8


    static class MyFrame extends JFrame implements ActionListener{
        public MyFrame(String title) {
            super(title);
            this.setLayout(new BorderLayout());
            this.setSize(400, 500);
            this.setVisible(true);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            initUI(this);
            mixCard();
            this.pack();
        }

        static void playSound(String fileName) {
            File file = new File("./wav/" + fileName);
            if (file.exists()) {
                try {
                    AudioInputStream str = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(str);
                    clip.start();
                } catch (Exception e) {
                }
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {

            if (openCount == 2) {
                return;
            }

            JButton btn = (JButton) e.getSource();
            int index = getButtonIndex(btn);
            btn.setIcon(changeImage(images[index]));

            openCount++;
            if (openCount == 1) {
                buttonIndexSave1 = index;
            } else if (openCount == 2) {
                buttonIndexSave2 = index;
                tryCount++;
                labelMessge.setText("Find the Same Fruits" + " ( Try " + tryCount + ")");

                //판정 로직
                boolean isBingo = checkCard(buttonIndexSave1, buttonIndexSave2);
                if (isBingo == true) {
                    playSound("bingo.wav");
                    openCount = 0;
                    successCount++;
                    if (successCount == 8) {
                        labelMessge.setText("Game Over You're Winner!!");
                    }
                }else{
                    backToQuestion();

                }
            }
        }

        public void backToQuestion(){
            //Timer 1s
            timer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    playSound("fail.wav");
                    openCount = 0;
                    buttons[buttonIndexSave1].setIcon(changeImage("question.png"));
                    buttons[buttonIndexSave2].setIcon(changeImage("question.png"));
                    timer.stop();
                }
        });timer.start();
    }
        public boolean checkCard(int index1, int index2) {
            if (index1 == index2) {
                return false;
            }
            if (images[index1].equals(images[index2])) {
                return true;
            }else{
                return false;
            }

        }

        private int getButtonIndex(JButton btn) {
            int index = 0;
            for (int i = 0; i < 16; i++) {
                if (buttons[i] == btn) {
                    index = i;
                }
            }
            return index;
        }
    }
    static void mixCard(){
        Random rd = new Random();
        for (int i = 0; i < 1000; i++) {
            int random = rd.nextInt(15) + 1; // 1부터 15까지의 카드 인덱스 값
            //바꾼다
            String temp = images[0];
            images[0] = images[random];
            images[random] = temp;
        }
    }
    static void initUI(MyFrame myFrame){
        panelNorth = new JPanel();
        panelNorth.setBackground(Color.BLUE);
        labelMessge = new JLabel("Find the Same Fruits" + "Try 0");
        labelMessge.setPreferredSize(new Dimension(400, 100));
        labelMessge.setForeground(Color.white);
        labelMessge.setFont(new Font("Monaco", Font.BOLD, 20));
        labelMessge.setHorizontalAlignment(JLabel.CENTER); //가운데 정렬
        panelNorth.add(labelMessge);
        myFrame.add("North", panelNorth);

        panelCenter = new JPanel();
        panelCenter.setLayout(new GridLayout(4, 4));
        panelCenter.setPreferredSize(new Dimension(400, 400));
        for (int i = 0; i < 16; i++) {
            buttons[i] = new JButton();
            buttons[i].setPreferredSize(new Dimension(100, 100));
            buttons[i].setIcon(changeImage("question.png"));
            buttons[i].addActionListener(myFrame);
            panelCenter.add(buttons[i]);
        }
        myFrame.add("Center", panelCenter);
    }

    static ImageIcon changeImage(String filename) {
        ImageIcon icon = new ImageIcon("./imgs/" + filename);
        Image originImage = icon.getImage();
        Image changedImage = originImage.getScaledInstance(85, 85, Image.SCALE_SMOOTH);
        ImageIcon icon_new = new ImageIcon(changedImage);
        return icon_new;
    }

    public static void main(String[] args) {
        new MyFrame("Bingo Game");
    }
}
