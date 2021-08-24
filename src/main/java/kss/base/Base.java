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

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Base {

    public static boolean empty(Object o) {
        if (o instanceof String) {
            return ((String) o).length() == 0;
        }
        return ((List<?>) o).size() == 0;
    }

    public static boolean top(LinkedList<String> stack, String symbol) {
        return Objects.equals(stack.peek(), symbol);
    }

    public static String doPushPopSymbol(
        LinkedList<String> stack,
        String symbol,
        String currentCh
    ) {
        if (empty(stack)) {
            stack.add(symbol);
        } else {
            if (top(stack, currentCh)) {
                stack.pop();
            } else {
                stack.add(symbol);
            }
        }
        return currentCh;
    }

    public static String doTrimSentPushResults(String curSentence, List<String> results) {
        results.add(curSentence.strip());
        curSentence = "";
        return curSentence;
    }
}
