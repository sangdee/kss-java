package kss.core;
/**
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

import static kss.base.Base.doPushPopSymbol;
import static kss.base.Base.doTrimSentPushResults;
import static kss.base.Const.bracket;
import static kss.base.Const.bracketCloseToOpen;
import static kss.base.Const.bracketOpenToClose;
import static kss.base.Const.doubleQuotes;
import static kss.base.Const.doubleQuotesCloseToOpen;
import static kss.base.Const.doubleQuotesOpenToClose;
import static kss.base.Const.punctuation;
import static kss.base.Const.singleQuotes;
import static kss.base.Const.singleQuotesCloseToOpen;
import static kss.base.Const.singleQuotesOpenToClose;
import static kss.rule.Rule.commonValue;
import static kss.rule.Rule.postProcessingDa;
import static kss.rule.Rule.postProcessingHam;
import static kss.rule.Rule.postProcessingJyo;
import static kss.rule.Rule.postProcessingUm;
import static kss.rule.Rule.postProcessingYo;
import static kss.rule.Rule.table;
import static kss.util.IntToBool.intToBool;

import kss.base.BackupManager;
import kss.base.ChunkWithIndex;
import kss.base.SentenceIndex;
import kss.base.enumerate.Id;
import kss.base.enumerate.Stats;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Backend {

    public List<String> realignByQuote(
        String text,
        int lastQuotePos,
        String quoteType,
        boolean useHeuristic,
        boolean useQuotesBracketsProcessing,
        int maxRecoverStep,
        int maxRecoverLength,
        int recoverStep
    ) {

        List<String> beforeQuote =
            splitSentences(
                text.substring(0, lastQuotePos),
                useHeuristic,
                useQuotesBracketsProcessing,
                maxRecoverStep,
                maxRecoverLength,
                recoverStep,
                false
            );

        String beforeLast = (beforeQuote.size() > 0) ? beforeQuote.get(beforeQuote.size() - 1) : "";
        beforeQuote = (beforeQuote.size() == 1) ? new ArrayList<>()
            : beforeQuote.subList(0, beforeQuote.size() - 1);

        List<String> afterQuote = splitSentences(
            text.substring(lastQuotePos + 1),
            useHeuristic,
            useQuotesBracketsProcessing,
            maxRecoverStep,
            maxRecoverLength,
            recoverStep,
            false
        );

        String afterFirst = (afterQuote.size() > 0) ? afterQuote.get(0) : "";

        afterQuote = (afterQuote.size() == 1)
            ? new ArrayList<>() : afterQuote.subList(1, afterQuote.size());

        List<String> middleQuote = new ArrayList<>();
        middleQuote.add(beforeLast + quoteType + afterFirst);
        List<String> results = new ArrayList<>();

        results.addAll(beforeQuote);
        results.addAll(middleQuote);
        results.addAll(afterQuote);

        return results;
    }

    public List<String> lindexSplit(String text, List<Integer> indices) {
        List<Integer> args = new ArrayList<>();
        args.add(0);

        for (Integer data : indices) {
            args.add(data + 1);
        }
        args.add(text.length() + 1);

        List<List<Integer>> zipped = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            if (i != args.size() - 1) {
                List<Integer> newList = new ArrayList<>();
                newList.add(args.get(i));
                newList.add(args.get(i + 1));
                zipped.add(newList);

            }
        }
        List<String> newList = new ArrayList<>();
        for (List<Integer> zip : zipped) {
            newList.add(text.substring(zip.get(0), zip.get(1) - 1));
        }
        return newList;
    }

    public List<Integer> findAll(String aStr, String sub) {
        int start = 0;
        List<Integer> output = new ArrayList<>();
        while (true) {
            start = aStr.indexOf(sub, start);
            if (start == -1) {
                break;
            }
            output.add(start + sub.length());
            start += sub.length();
        }
        return output;

    }

    public List<String> postProcessing(List<String> results, List<String> postProcessingList) {
        List<String> finalResults = new ArrayList<>();
        for (String res : results) {
            List<Integer> splitIdx = new ArrayList<>();
            List<String> qoutes = new ArrayList<>();
            boolean findQuotes = false;
            qoutes.addAll(singleQuotes);
            qoutes.addAll(doubleQuotes);
            qoutes.addAll(bracket);

            for (String qt : qoutes) {
                if (res.contains(qt)) {
                    findQuotes = true;
                    break;
                }
            }
            if (!findQuotes) {
                for (String post : postProcessingList) {
                    if (res.contains(post)) {
                        splitIdx.addAll(findAll(res, post + 1));
                    }
                }
            }
            Collections.sort(splitIdx);
            finalResults.addAll(lindexSplit(res, splitIdx));
        }
        return finalResults;
    }

    ArrayList<String> endPoint = setEndPoint();
    ArrayList<String> needToReplaceZwsp = setNeedToReplaceZwsp();

    private ArrayList<String> setEndPoint() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(singleQuotes);
        list.addAll(doubleQuotes);
        list.addAll(bracket);
        list.addAll(punctuation);
        list.add(" ");
        list.add("");
        list.addAll(commonValue.keySet());
        return list;
    }

    private ArrayList<String> setNeedToReplaceZwsp() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(singleQuotes);
        list.addAll(doubleQuotes);
        list.addAll(bracket);
        return list;
    }

    public ArrayList<String> splitSentences(
        String text,
        boolean useHeuristic,
        boolean useQuotesBracketsProcessing,
        int maxRecoverStep,
        int maxRecoverLength,
        int recoverStep,
        boolean useStrip
    ) {

        if (text.length() > maxRecoverLength) {
            maxRecoverStep = 0;
        }

        text = text.replace("\u200b", "");
        BackupManager backupManager = new BackupManager();

        LinkedList<String> doubleQuoteStack = new LinkedList<>();
        LinkedList<String> singleQuoteStack = new LinkedList<>();
        LinkedList<String> bracketStack = new LinkedList<>();
        List<String> tests = Arrays.asList("다", "요", "죠", "함", "음");

        for (int i = 0; i < text.length(); i++) {
            String ch = Character.toString(text.charAt(i));
            if (tests.contains(ch)) {
                if (i != text.length() - 1) {
                    if (!endPoint.contains(Character.toString(text.charAt(i + 1)))) {
                        String targetToBackup = ch + text.charAt(i + 1);
                        backupManager.addItem2Dict(
                            targetToBackup,
                            String.valueOf(Math.abs(targetToBackup.hashCode()))
                        );
                    }
                }
            }
        }

        text = backupManager.backup(text);

        for (String s : needToReplaceZwsp) {
            text = text.replace(s, String.format("\u200b%s\u200b", s));
        }

        String prev = "";
        String curSentence = "";
        List<String> results = new ArrayList<>();
        int curStat = Stats.DEFAULT.getValue();

        int lastSingleQuotePos = 0;
        int lastDoubleQuotePos = 0;
        int lastBracketPos = 0;

        String singleQuotePop = "'";
        String doubleQuotePop = "\"";
        String bracketPoP = " ";

        for (int i = 0; i < text.length(); i++) {
            List<String> code = Arrays.asList(".", "!", "?");
            String ch = Character.toString(text.charAt(i));

            if (curStat == Stats.DEFAULT.getValue()) {
                if (doubleQuotes.contains(ch)) {
                    if (useQuotesBracketsProcessing) {
                        if (doubleQuotesOpenToClose.containsKey(ch)) {
                            doubleQuotePop = doPushPopSymbol(
                                doubleQuoteStack,
                                doubleQuotesOpenToClose.get(ch),
                                ch
                            );
                        } else {
                            doubleQuotePop = doPushPopSymbol(
                                doubleQuoteStack,
                                doubleQuotesCloseToOpen.get(ch),
                                ch
                            );
                        }
                        lastDoubleQuotePos = i;
                    }
                } else if (singleQuotes.contains(ch)) {
                    if (useQuotesBracketsProcessing) {
                        if (singleQuotesOpenToClose.containsKey(ch)) {
                            singleQuotePop = doPushPopSymbol(
                                singleQuoteStack,
                                singleQuotesOpenToClose.get(ch),
                                ch
                            );
                        } else {
                            singleQuotePop = doPushPopSymbol(
                                singleQuoteStack,
                                singleQuotesCloseToOpen.get(ch),
                                ch
                            );
                        }
                        lastSingleQuotePos = i;
                    }
                } else if (bracket.contains(ch)) {
                    if (useQuotesBracketsProcessing) {
                        if (bracketOpenToClose.containsKey(ch)) {
                            bracketPoP = doPushPopSymbol(
                                bracketStack,
                                bracketOpenToClose.get(ch),
                                ch
                            );
                        } else {
                            bracketPoP = doPushPopSymbol(
                                bracketStack,
                                bracketCloseToOpen.get(ch),
                                ch
                            );
                        }
                        lastBracketPos = i;
                    }
                } else if (code.contains(ch)) {
                    if (doubleQuoteStack.isEmpty()
                        && singleQuoteStack.isEmpty()
                        && bracketStack.isEmpty()
                        && intToBool(table.get(Stats.SB.getValue()).getOrDefault(prev, 0)
                        & Id.PREV.getValue())) {
                        curStat = Stats.SB.getValue();
                    }
                }

                if (useHeuristic) {
                    if (ch.equals("다")) {

                        if (doubleQuoteStack.isEmpty()
                            && singleQuoteStack.isEmpty()
                            && bracketStack.isEmpty()
                            && intToBool(
                            table.get(Stats.DA.getValue()).getOrDefault(prev, 0) & Id.PREV
                                .getValue())) {
                            curStat = Stats.DA.getValue();
                        }
                    }

                    if (ch.equals("요")) {
                        if (doubleQuoteStack.isEmpty()
                            && singleQuoteStack.isEmpty()
                            && bracketStack.isEmpty()
                            && intToBool(table.get(Stats.YO.getValue()).getOrDefault(prev, 0)
                            & Id.PREV.getValue())) {
                            curStat = Stats.YO.getValue();
                        }
                    }
                    if (ch.equals("죠")) {
                        if (doubleQuoteStack.isEmpty()
                            && singleQuoteStack.isEmpty()
                            && bracketStack.isEmpty()
                            && intToBool(table.get(Stats.JYO.getValue()).getOrDefault(prev, 0)
                            & Id.PREV.getValue())) {
                            curStat = Stats.JYO.getValue();
                        }
                    }
                    if (ch.equals("함")) {
                        if (doubleQuoteStack.isEmpty()
                            && singleQuoteStack.isEmpty()
                            && bracketStack.isEmpty()
                            && intToBool(table.get(Stats.HAM.getValue()).getOrDefault(prev, 0)
                            & Id.PREV.getValue())) {
                            curStat = Stats.HAM.getValue();
                        }
                    }
                    if (ch.equals("음")) {
                        if (doubleQuoteStack.isEmpty()
                            && singleQuoteStack.isEmpty()
                            && bracketStack.isEmpty()
                            && intToBool(table.get(Stats.UM.getValue()).getOrDefault(prev, 0)
                            & Id.PREV.getValue())) {
                            curStat = Stats.UM.getValue();
                        }
                    }
                }
            } else {
                if (doubleQuotes.contains(ch)) {
                    lastDoubleQuotePos = i;
                } else if (singleQuotes
                    .contains(ch)) {
                    lastSingleQuotePos = i;
                } else if (bracket.contains(ch)) {
                    lastBracketPos = i;
                }

                boolean endIf = false;
                if (!endIf) {
                    if (ch.equals(" ")
                        || (intToBool(table.get(Stats.COMMON.getValue())
                        .getOrDefault(ch, 0) & Id.CONT.getValue()))) {

                        if (intToBool(table.get(curStat)
                            .getOrDefault(prev, 0) & Id.NEXT1.getValue())) {

                            curSentence = doTrimSentPushResults(
                                curSentence,
                                results
                            );

                            curSentence += prev;
                            curStat = Stats.DEFAULT.getValue();
                        }
                        endIf = true;
                    }
                }
                if (!endIf) {
                    if (intToBool(table.get(curStat)
                        .getOrDefault(ch, 0) & Id.NEXT
                        .getValue())) {

                        if (intToBool(table.get(curStat).getOrDefault(prev, 0) & Id.NEXT1
                            .getValue())) {
                            curSentence += prev;
                        }
                        curStat = Stats.DEFAULT.getValue();
                        endIf = true;
                    }
                }
                if (!endIf) {
                    if (intToBool(table.get(curStat)
                        .getOrDefault(ch, 0) & Id.NEXT1
                        .getValue())) {

                        if (intToBool(table.get(curStat).getOrDefault(prev, 0)
                            & Id.NEXT1.getValue())) {

                            curSentence = doTrimSentPushResults(
                                curSentence,
                                results
                            );

                            curSentence += prev;
                            curStat = Stats.DEFAULT.getValue();
                        }
                        endIf = true;
                    }
                }

                if (!endIf) {
                    if (intToBool(table.get(curStat)
                        .getOrDefault(ch, 0)
                        & Id.NEXT2
                        .getValue())) {
                        if (intToBool(table.get(curStat).getOrDefault(prev, 0) & Id.NEXT1
                            .getValue())) {
                            curSentence += prev;
                        } else {
                            curSentence = doTrimSentPushResults(curSentence,
                                results);
                        }
                        curStat = Stats.DEFAULT.getValue();
                        endIf = true;
                    }
                }
                if (!endIf) {
                    if (!intToBool(table.get(curStat).getOrDefault(ch, 0))
                        || intToBool(table.get(curStat).getOrDefault(ch, 0) & Id.PREV.getValue())) {

                        curSentence = doTrimSentPushResults(
                            curSentence,
                            results
                        );

                        if (intToBool(table.get(curStat).getOrDefault(prev, 0)
                            & Id.NEXT1.getValue())) {
                            curSentence += prev;
                        }

                        curStat = Stats.DEFAULT.getValue();

                        if (bracket.contains(ch)) {
                            if (useQuotesBracketsProcessing) {
                                if (bracketOpenToClose.containsKey(ch)) {

                                    bracketPoP = doPushPopSymbol(
                                        bracketStack,
                                        bracketOpenToClose.get(ch),
                                        ch
                                    );

                                } else {
                                    bracketPoP = doPushPopSymbol(
                                        bracketStack,
                                        bracketCloseToOpen.get(ch),
                                        ch
                                    );
                                }
                                lastBracketPos = i;
                            }
                        } else if (doubleQuotes.contains(ch)) {
                            if (useQuotesBracketsProcessing) {
                                if (doubleQuotesOpenToClose.containsKey(ch)) {
                                    doubleQuotePop = doPushPopSymbol(
                                        doubleQuoteStack,
                                        doubleQuotesOpenToClose.get(ch),
                                        ch
                                    );
                                } else {
                                    doubleQuotePop = doPushPopSymbol(
                                        doubleQuoteStack,
                                        doubleQuotesCloseToOpen.get(ch),
                                        ch
                                    );
                                }
                                lastDoubleQuotePos = i;
                            }
                        } else if (singleQuotes.contains(ch)) {
                            if (useQuotesBracketsProcessing) {
                                if (singleQuotesOpenToClose.containsKey(ch)) {
                                    singleQuotePop = doPushPopSymbol(
                                        singleQuoteStack,
                                        singleQuotesOpenToClose.get(ch),
                                        ch
                                    );
                                } else {
                                    singleQuotePop = doPushPopSymbol(
                                        singleQuoteStack,
                                        singleQuotesCloseToOpen.get(ch),
                                        ch
                                    );
                                }
                                lastSingleQuotePos = i;
                            }
                        }
                        endIf = true;
                    }
                }
            }

            if (curStat == Stats.DEFAULT.getValue()
                || !intToBool(
                (table.get(curStat).getOrDefault(ch, 0) & Id.NEXT1.getValue())
            )) {
                curSentence += ch;
            }

            prev = ch;
        }

        if (!curSentence.isEmpty()) {
            curSentence = doTrimSentPushResults(curSentence, results);
        }
        if (
            intToBool(
                table.get(curStat).getOrDefault(prev, 0) & Id.NEXT1.getValue()
            )) {
            curSentence += prev;
            doTrimSentPushResults(curSentence, results);
        }

        if (useHeuristic) {
            if (text.contains("다 ")) {
                results = postProcessing(results, postProcessingDa);
            }
            if (text.contains("요 ")) {
                results = postProcessing(results, postProcessingYo);
            }
            if (text.contains("죠 ")) {
                results = postProcessing(results, postProcessingJyo);
            }
            if (text.contains("함 ")) {
                results = postProcessing(results, postProcessingHam);
            }
            if (text.contains("음 ")) {
                results = postProcessing(results, postProcessingUm);
            }
        }
        if (singleQuoteStack.size() != 0 && recoverStep < maxRecoverStep) {
            results = realignByQuote(
                text,
                lastSingleQuotePos,
                singleQuotePop,
                useHeuristic,
                useQuotesBracketsProcessing,
                maxRecoverStep,
                maxRecoverLength,
                recoverStep + 1
            );
        }
        if (doubleQuoteStack.size() != 0 && recoverStep < maxRecoverStep) {
            results = realignByQuote(
                text,
                lastDoubleQuotePos,
                doubleQuotePop,
                useHeuristic,
                useQuotesBracketsProcessing,
                maxRecoverStep,
                maxRecoverLength,
                recoverStep + 1
            );
        }
        if (bracketStack.size() != 0 && recoverStep < maxRecoverStep) {
            results = realignByQuote(
                text,
                lastBracketPos,
                bracketPoP,
                useHeuristic,
                useQuotesBracketsProcessing,
                maxRecoverStep,
                maxRecoverLength,
                recoverStep + 1
            );
        }

        ArrayList<String> resultList = new ArrayList<>();

        for (String s : results) {
            s = backupManager.restore(s);
            s = s.replace("\u200b", "");
            resultList.add(useStrip ? s.strip() : s);
        }

        results.addAll(resultList);
        return resultList;
    }

    public ArrayList<SentenceIndex> splitSentencesIndex(
        String text,
        boolean useHeuristic,
        boolean useQuotesBracketsProcessing,
        int maxRecoverStep,
        int maxRecoverLength
    ) {
        ArrayList<String> sentences = splitSentences(
            text,
            useHeuristic,
            useQuotesBracketsProcessing,
            maxRecoverStep,
            maxRecoverLength,
            0,
            true
        );

        ArrayList<SentenceIndex> sentenceIndexes = new ArrayList<>();
        int offset = 0;

        for (String sentence : sentences) {
            sentenceIndexes.add(new SentenceIndex(offset + text.indexOf(sentence),
                offset + text.indexOf(sentence) + sentence.length()));

            offset += text.indexOf(sentence) + sentence.length();
            text = text.substring(text.indexOf(sentence) + sentence.length());
        }
        return sentenceIndexes;
    }

    public ArrayList<ChunkWithIndex> splitChunks(
        String text,
        int maxLength,
        boolean overlap,
        boolean useHeuristic,
        boolean useQuotesBracketsProcessing,
        int maxRecoverStep,
        int maxRecoverLength
    ) {

        ArrayList<SentenceIndex> span = new ArrayList<>();
        ArrayList<ChunkWithIndex> chunks = new ArrayList<>();

        ArrayList<SentenceIndex> indices = splitSentencesIndex(
            text,
            useHeuristic,
            useQuotesBracketsProcessing,
            maxRecoverStep,
            maxRecoverLength
        );

        for (SentenceIndex index : indices) {
            if (span.size() > 0) {
                if (index.getEnd() - span.get(0).getStart() > maxLength) {
                    chunks.add(getChunkWithIndex(span, text));
                    if (overlap) {
                        double halfSpanSize = span.size() / 2.0;
                        span = new ArrayList<>(span.subList(
                            (int) (halfSpanSize - (halfSpanSize % 1)),
                            span.size()
                        ));

                    } else {
                        span = new ArrayList<>();
                    }
                }
            }
            span.add(index);
        }

        chunks.add(getChunkWithIndex(span, text));
        return chunks;
    }

    public ChunkWithIndex getChunkWithIndex(
        List<SentenceIndex> span,
        String text) {
        int start = span.get(0).getStart();
        int end = span.get(span.size() - 1).getEnd();

        return new ChunkWithIndex(
            span.get(0).getStart(),
            text.substring(start, end)
        );
    }
}
