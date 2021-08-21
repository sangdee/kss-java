package kss.rule;
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

import kss.base.enumerate.Id;
import kss.base.enumerate.Stats;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Rule {

    static List<String> top500 = Arrays
        .asList("가", "간", "갈", "갉", "감", "갔", "갖", "같", "갚", "개", "걔", "걘", "거", "건", "걷", "걸", "검",
            "겪", "곤", "골", "곪", "곱", "괴",
            "구", "군", "굵", "굶", "굼", "굽", "궤", "귑", "귓", "규", "균", "긁", "긋", "기", "길", "긺", "깊",
            "까", "깎", "깐", "깖", "깜", "깠",
            "깨", "깬", "깼", "꺼", "꺾", "껐", "껴", "꼈", "꼬", "꼽", "꽂", "꽤", "꾸", "꾼", "꿇", "꿈", "꿔",
            "꿨", "꿰", "뀌", "끈", "끊", "끌",
            "끎", "끓", "끔", "끼", "낀", "낌", "나", "낚", "난", "날", "낡", "남", "났", "낮", "내", "낸", "냄",
            "냅", "냈", "넓", "넘", "넣", "녹",
            "논", "놀", "놂", "높", "놓", "놔", "놨", "누", "눈", "눕", "눠", "늘", "늙", "늚", "늦", "닦", "단",
            "닫", "달", "닮", "닳", "담", "답",
            "닿", "대", "댄", "댐", "댔", "던", "덜", "덞", "덥", "덮", "데", "덴", "뎀", "뎄", "돈", "돋", "돌",
            "돕", "돼", "됐", "되", "된", "됨",
            "두", "둔", "둠", "뒀", "든", "듣", "들", "듦", "듬", "딛", "딪", "따", "딴", "땀", "땄", "땋", "땠",
            "떠", "떨", "떴", "떼", "뗀", "뗌",
            "뛰", "뜀", "뜨", "뜯", "뜸", "띄", "띈", "띔", "띠", "띤", "막", "만", "많", "말", "맑", "맒", "맞",
            "맡", "매", "맨", "맴", "맵", "맸",
            "맺", "먹", "멀", "멂", "메", "멘", "멨", "몬", "몰", "몲", "묵", "묶", "묻", "물", "묽", "묾", "뭍",
            "뭘", "민", "믿", "밀", "밂", "밈",
            "밉", "박", "받", "밝", "밟", "배", "밴", "뱀", "뱄", "뱉", "번", "벌", "벎", "벗", "베", "벤", "보",
            "볶", "본", "봄", "봤", "봬", "뵀",
            "뵈", "뵌", "분", "붇", "불", "붉", "붊", "붓", "붙", "비", "빈", "빌", "빎", "빔", "빚", "빤", "빨",
            "빪", "빻", "빼", "뺀", "뺌", "뺐",
            "뻗", "뻤", "뼜", "뿜", "삔", "삠", "사", "산", "살", "삵", "삶", "삼", "샀", "새", "샌", "샘", "샛",
            "샜", "서", "섞", "선", "섰", "세",
            "셈", "셌", "속", "솎", "솟", "숨", "쉬", "쉰", "쉼", "쉽", "시", "식", "싣", "싫", "싶", "싸", "싼",
            "쌈", "쌌", "쌓", "쌔", "쌨", "써",
            "썩", "썰", "썲", "썼", "쎄", "쏘", "쏜", "쏟", "쏨", "쏴", "쐈", "쑤", "쑨", "쓰", "쓴", "쓸", "쓺",
            "씀", "씌", "씐", "씹", "안", "앉",
            "않", "알", "앎", "앓", "암", "약", "얇", "얕", "얘", "얜", "언", "얹", "얻", "얼", "없", "엎", "엮",
            "연", "열", "엶", "옅", "옌", "옛",
            "오", "온", "옭", "옮", "옳", "옴", "와", "왔", "왜", "운", "울", "읊", "일", "읽", "잃", "입", "있",
            "잊", "자", "작", "잔", "잡", "잤",
            "잦", "재", "잰", "잼", "쟀", "쟤", "쟨", "적", "전", "절", "젊", "접", "젓", "져", "졌", "존", "졸",
            "졺", "좁", "좇", "좋", "주", "죽",
            "준", "줌", "줍", "줘", "줬", "쥐", "쥠", "지", "진", "질", "집", "짓", "짖", "짙", "짜", "짧", "짰",
            "째", "짼", "쨌", "쩐", "쩔", "쪄",
            "쪘", "쫀", "쬐", "쬠", "찌", "찍", "찐", "찜", "찝", "찢", "차", "찬", "참", "찼", "찾", "채", "챈",
            "챘", "쳐", "쳤", "추", "춘", "춤",
            "춥", "춰", "췄", "치", "친", "침", "캐", "캠", "캤", "커", "컸", "켜", "켠", "켬", "켰", "크", "큼",
            "키", "킨", "킴", "타", "탄", "탐",
            "탔", "터", "턺", "텁", "텨", "튀", "튄", "튐", "트", "튼", "틂", "틈", "파", "팔", "팜", "팠", "패",
            "팼", "퍼", "펌", "펐", "펴", "편",
            "폄", "폈", "푼", "품", "피", "핀", "핌", "하", "핥", "함", "해", "했", "헌", "휘", "희");

    static List<String> da = Arrays.asList("간", "같", "걔", "거", "건", "검", "곤", "곱",
        "구", "군", "굵", "길", "깊", "깐", "깠",
        "깬", "깼", "껐", "꼈", "꾼", "꿨",
        "끼", "낀", "난", "낡", "났", "낮", "낸", "냈", "넓",
        "논", "높", "놨", "눈", "늙", "늦", "단", "답",
        "닿", "댄", "댔", "덥", "덴", "뎄", "돈", "돼", "됐", "되", "된",
        "둔", "뒀", "딴", "땄", "땠", "떴", "뗀",
        "띈", "띤", "많", "맑", "맨", "맵", "맸",
        "멀", "멘", "멨", "몬", "묽", "민",
        "밉", "밝", "밴", "뱄", "번", "벤", "본", "봤", "뵀",
        "뵌", "분", "붉", "비", "빈", "빤", "뺀", "뺐",
        "뻤", "뼜", "삔", "산", "샀", "샌", "샜", "선", "섰",
        "셌", "쉰", "쉽", "시", "싶", "싼", "쌈", "쌌", "쌔", "쌨", "써",
        "썼", "쏜", "쐈", "쑨", "쓴",
        "않", "얇", "얕", "얘", "언", "없", "연", "옅",
        "옳", "왔", "운", "있", "작", "잔", "잤",
        "잦", "잰", "쟀", "쟤", "적", "전", "젊", "졌", "존", "좁", "좋",
        "준", "줍", "줬", "진", "짙", "짜", "짧", "짰", "짼", "쨌", "쩐",
        "쪘", "쫀", "찐", "차", "찬", "찼", "챈", "챘", "쳤", "춘",
        "춥", "췄", "친", "캤", "컸", "켠", "켰", "크", "킨", "탄",
        "탔", "튀", "튄", "트", "튼", "팠", "팼", "펐", "편",
        "폈", "푼", "핀", "했", "희");

    static List<String> yo = Arrays.asList("가", "감", "개", "걔", "걘", "괴",
        "까",
        "깨", "껴", "꿈", "꿔", "꿰",
        "끔", "낌", "나", "남", "내", "냄",
        "놂", "놔", "눠", "늚",
        "대", "댐", "데", "돼", "되", "됨",
        "둠", "듦", "듬", "따", "땀", "떠", "떼",
        "뜀", "뜸", "띔", "매",
        "메",
        "배", "베", "봬",
        "뵈", "빎", "빔", "빪", "뺌",
        "삠", "사", "삶", "삼", "새", "서", "세",
        "셈", "싸", "쌈", "쌔", "써",
        "썲", "쎄", "쏨", "쏴", "쓺", "씀",
        "앎", "암", "얘", "얜", "엶",
        "옮", "옴", "와", "왜", "자",
        "재", "잼", "쟤", "쟨", "젊", "져", "졺",
        "줌", "줘", "짜", "째", "쪄",
        "쬠", "찜", "차", "채", "쳐", "춤",
        "춰", "캐", "커", "켜", "켬", "킴", "타", "탐",
        "터", "텨", "튐", "틈", "팜", "패", "퍼", "펌", "펴",
        "폄", "품", "핌", "함", "해");

    static List<String> jyo = Arrays
        .asList("가", "갉", "갔", "갖", "같", "갚", "개", "걔", "걷", "걸", "검", "겪", "골", "곪", "곱", "괴",
            "굵", "굶", "굼", "굽", "긁", "긋", "길", "깊", "깎", "깠",
            "깨", "깼", "꺾", "껐", "꼈", "꼽", "꽂", "꾸", "꿇", "꿨", "꿰", "뀌", "끊", "끌",
            "끓", "끼", "낚", "날", "낡", "남", "났", "낮", "내", "냈", "넓", "넘", "넣", "녹",
            "놀", "높", "놓", "놨", "누", "눕", "늙", "늦", "닦", "닫", "달", "닮", "닳", "답",
            "닿", "대", "댔", "덜", "덥", "덮", "데", "뎄", "돋", "돌", "돕", "돼", "됐", "되",
            "두", "뒀", "듣", "들", "딛", "딪", "따", "땄", "땋", "땠", "떨", "떴", "떼",
            "뛰", "뜨", "뜯", "띄", "띠", "막", "많", "말", "맑", "맞", "맡", "매", "맵", "맸",
            "맺", "먹", "멀", "메", "멨", "몰", "묵", "묶", "묻", "묽", "뭍", "믿", "밀",
            "밉", "박", "받", "밝", "밟", "배", "뱄", "뱉", "벌", "벗", "베", "보", "볶", "봤", "봬", "뵀",
            "뵈", "붇", "불", "붉", "붓", "붙", "비", "빌", "빚", "빨", "빻", "빼", "뺐",
            "뻗", "뻤", "뼜", "사", "살", "삵", "샀", "새", "샌", "샛", "샜", "서", "섞", "섰", "세",
            "셌", "속", "솎", "솟", "숨", "쉬", "쉽", "시", "식", "싣", "싫", "싶", "싸", "쌌", "쌓", "쌔", "쌨",
            "써",
            "썩", "썰", "썼", "쎄", "쏘", "쏟", "쏴", "쐈", "쑤", "쓰", "쓸", "씌", "씹", "앉",
            "않", "알", "앓", "약", "얇", "얕", "얘", "얹", "얻", "얼", "없", "엎", "엮", "열", "옅", "옛",
            "오", "온", "옭", "옮", "옳", "와", "왔", "울", "읊", "일", "읽", "잃", "입", "있", "잊", "자", "작",
            "잡", "잤",
            "잦", "재", "잰", "쟀", "쟤", "적", "절", "젊", "접", "젓", "졌", "졸", "좁", "좇", "좋", "주", "죽",
            "줍", "줬", "쥐", "지", "질", "집", "짓", "짖", "짙", "짜", "짧", "짰", "째", "쨌", "쩔",
            "쪘", "쬐", "찌", "찍", "찐", "찝", "찢", "차", "찼", "찾", "채", "챘", "쳐", "쳤", "추",
            "춥", "춰", "췄", "치", "캐", "캤", "커", "컸", "켜", "켠", "켰", "크", "키", "타",
            "탔", "터", "튀", "트", "파", "팔", "팠", "패", "팼", "펐", "펴",
            "폈", "피", "하", "핥", "했", "휘", "희");

    static List<String> ham = Arrays
        .asList("리", "절", "용", "편", "륭", "듯", "야", "족", "못", "끗", "안", "천",
            "정", "각", "실", "소", "끔", "분", "이", "약");

    static List<String> um = Arrays
        .asList("았", "었", "했", "없", "좋", "있", "웠", "였", "않", "같", "많", "겠", "찮", "났", "좁", "작", "싶",
            "셨", "졌", "넓");

    static List<String> before = Arrays.asList(
        //조사
        "이", "가", "에서", "은", "는", "을", "를", "도", "에", "게", "께", "한테", "로", "써",
        "와", "과", "랑", "까지", "부터", "뿐", "만", "따라", "토록", "도록", "든지", "던지", "란",
        "만큼", "만치", "때",

        //부사
        "너무", "잘", "못", "빨리", "매우", "몹시", "별로", "아까", "내일", "일찍", "금방",
        "이미", "이리", "저리", "아니", "과연", "설마", "제발", "정말", "결코", "가득", "히",

        //대명사
        "나", "저", "우리", "저희", "너", "너희", "당신", "그대", "그", "그녀", "분", "놈", "거", "것",
        "여기", "저기", "쪽", "곳", "님"
    );
    public static Map<String, Integer> daValue = new HashMap<>() {{
        put("갔", Id.PREV.getValue());
        put("간", Id.PREV.getValue());
        put("겠", Id.PREV.getValue());
        put("겼", Id.PREV.getValue());
        put("같", Id.PREV.getValue());
        put("놨", Id.PREV.getValue());
        put("녔", Id.PREV.getValue());
        put("니", Id.PREV.getValue());
        put("논", Id.PREV.getValue());
        put("낸", Id.PREV.getValue());
        put("냈", Id.PREV.getValue());
        put("뒀", Id.PREV.getValue());
        put("때", Id.PREV.getValue());
        put("랐", Id.PREV.getValue());
        put("럽", Id.PREV.getValue());
        put("렵", Id.PREV.getValue());
        put("렸", Id.PREV.getValue());
        put("뤘", Id.PREV.getValue());
        put("몄", Id.PREV.getValue());
        put("밌", Id.PREV.getValue());
        put("볐", Id.PREV.getValue());
        put("볍", Id.PREV.getValue());
        put("봤", Id.PREV.getValue());
        put("섰", Id.PREV.getValue());
        put("샜", Id.PREV.getValue());
        put("셨", Id.PREV.getValue());
        put("싼", Id.PREV.getValue());
        put("싸", Id.PREV.getValue());
        put("않", Id.PREV.getValue());
        put("았", Id.PREV.getValue());
        put("없", Id.PREV.getValue());
        put("었", Id.PREV.getValue());
        put("였", Id.PREV.getValue());
        put("온", Id.PREV.getValue());
        put("웠", Id.PREV.getValue());
        put("이", Id.PREV.getValue());
        put("인", Id.PREV.getValue());
        put("있", Id.PREV.getValue());
        put("진", Id.PREV.getValue());
        put("졌", Id.PREV.getValue());
        put("쳤", Id.PREV.getValue());
        put("췄", Id.PREV.getValue());
        put("챘", Id.PREV.getValue());
        put("켰", Id.PREV.getValue());
        put("켠", Id.PREV.getValue());
        put("팠", Id.PREV.getValue());
        put("펐", Id.PREV.getValue());
        put("폈", Id.PREV.getValue());
        put("했", Id.PREV.getValue());
        put("혔", Id.PREV.getValue());
        put("한", Id.NEXT.getValue());
        put("가", Id.NEXT.getValue());
        put("고", Id.NEXT.getValue() | Id.NEXT2.getValue());
        put("는", Id.NEXT.getValue() | Id.NEXT2.getValue());
        put("라", Id.NEXT.getValue());
        put("시", Id.NEXT.getValue());
        put("등", Id.NEXT.getValue());
        put("던", Id.NEXT.getValue());
        put("든", Id.NEXT.getValue());
        put("지", Id.NEXT1.getValue() | Id.NEXT2.getValue());
        put("를", Id.NEXT.getValue());
        put("운", Id.NEXT.getValue());  //~다운
        put("만", Id.NEXT.getValue());
        put("며", Id.NEXT.getValue() | Id.NEXT2.getValue());
        put("면", Id.NEXT.getValue() | Id.NEXT1.getValue() | Id.NEXT2.getValue());
        put("서", Id.NEXT2.getValue());
        put("싶", Id.PREV.getValue() | Id.NEXT.getValue());
        put("죠", Id.NEXT.getValue());
        put("죵", Id.NEXT.getValue());
        put("쥬", Id.NEXT.getValue());
        put("하", Id.NEXT1.getValue());
        put("해", Id.NEXT1.getValue());
        put("도", Id.NEXT2.getValue());
        put("", Id.NONE.getValue());
    }};
    public static Map<String, Integer> yoValue = new HashMap<>() {{
        put("겨", Id.PREV.getValue());
        put("거", Id.PREV.getValue());
        put("구", Id.PREV.getValue());
        put("군", Id.PREV.getValue());
        put("걸", Id.PREV.getValue());
        put("까", Id.PREV.getValue());
        put("께", Id.PREV.getValue());
        put("껴", Id.PREV.getValue());
        put("네", Id.PREV.getValue());
        put("나", Id.PREV.getValue());
        put("니", Id.PREV.getValue());
        put("데", Id.PREV.getValue());
        put("든", Id.PREV.getValue());
        put("려", Id.PREV.getValue());
        put("서", Id.PREV.getValue());
        put("세", Id.PREV.getValue());
        put("아", Id.PREV.getValue());
        put("어", Id.PREV.getValue());
        put("워", Id.PREV.getValue());
        put("에", Id.PREV.getValue());
        put("예", Id.PREV.getValue());
        put("을", Id.PREV.getValue());
        put("져", Id.PREV.getValue());
        put("줘", Id.PREV.getValue());
        put("지", Id.PREV.getValue());
        put("춰", Id.PREV.getValue());
        put("해", Id.PREV.getValue());
        put("고", Id.NEXT2.getValue());
        put("는", Id.NEXT.getValue());
        put("라", Id.NEXT1.getValue());
        put("등", Id.NEXT.getValue());
        put("를", Id.NEXT.getValue());
        put("즘", Id.NEXT.getValue());
        put("소", Id.NEXT.getValue());
        put("며", Id.NEXT2.getValue());
        put("면", Id.PREV.getValue() | Id.NEXT2.getValue());
        put("하", Id.NEXT1.getValue());
        put("", Id.NONE.getValue());
    }};
    public static Map<String, Integer> jyoValue = new HashMap<>() {{
        put("거", Id.PREV.getValue());
        put("가", Id.PREV.getValue());
        put("갔", Id.PREV.getValue());
        put("겠", Id.PREV.getValue());
        put("같", Id.PREV.getValue());
        put("놨", Id.PREV.getValue());
        put("녔", Id.PREV.getValue());
        put("냈", Id.PREV.getValue());
        put("니", Id.PREV.getValue());
        put("뒀", Id.PREV.getValue());
        put("았", Id.PREV.getValue());
        put("르", Id.PREV.getValue());
        put("랐", Id.PREV.getValue());
        put("럽", Id.PREV.getValue());
        put("렵", Id.PREV.getValue());
        put("렸", Id.PREV.getValue());
        put("맞", Id.PREV.getValue());
        put("몄", Id.PREV.getValue());
        put("밌", Id.PREV.getValue());
        put("볐", Id.PREV.getValue());
        put("볍", Id.PREV.getValue());
        put("봤", Id.PREV.getValue());
        put("서", Id.PREV.getValue());
        put("섰", Id.PREV.getValue());
        put("셨", Id.PREV.getValue());
        put("샜", Id.PREV.getValue());
        put("않", Id.PREV.getValue());
        put("없", Id.PREV.getValue());
        put("었", Id.PREV.getValue());
        put("였", Id.PREV.getValue());
        put("이", Id.PREV.getValue());
        put("졌", Id.PREV.getValue());
        put("쳤", Id.PREV.getValue());
        put("챘", Id.PREV.getValue());
        put("켰", Id.PREV.getValue());
        put("팠", Id.PREV.getValue());
        put("폈", Id.PREV.getValue());
        put("하", Id.PREV.getValue());
        put("했", Id.PREV.getValue());
        put("혔", Id.PREV.getValue());
        put("고", Id.PREV.getValue() | Id.NEXT2.getValue());
        put("는", Id.NEXT.getValue());
        put("등", Id.NEXT.getValue());
        put("라", Id.NEXT1.getValue());
        put("를", Id.NEXT.getValue());
        put("며", Id.NEXT2.getValue());
        put("면", Id.PREV.getValue() | Id.NEXT2.getValue());
        put("", Id.NONE.getValue());
    }};
    public static Map<String, Integer> umValue = new HashMap<>() {{
        put("았", Id.PREV.getValue());
        put("없", Id.PREV.getValue());
        put("었", Id.PREV.getValue());
        put("했", Id.PREV.getValue());
        put("있", Id.PREV.getValue());
        put("좋", Id.PREV.getValue());
        put("웠", Id.PREV.getValue());
        put("였", Id.PREV.getValue());
        put("않", Id.PREV.getValue());
        put("같", Id.PREV.getValue());
        put("겠", Id.PREV.getValue());
        put("봤", Id.PREV.getValue());
        put("밌", Id.PREV.getValue());
        put("많", Id.PREV.getValue());
        put("찮", Id.PREV.getValue());
        put("났", Id.PREV.getValue());
        put("처", Id.PREV.getValue());
        put("렸", Id.PREV.getValue());
        put("졌", Id.PREV.getValue());
        put("싶", Id.PREV.getValue());
        put("이", Id.NEXT.getValue());
        put("에", Id.NEXT.getValue());
        put("악", Id.NEXT.getValue());
        put("식", Id.NEXT.getValue());
        put("을", Id.NEXT.getValue());
        put("으", Id.NEXT.getValue());
        put("부", Id.NEXT.getValue());
        put("도", Id.NEXT.getValue());
        put("은", Id.NEXT.getValue());
        put("엔", Id.NEXT.getValue());
        put("날", Id.NEXT.getValue());
        put("료", Id.NEXT.getValue());
        put("과", Id.NEXT.getValue());
        put("의", Id.NEXT.getValue());
        put("만", Id.NEXT.getValue());
        put("보", Id.NEXT.getValue());
        put("인", Id.NEXT.getValue());
        put("속", Id.NEXT.getValue());
        put("", Id.NONE.getValue());
    }};
    public static Map<String, Integer> hamValue = new HashMap<>() {{
        put("루", Id.PREV.getValue());
        put("편", Id.PREV.getValue());
        put("절", Id.PREV.getValue());
        put("포", Id.PREV.getValue());
        put("안", Id.PREV.getValue());
        put("못", Id.PREV.getValue());
        put("만", Id.PREV.getValue() | Id.NEXT.getValue());
        put("족", Id.PREV.getValue());
        put("야", Id.PREV.getValue());
        put("치", Id.PREV.getValue());
        put("결", Id.PREV.getValue());
        put("수", Id.PREV.getValue());
        put("각", Id.PREV.getValue());
        put("끗", Id.PREV.getValue());
        put("리", Id.PREV.getValue());
        put("답", Id.PREV.getValue());
        put("중", Id.PREV.getValue());
        put("용", Id.PREV.getValue());
        put("심", Id.PREV.getValue());
        put("쾌", Id.PREV.getValue());
        put("께", Id.NEXT.getValue());
        put("이", Id.NEXT.getValue());
        put("을", Id.NEXT.getValue());
        put("과", Id.NEXT.getValue());
        put("에", Id.NEXT.getValue());
        put("은", Id.NEXT.getValue());
        put("의", Id.NEXT.getValue());
        put("도", Id.NEXT.getValue());
        put("으", Id.NEXT.getValue());
        put("되", Id.NEXT.getValue());
        put("없", Id.NEXT.getValue());
        put("부", Id.NEXT.getValue());
        put("된", Id.NEXT.getValue());
        put("정", Id.NEXT.getValue());
        put("해", Id.NEXT.getValue());
        put("한", Id.NEXT.getValue());
        put("까", Id.NEXT.getValue());
        put("축", Id.NEXT.getValue());
        put("", Id.NONE.getValue());
    }};

    public static Map<String, Integer> sbValue = new HashMap<>() {{
        put("것", Id.PREV.getValue());
        put("가", Id.PREV.getValue());
        put("까", Id.PREV.getValue());
        put("거", Id.PREV.getValue());
        put("게", Id.PREV.getValue());
        put("걸", Id.PREV.getValue());
        put("껄", Id.PREV.getValue());
        put("나", Id.PREV.getValue());
        put("니", Id.PREV.getValue());
        put("네", Id.PREV.getValue());
        put("다", Id.PREV.getValue());
        put("쎄", Id.PREV.getValue());
        put("래", Id.PREV.getValue());
        put("데", Id.PREV.getValue());
        put("지", Id.PREV.getValue());
        put("든", Id.PREV.getValue());
        put("덩", Id.PREV.getValue());
        put("등", Id.PREV.getValue());
        put("랴", Id.PREV.getValue());
        put("마", Id.PREV.getValue());
        put("봐", Id.PREV.getValue());
        put("서", Id.PREV.getValue());
        put("아", Id.PREV.getValue());
        put("어", Id.PREV.getValue());
        put("오", Id.PREV.getValue());
        put("요", Id.PREV.getValue());
        put("을", Id.PREV.getValue());
        put("자", Id.PREV.getValue());
        put("죠", Id.PREV.getValue());
        put("고", Id.NEXT2.getValue());
        put("는", Id.NEXT.getValue());
        put("라", Id.PREV.getValue() | Id.NEXT.getValue());
        put("며", Id.NEXT2.getValue());
        put("면", Id.NEXT2.getValue());
        put("하", Id.NEXT1.getValue());
        put("", Id.NONE.getValue());
    }};
    public static Map<String, Integer> commonValue = new HashMap<>() {{
        put("ㄱ", Id.CONT.getValue());
        put("ㄴ", Id.CONT.getValue());
        put("ㄷ", Id.CONT.getValue());
        put("ㄹ", Id.CONT.getValue());
        put("ㅁ", Id.CONT.getValue());
        put("ㅂ", Id.CONT.getValue());
        put("ㅅ", Id.CONT.getValue());
        put("ㅇ", Id.CONT.getValue());
        put("ㅈ", Id.CONT.getValue());
        put("ㅊ", Id.CONT.getValue());
        put("ㅋ", Id.CONT.getValue());
        put("ㅌ", Id.CONT.getValue());
        put("ㅍ", Id.CONT.getValue());
        put("ㅎ", Id.CONT.getValue());
        put("ㅏ", Id.CONT.getValue());
        put("ㅑ", Id.CONT.getValue());
        put("ㅓ", Id.CONT.getValue());
        put("ㅕ", Id.CONT.getValue());
        put("ㅗ", Id.CONT.getValue());
        put("ㅛ", Id.CONT.getValue());
        put("ㅜ", Id.CONT.getValue());
        put("ㅠ", Id.CONT.getValue());
        put("ㅡ", Id.CONT.getValue());
        put("ㅣ", Id.CONT.getValue());
        put("^", Id.CONT.getValue());
        put(";", Id.CONT.getValue());
        put(".", Id.CONT.getValue());
        put("?", Id.CONT.getValue());
        put("!", Id.CONT.getValue());
        put("~", Id.CONT.getValue());
        put("…", Id.CONT.getValue());
        put(",", Id.CONT.getValue());
        put("", Id.NONE.getValue());
    }};
    public static Map<Integer, Map<String, Integer>> table = new HashMap<>() {{
        put(Stats.DEFAULT.getValue(), new HashMap<>());
        put(Stats.DA.getValue(), daValue);
        put(Stats.YO.getValue(), yoValue);
        put(Stats.JYO.getValue(), jyoValue);
        put(Stats.UM.getValue(), umValue);
        put(Stats.HAM.getValue(), hamValue);
        put(Stats.SB.getValue(), sbValue);
        put(Stats.COMMON.getValue(), commonValue);
    }};

    public static List<String> postProcessingDa = setPostProcessingDa();
    public static List<String> postProcessingYo = setPostProcessingYo();
    public static List<String> postProcessingJyo = setPostProcessingJyo();
    public static List<String> postProcessingHam = setPostProcessingHam();
    public static List<String> postProcessingUm = setPostProcessingUm();


    private static ArrayList<String> setPostProcessingDa() {
        Set<String> set = new HashSet<>();
        for (String i : da) {
            for (String j : before) {
                set.add(String.format("%s %s다", j, i));
            }
        }
        return new ArrayList<>(set);
    }

    private static List<String> setPostProcessingYo() {
        Set<String> set = new HashSet<>();
        for (String i : yo) {
            for (String j : before) {
                set.add(String.format("%s %s요", j, i));
            }
        }
        return new ArrayList<>(set);
    }

    private static List<String> setPostProcessingJyo() {
        Set<String> set = new HashSet<>();
        for (String i : jyo) {
            for (String j : before) {
                set.add(String.format("%s %s죠", j, i));
            }
        }
        return new ArrayList<>(set);
    }

    private static List<String> setPostProcessingHam() {
        Set<String> set = new HashSet<>();
        for (String i : ham) {
            for (String j : before) {
                set.add(String.format("%s %s함", j, i));
            }
        }
        return new ArrayList<>(set);
    }

    private static List<String> setPostProcessingUm() {
        Set<String> set = new HashSet<>();
        for (String i : um) {
            for (String j : before) {
                set.add(String.format("%s %s음", j, i));
            }
        }
        return new ArrayList<>(set);
    }
}
