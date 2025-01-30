package com.quickmemo.plugin.ui;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.intellij.notification.impl.NotificationsManagerImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

public class ToastPopup extends JWindow {
    private static final int POPUP_WIDTH = 80;
    private static final int POPUP_HEIGHT = 28;  // 높이를 더 줄임
    private static final int ROUND_RECT_ARC = 10;
    private static final int ARROW_SIZE = 8;
    private static final float OPACITY_START = 0.9f;
    private static final float OPACITY_END = 0.0f;
    private static final int FADE_REFRESH_RATE = 25;
    private static final int FADE_DURATION = 1000;
    private static final int DISPLAY_DURATION = 500;
    
    private final Timer fadeTimer;
    private float opacity = OPACITY_START;

    public ToastPopup(String message, Component parent) {
        setLayout(null);  // 절대 위치 사용
        setSize(POPUP_WIDTH, POPUP_HEIGHT + ARROW_SIZE);
        setAlwaysOnTop(true);
        
        // 메시지 레이블
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setForeground(JBColor.foreground());
        label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));  // 볼드 처리
        
        // 레이블 크기와 위치 직접 설정
        Dimension labelSize = label.getPreferredSize();
        label.setBounds(
            (POPUP_WIDTH - labelSize.width) / 2,  // 가로 중앙
            (POPUP_HEIGHT - labelSize.height) / 2 - 1,  // 세로 중앙에서 살짝 위로
            labelSize.width,
            labelSize.height
        );
        
        add(label);

        // 위치 계산
        Point parentLocation = parent.getLocationOnScreen();
        int x = parentLocation.x - (POPUP_WIDTH / 2);
        int y = parentLocation.y - POPUP_HEIGHT - 15;
        setLocation(x, y);

        // 윈도우 shape 설정
        Path2D shape = new Path2D.Float();
        shape.append(new RoundRectangle2D.Float(0, 0, POPUP_WIDTH, POPUP_HEIGHT, ROUND_RECT_ARC, ROUND_RECT_ARC), false);
        shape.moveTo(POPUP_WIDTH/2 - 5, POPUP_HEIGHT);
        shape.lineTo(POPUP_WIDTH/2, POPUP_HEIGHT + ARROW_SIZE);
        shape.lineTo(POPUP_WIDTH/2 + 5, POPUP_HEIGHT);
        shape.closePath();
        setShape(shape);

        // 페이드 아웃 타이머
        ActionListener fadeActionListener = e -> {
            opacity -= (OPACITY_START - OPACITY_END) / ((float) FADE_DURATION / FADE_REFRESH_RATE);
            if (opacity <= OPACITY_END) {
                opacity = OPACITY_END;
                ((Timer) e.getSource()).stop();
                dispose();
            }
            setOpacity(opacity);
        };
        
        fadeTimer = new Timer(FADE_REFRESH_RATE, fadeActionListener);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Path2D path = new Path2D.Float();
        
        // 메인 둥근 사각형
        RoundRectangle2D roundRect = new RoundRectangle2D.Float(
            0, 0, POPUP_WIDTH, POPUP_HEIGHT, ROUND_RECT_ARC, ROUND_RECT_ARC
        );
        path.append(roundRect, false);
        
        // 화살표
        path.moveTo(POPUP_WIDTH/2 - 5, POPUP_HEIGHT);
        path.lineTo(POPUP_WIDTH/2, POPUP_HEIGHT + ARROW_SIZE);
        path.lineTo(POPUP_WIDTH/2 + 5, POPUP_HEIGHT);
        
        // 배경 그리기
        g2.setColor(NotificationsManagerImpl.FILL_COLOR);
        g2.fill(path);
        
        super.paint(g);
    }

    public void showToast() {
        setOpacity(OPACITY_START);
        setVisible(true);
        
        // 페이드 아웃 시작 전 지연
        Timer displayTimer = new Timer(DISPLAY_DURATION, e -> fadeTimer.start());
        displayTimer.setRepeats(false);
        displayTimer.start();
    }
}
