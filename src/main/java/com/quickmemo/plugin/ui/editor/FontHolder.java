package com.quickmemo.plugin.ui.editor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.EditorFontType;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class FontHolder {
    private static final Font DEFAULT_GLOBAL = new Font(Font.MONOSPACED, Font.PLAIN, 14);
    private static final List<String> KOREAN_FONTS = List.of(
            // for macOS
            "Apple SD GothicNeo Neo",
            "AppleSDGothicNeo-Regular",

            // for Window guys
            "D2Coding",
            "맑은 고딕",

            // Linux
            "NotoSansCJK-Regular",
            "Noto Sans CJK KR"
    );

    public static final char VALIDATE_KOREAN_CHAR = '한';

    public Font getEditorFont() {
        Font editorFont = getIDEFont();

        // find korean for korean users..
        String koreanFont = getKoreanFont();
        String fontFamily = editorFont.getFamily() + ", " + koreanFont;
        return Font.decode(fontFamily + "-PLAIN-" + editorFont.getSize());
    }

    private String getKoreanFont() {
        String koreanFont =  findKoreanFont();
        System.out.println("[DEBUG] Korean font: " + koreanFont);
        if (koreanFont != null) {
            return koreanFont;
        }
        return DEFAULT_GLOBAL.getFamily();
    }

    private String findKoreanFont() {
        if (KOREAN_FONTS == null) {
            return DEFAULT_GLOBAL.getFamily();
        }

        String installedKoreanFont = findInstalledKoreanFont();
        if (installedKoreanFont != null) {
            return installedKoreanFont;
        }
        return DEFAULT_GLOBAL.getFamily();
    }

    private String findInstalledKoreanFont() {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] availableFontFamilyNames = graphicsEnvironment.getAvailableFontFamilyNames();
        List<String> installedFonts = Arrays.asList(availableFontFamilyNames);
        return installedFonts.stream()
                .filter(KOREAN_FONTS::contains)
                .filter(this::canDisplay)
                .findFirst()
                .orElse(getGlobalFont().getFamily());
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

    private boolean canDisplay(String koreanFont) {
        Font font = new Font(koreanFont, Font.PLAIN, 1);
        return font.canDisplay(VALIDATE_KOREAN_CHAR);
    }
}
