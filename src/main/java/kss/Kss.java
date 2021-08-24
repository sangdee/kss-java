package kss;
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
import kss.base.ChunkWithIndex;
import java.util.ArrayList;
import kss.core.Backend;

public class Kss {

    private final Backend kss;

    public Kss() {
        this.kss = new Backend();
    }

    public ArrayList<String> splitSentences(
        String text,
        boolean useHeuristic,
        boolean useQuotesBracketProcessing,
        int maxRecoverStep,
        int maxRecoverLength
    ) {
        return this.kss.splitSentences(
            text,
            useHeuristic,
            useQuotesBracketProcessing,
            maxRecoverStep,
            maxRecoverLength,
            0,
            true
        );
    }

    public ArrayList<String> splitSentences(
        String text,
        boolean useHeuristic,
        boolean useQuotesBracketProcessing,
        int maxRecoverStep
    ) {
        return this.kss.splitSentences(
            text,
            useHeuristic,
            useQuotesBracketProcessing,
            maxRecoverStep,
            20000,
            0,
            true
        );
    }

    public ArrayList<String> splitSentences(
        String text,
        boolean useHeuristic,
        boolean useQuotesBracketProcessing
    ) {
        return this.kss.splitSentences(
            text,
            useHeuristic,
            useQuotesBracketProcessing,
            5,
            20000,
            0,
            true
        );
    }

    public ArrayList<String> splitSentences(
        String text,
        boolean useHeuristic
    ) {
        return this.kss.splitSentences(
            text,
            useHeuristic,
            true,
            5,
            20000,
            0,
            true
        );
    }

    public ArrayList<String> splitSentences(
        String text
    ) {
        return this.kss.splitSentences(
            text,
            true,
            true,
            5,
            20000,
            0,
            true
        );
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
        return kss.splitChunks(
            text,
            maxLength,
            overlap,
            useHeuristic,
            useQuotesBracketsProcessing,
            maxRecoverStep,
            maxRecoverLength
        );
    }

    public ArrayList<ChunkWithIndex> splitChunks(
        String text,
        int maxLength,
        boolean overlap,
        boolean useHeuristic,
        boolean useQuotesBracketsProcessing,
        int maxRecoverStep
    ) {
        return kss.splitChunks(
            text,
            maxLength,
            overlap,
            useHeuristic,
            useQuotesBracketsProcessing,
            maxRecoverStep,
            20000
        );
    }

    public ArrayList<ChunkWithIndex> splitChunks(
        String text,
        int maxLength,
        boolean overlap,
        boolean useHeuristic,
        boolean useQuotesBracketsProcessing
    ) {
        return kss.splitChunks(
            text,
            maxLength,
            overlap,
            useHeuristic,
            useQuotesBracketsProcessing,
            5,
            20000
        );
    }

    public ArrayList<ChunkWithIndex> splitChunks(
        String text,
        int maxLength,
        boolean overlap,
        boolean useHeuristic
    ) {
        return kss.splitChunks(
            text,
            maxLength,
            overlap,
            useHeuristic,
            true,
            5,
            20000
        );
    }

    public ArrayList<ChunkWithIndex> splitChunks(
        String text,
        int maxLength,
        boolean overlap
    ) {
        return kss.splitChunks(
            text,
            maxLength,
            overlap,
            true,
            true,
            5,
            20000
        );
    }

    public ArrayList<ChunkWithIndex> splitChunks(
        String text,
        int maxLength
    ) {
        return kss.splitChunks(
            text,
            maxLength,
            false,
            true,
            true,
            5,
            20000
        );
    }
}
