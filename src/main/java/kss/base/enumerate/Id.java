package kss.base.enumerate;
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

public enum Id {
    NONE(0),
    PREV(1 << 0),
    CONT(1 << 1),
    NEXT(1 << 2),
    NEXT1(1 << 3),
    NEXT2(1 << 4);

    private int value;

    Id(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
