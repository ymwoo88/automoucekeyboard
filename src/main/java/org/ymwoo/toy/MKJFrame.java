package org.ymwoo.toy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MKJFrame extends JFrame implements ActionListener, KeyListener {

    private static boolean ON_OFF = false;
    private static Integer BOUNCE = 10000;
    private static Integer START_TIME = 0;
    private static Integer END_TIME = 2359;
    private static int KEY_CODE = KeyEvent.VK_PRINTSCREEN;
    private static int LIST_MODEL_DELTE_INDEX = 0;
    private JTextField startTime;
    private JTextField endTime;
    private JTextField bounceTime;
    private JTextField pressKey;
    private DefaultListModel<String> model;
    private JList jList;
    private JScrollPane jScrollPane;

    /**
     * JFrame UI
     */
    public MKJFrame() {
        setTitle("Toy of YMW v1.0");

        JLabel notice = new JLabel("<html>오마키에 오신걸 환영합니다. <br> 메모리관리를위해 로그는 20라인까지만 기록됩니다.</html>");
        notice.setOpaque(true);
        notice.setBackground(Color.DARK_GRAY);
        notice.setForeground(Color.WHITE);
        JPanel north0 = new JPanel();
        north0.add(notice);

        //문자열을 입력할수 있는 UI
        JLabel label1 = new JLabel("반복간격(초) (범위 5 ~ 60)");
        bounceTime = new JTextField("5", 2);
        JLabel label2 = new JLabel("작동시간 (범위 0000 ~ 2359)");
        startTime = new JTextField("0000", 4);
        endTime = new JTextField("2359", 4);
        JPanel north1 = new JPanel();
        north1.add(label1);
        north1.add(bounceTime);
        north1.add(label2);
        north1.add(startTime);
        north1.add(endTime);

        // Layout 중요한 부분
        JPanel pg0 = new JPanel(new BorderLayout());
        // 새로 추가한 컨테이너에 기존의 컨테이너를 올려주자.
        pg0.add(north0, BorderLayout.NORTH);
        pg0.add(north1, BorderLayout.CENTER);

        //문자열을 입력할수 있는 UI
        JLabel label3 = new JLabel("입력 키 지정 (커서위치 후 설정 키를 눌러주세요)");
        pressKey = new JTextField("Print Screen", 15);
        JPanel north2 = new JPanel();
        north2.add(label3);
        north2.add(pressKey);
        pressKey.addKeyListener(this);

        //목록을 출력할 UI
        jList = new JList<>();
        //JList 에 출력할 데이터를 가지고 있는 모델 객체
        model = new DefaultListModel<>();
        //모델을 JList 에 연결하기
        jList.setModel(model);
        //스크롤 페널에 JList 넣어주기
        jScrollPane = new JScrollPane(jList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //버튼을 만들어서
        JButton start = new JButton("시작");
        start.setActionCommand("start");
        start.addActionListener(this);
        JButton stop = new JButton("정지");
        stop.setActionCommand("stop");
        stop.addActionListener(this);
        JPanel south = new JPanel();
        south.add(start);
        south.add(stop);

        // Layout 중요한 부분
        JPanel pg = new JPanel(new BorderLayout());
        // 새로 추가한 컨테이너에 기존의 컨테이너를 올려주자.
        pg.add(north2, BorderLayout.NORTH);
        pg.add(jScrollPane, BorderLayout.CENTER);
        pg.add(south, BorderLayout.SOUTH);

        // 3. 컨테이너를 프레임에 올려주어야 한다.
//        add(north1, BorderLayout.NORTH);
        add(pg0, BorderLayout.NORTH);
        add(pg, BorderLayout.CENTER);

        // Default Config SET
        setBounds(300, 400, 700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // 매크로 살행
        bounce();
    }

    /**
     * 버튼 클릭 시 이벤트리스너
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // date
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] dateTime = format.format(date).split(" ");
        String logTime = dateTime[0] + " " + dateTime[1];

        //이벤트가 발생한 버튼에 설정된 action command 읽어오기
        String command = e.getActionCommand();
        if (command.equals("start")) {
            if (ON_OFF) {
                return;
            }

            //JTextField에 입력한 문자열 읽어오기
            String start = startTime.getText();
            String end = endTime.getText();

            // 시간정합성 체크
            if (!isNumericTime(start) || !isNumericTime(end)) {
                JOptionPane.showMessageDialog(this, "0000 ~ 2359 시간범위로만 설정할 수 있습니다.");
                setLog("[" + logTime + "] 오마키 동작시간 설정오류!");
                return;
            }
            START_TIME = Integer.parseInt(startTime.getText());
            END_TIME = Integer.parseInt(endTime.getText());

            if (!isNumericBounce(bounceTime.getText())) {
                JOptionPane.showMessageDialog(this, "5 ~ 60 간격범위로만 설정할 수 있습니다.");
                setLog("[" + logTime + "] 오마키 동작간격 설정오류!");
                return;
            }
            BOUNCE = Integer.parseInt(bounceTime.getText()) * 1000;

            ON_OFF = true;
            setLog("[" + logTime + "] 오마키 시작! STATE[" + ON_OFF + "]");
            setLog("[" + logTime + "] 동작시간범위 = " + START_TIME + " ~ " + END_TIME);
            setLog("[" + logTime + "] 동작간격 = " + bounceTime.getText() + "초 마다 실행");
            setLog("[" + logTime + "] 마우스가동범위 = (FIX) 1920x1080 랜덤");
            setLog("[" + logTime + "] 키보드 입력키 = " + pressKey.getText());
        } else if (command.equals("stop")) {
            if (!ON_OFF) {
                return;
            }

            ON_OFF = false;
            BOUNCE = 10000;
            // date
            setLog("[" + logTime + "] 오마키 정지! STATE[" + ON_OFF + "]");
            setLog("");
        }
        jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());
    }

    /**
     * 오토 마우스 키보드 매크로
     */
    public void bounce() {
        try {
            Robot robot = new Robot();
            int x, y;
            while (true) { // 종료할 때 까지 무한 반복
                if (ON_OFF) {
                    // date
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String[] dateTime = format.format(date).split(" ");
                    String logTime = dateTime[0] + " " + dateTime[1];

                    String log;
                    if (isOnTime(dateTime[1].replace(":", ""))) {
                        x = (int) (Math.random() * 1920); // 1920*1080 해상도 내의 x,y 값을 랜덤하게 생성
                        y = (int) (Math.random() * 1080);
                        robot.mouseMove(x, y); // x,y 좌표로 이동

                        // key 입력
                        robot.keyPress(KEY_CODE);
                        robot.keyRelease(KEY_CODE);

                        log = "[" + logTime + "] 반복간격 = [" + BOUNCE / 1000 + "초]" + " :: 입력키 = [" + KeyEvent.getKeyText(KEY_CODE) + "]" + " :: 마우스좌표 = X[" + x + "] Y[" + y + "]";
                    } else {
                        log = "[" + logTime + "] 오마키가 동작중이나 가동시간범위가 아닙니다.";
                    }

                    setLog(log);
                    jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());
                }

                Thread.sleep(BOUNCE); // 10초 대기 후 다음 좌표로 이동
            }
        } catch (AWTException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 지정시간 정합성체크
     */
    private boolean isNumericTime(String str) {
        if (str.length() != 4) {
            return false;
        }

        if (str.matches("[+-]?\\d*(\\.\\d+)?")) {
            int time = Integer.parseInt(str);
            return 0 <= time && 2400 > time;
        } else {
            return false;
        }
    }

    /**
     * 동작간격 정합성체크
     */
    private boolean isNumericBounce(String str) {
        if (str.length() > 2 || str.isEmpty()) {
            return false;
        }

        if (str.matches("[+-]?\\d*(\\.\\d+)?")) {
            int bounce = Integer.parseInt(str);
            return 5 <= bounce && 60 >= bounce;
        } else {
            return false;
        }
    }

    /**
     * 메모리 관리를위한 로그 관리
     */
    private void setLog(String log) {
        if (model.size() == 20) {
            model.remove(0);
        }
        model.addElement(log);
    }

    /**
     * 설정 된 가동시간 범위인지 체크
     */
    private boolean isOnTime(String time) {
        Integer checkTime = Integer.valueOf(time.substring(0, 4));
        return START_TIME <= checkTime && END_TIME >= checkTime;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    /**
     * 키 입력 후 땟을 때 이벤트리스너
     */
    @Override
    public void keyReleased(KeyEvent e) {
//        setLog("[keyReleased] keyCode = [" + e.getKeyCode() + "] :: keyName = [" + KeyEvent.getKeyText(e.getKeyCode()) + "]");
        setLog("[keyReleased] keyCode = [" + e.getKeyCode() + "] :: keyName = [" + KeyEvent.getKeyText(e.getKeyCode()) + "]");
        pressKey.setText(KeyEvent.getKeyText(e.getKeyCode()));
        KEY_CODE = e.getKeyCode();
    }
}
