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

import static kss.base.Const.doubleQuotes;
import static kss.base.Const.lowerAlphabets;
import static kss.base.Const.numbers;
import static kss.base.Const.singleQuotes;
import static kss.base.Const.upperAlphabets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackupManager {

    private final Map<String, String> backupDict = new HashMap<>();

    public BackupManager() {
        for (String s : getData()) {
            this.backupDict.put(s, String.valueOf(Math.abs(s.hashCode())));
        }
    }

    public List<String> getData() {
        List<String> faces = Arrays.asList(":)", ":(", ":'(", "O:)", "&)", ">:(", "3:)", "<(\")");
        List<String> lowUpperNum = lowerAlphabets;
        lowUpperNum.addAll(upperAlphabets);

        List<String> apostrophe = new ArrayList<>();
        for (String i : lowUpperNum) {
            for (String j : lowUpperNum) {
                apostrophe.add(String.format("%s'%s", i, j));
            }
        }

        List<String> years = new ArrayList<>();
        for (String i : numbers) {
            years.add(String.format("%s's", i));
            years.add(String.format("%s'S", i));
        }

        List<String> time = new ArrayList<>();
        for (String i : numbers) {
            for (String j : numbers) {
                for (String k : singleQuotes) {
                    time.add(String.format("%s%s%s", i, j, k));
                }
            }
        }

        List<String> inch = new ArrayList<>();
        List<String> numersAdd = numbers;
        numersAdd.add(".");
        for (String i : numersAdd) {
            for (String j : numbers) {
                for (String k : doubleQuotes) {
                    inch.add(String.format("%s%s%s", i, j, k));
                }
            }
        }

        List<String> ecCases = Arrays.asList(
            "쌓이다",
            "보이다",
            "먹이다",
            "죽이다",
            "끼이다",
            "트이다",
            "까이다",
            "꼬이다",
            "데이다",
            "치이다",
            "쬐이다",
            "꺾이다",
            "낚이다",
            "녹이다",
            "벌이다",
            "다 적발",
            "다 말하",
            "다 말한",
            "다 말했",
            "다 밝혀",
            "다 밝혔",
            "다 밝히",
            "다 밝힌",
            "다 주장",
            "요 라고",
            "요. 라고",
            "죠 라고",
            "죠. 라고",
            "다 라고",
            "다. 라고",
            "다 하여",
            "다 거나",
            "다. 거나",
            "다 시피",
            "다. 시피",
            "다 응답",
            "다 로 응답",
            "다. 로 응답",
            "요 로 응답",
            "요. 로 응답",
            "죠 로 응답",
            "죠. 로 응답",
            "다 에서",
            "다. 에서",
            "요 에서",
            "요. 에서",
            "죠 에서",
            "죠. 에서",
            "타다 금지법",
            "다 온 사실",
            "다 온 것",
            "다 온 사람",
            "다 왔다",
            "다 왔더",
            "다 와보",
            "우간다",
            "사이다");

        List<String> data = new ArrayList<>();
        data.addAll(faces);
        data.addAll(apostrophe);
        data.addAll(years);
        data.addAll(ecCases);
        data.addAll(time);
        data.addAll(inch);

        return data;
    }

    public String process(String text, Map<String, String> purposeDict) {
        for (Map.Entry<String, String> entry : purposeDict.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        return text.strip();
    }

    public void addItem2Dict(String key, String value) {
        this.backupDict.put(key, value);
    }

    public String backup(String text) {
        return process(text, backupDict);
    }

    public String restore(String text) {
        Map<String, String> purposeDict = new HashMap<>();
        for (Map.Entry<String, String> entry : backupDict.entrySet()) {
            purposeDict.put(entry.getValue(), entry.getKey());
        }
        return process(text, purposeDict);
    }
}
