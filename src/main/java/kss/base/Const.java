package kss.base;
/*
 * Korean Sentence Splitter
 * Split Korean text into sentences using heuristic algorithm.
 *
 * Copyright (C) 2021 Sang-ji Lee <tkdwl06@gmail.com>
 * Copyright (C) 2021 Hyun-woong Ko <kevin.woong@tunib.ai> and Sang-Kil Park <skpark1224@hyundai.com>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Const {

    static List<String> numbersArr = Arrays
        .asList(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
        );
    public static final ArrayList<String> numbers = new ArrayList<>(numbersArr);

    static List<String> alphabetArr = Arrays
        .asList(
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
            "z"
        );
    public static final ArrayList<String> alphabets = new ArrayList<>(alphabetArr);

    static List<String> bracketArr = Arrays
        .asList(
            ")", "）", "〉", "》", "]", "］", "〕", "】", "}", "｝", "』", "」",
            "(", "（", "〈", "《", "[", "［", "〔", "【", "{", "｛", "「", "『"
        );
    public static final ArrayList<String> bracket = new ArrayList<>(bracketArr);

    static List<String> punctuationArr = Arrays
        .asList(";", ".", "?", "!", "~", "…");
    public static final ArrayList<String> punctuation = new ArrayList<>(punctuationArr);

    static List<String> doubleQuotesArr = Arrays
        .asList("\"", "“", "”");
    public static final ArrayList<String> doubleQuotes = new ArrayList<>(doubleQuotesArr);

    static List<String> singleQuotesArr = Arrays
        .asList("'", "‘", "’");
    public static final ArrayList<String> singleQuotes = new ArrayList<>(singleQuotesArr);

    public static HashMap<String, String> doubleQuotesOpenToClose = new HashMap<String, String>() {{
        put("“", "”");
        put("\"", "\"");
    }};
    public static HashMap<String, String> doubleQuotesCloseToOpen = new HashMap<String, String>() {{
        put("”", "“");
        put("\"", "\"");
    }};
    public static HashMap<String, String> singleQuotesOpenToClose = new HashMap<String, String>() {{
        put("‘", "’");
        put("'", "'");
    }};
    public static HashMap<String, String> singleQuotesCloseToOpen = new HashMap<String, String>() {{
        put("’", "‘");
        put("'", "'");
    }};
    public static HashMap<String, String> bracketOpenToClose = new HashMap<String, String>() {{
        put("(", ")");
        put("（", "）");
        put("〈", "〉");
        put("《", "》");
        put("[", "]");
        put("［", "］");
        put("〔", "〕");
        put("【", "】");
        put("{", "}");
        put("｛", "｝");
        put("「", "」");
        put("『", "』");
    }};
    public static HashMap<String, String> bracketCloseToOpen = new HashMap<String, String>() {{
        put(")", "(");
        put("）", "（");
        put("〉", "〈");
        put("》", "《");
        put("]", "[");
        put("］", "［");
        put("〕", "〔");
        put("】", "【");
        put("}", "{");
        put("｝", "｛");
        put("」", "「");
        put("』", "『");
    }};


    public static final ArrayList<String> lowerAlphabets = alphabets;
    public static ArrayList<String> upperAlphabets = setUpperAlphabets();
    public static ArrayList<String> special = setSpecial();

    private static ArrayList<String> setUpperAlphabets() {
        upperAlphabets = new ArrayList<>();
        for (String s : alphabets) {
            upperAlphabets.add(s.toUpperCase());
        }
        return upperAlphabets;
    }

    private static ArrayList<String> setSpecial() {
        special = new ArrayList<>();
        special.addAll(punctuation);
        special.addAll(bracket);
        return special;
    }
}
