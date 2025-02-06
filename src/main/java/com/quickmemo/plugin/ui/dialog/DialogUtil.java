package com.quickmemo.plugin.ui.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;

import javax.swing.*;
import java.awt.*;

public class DialogUtil {
    private static final String TOOL_WINDOW_ID = "QuickMemo";

    public static void showInfo(Project project, String message, String title) {
        centerDialogAndShow(project, () -> Messages.showInfoMessage(message, title));
    }

    public static void showError(Project project, String message, String title) {
        centerDialogAndShow(project, () -> Messages.showErrorDialog(message, title));
    }

    public static boolean showYesNoDialog(Project project, String message, String title, String yesText, String noText) {
        centerDialogOnToolWindow(project);
        return Messages.showYesNoDialog(message, title, yesText, noText, Messages.getQuestionIcon()) == Messages.YES;
    }

    private static void centerDialogAndShow(Project project, Runnable showDialog) {
        centerDialogOnToolWindow(project);
        showDialog.run();
    }

    private static void centerDialogOnToolWindow(Project project) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(TOOL_WINDOW_ID);
        if (toolWindow != null && toolWindow.isVisible()) {
            Content content = toolWindow.getContentManager().getSelectedContent();
            if (content != null) {
                JComponent component = content.getComponent();
                Window window = SwingUtilities.getWindowAncestor(component);
                
                if (window != null) {
                    // 다이얼로그가 표시될 때 위치를 조정하기 위해 AWT 이벤트 큐를 사용
                    EventQueue.invokeLater(() -> {
                        Window activeWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
                        if (activeWindow != null && activeWindow != window) {
                            Rectangle bounds = window.getBounds();
                            Point location = window.getLocationOnScreen();
                            
                            // 툴 윈도우의 중앙 좌표 계산
                            int centerX = location.x + (bounds.width / 2);
                            int centerY = location.y + (bounds.height / 2);
                            
                            // 다이얼로그 크기의 절반만큼 오프셋
                            activeWindow.pack(); // 다이얼로그 크기 계산을 위해 필요
                            int dialogWidth = activeWindow.getWidth();
                            int dialogHeight = activeWindow.getHeight();
                            
                            // 최종 위치 설정
                            activeWindow.setLocation(
                                centerX - (dialogWidth / 2),
                                centerY - (dialogHeight / 2)
                            );
                            
                            // 툴 윈도우 영역 내에 있는지 확인하고 조정
                            if (activeWindow.getX() < location.x) {
                                activeWindow.setLocation(location.x, activeWindow.getY());
                            }
                            if (activeWindow.getY() < location.y) {
                                activeWindow.setLocation(activeWindow.getX(), location.y);
                            }
                            if (activeWindow.getX() + dialogWidth > location.x + bounds.width) {
                                activeWindow.setLocation(location.x + bounds.width - dialogWidth, activeWindow.getY());
                            }
                            if (activeWindow.getY() + dialogHeight > location.y + bounds.height) {
                                activeWindow.setLocation(activeWindow.getX(), location.y + bounds.height - dialogHeight);
                            }
                        }
                    });
                }
            }
        }
    }
}
