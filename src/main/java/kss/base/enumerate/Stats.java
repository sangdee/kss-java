package kss.base.enumerate;
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

public enum Stats {
    DEFAULT(0),
    DA(1),
    YO(2),
    JYO(3),
    HAM(4),
    UM(5),
    SB(6),
    COMMON(7);

    private int value;

    Stats(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
