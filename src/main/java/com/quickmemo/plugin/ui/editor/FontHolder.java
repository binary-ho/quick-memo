package com.quickmemo.plugin.ui.editor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.util.ui.UIUtil;

import java.awt.*;

public class FontHolder {
    private static final Font DEFAULT_GLOBAL = new Font(Font.MONOSPACED, Font.PLAIN, 14);
    public static final String DEFAULT_KOREAN_FONT = Font.DIALOG;
    private static final String[] KOREAN_FONTS = {
            // for macOS
            "Apple SD GothicNeo Neo",
            "AppleSDGothicNeo-Regular",

            // for Window guys
            "Malgun Gothic",

            // Linux
            "NotoSansCJK-Regular",
            "Noto Sans CJK KR"
    };

    public static final char VALIDATE_KOREAN_CHAR = '한';

    public Font getEditorFont() {
        Font editorFont = getIDEFont();

        // find korean for korean users..
        String koreanFont = findKoreanFont();

        String fontFamily = editorFont.getFamily() + ", " + koreanFont;
        return Font.decode(fontFamily + "-PLAIN-" + editorFont.getSize());
    }

    private String findKoreanFont() {
        if (KOREAN_FONTS == null) {
            return DEFAULT_KOREAN_FONT;
        }

        for (String koreanFont : KOREAN_FONTS) {
            try {
                if (canDisplay(koreanFont)) {
                    return koreanFont;
                }
            } catch (Exception e) {
                return getDefaultKoreanFont();
            }
        }

        return getDefaultKoreanFont();
    }

    private String getDefaultKoreanFont() {
        try {
            Font ideFont = UIUtil.getFontWithFallback(DEFAULT_KOREAN_FONT, Font.PLAIN, 1);
            if (ideFont.canDisplay(VALIDATE_KOREAN_CHAR)) {
                return ideFont.getFamily();
            } else {
                return DEFAULT_KOREAN_FONT;
            }
        } catch (Exception e) {
            return DEFAULT_KOREAN_FONT;
        }
    }

    private boolean canDisplay(String koreanFont) {
        Font font = new Font(koreanFont, Font.PLAIN, 1);
        return font.canDisplay(VALIDATE_KOREAN_CHAR);
    }

    private Font getIDEFont() {
        if (ApplicationManager.getApplication() == null) {
            return DEFAULT_GLOBAL;
        }

        try {
            return getGlobalFont();
        } catch (Exception e) {
            return DEFAULT_GLOBAL;
        }
    }

    private Font getGlobalFont() {
        EditorColorsManager colorsManager = EditorColorsManager.getInstance();
        if (colorsManager == null) {
            return DEFAULT_GLOBAL;
        }
        EditorColorsScheme globalScheme = colorsManager.getGlobalScheme();
        return globalScheme.getFont(EditorFontType.PLAIN);
    }
}
