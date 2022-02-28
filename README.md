# Korean Sentence Splitter
<img alt="latest version" src="https://img.shields.io/badge/latest%20version-2.6.1-blue.svg"/> <a href="https://github.com/sangdee/kss-java/blob/master/LICENSE"><img alt="BSD 3-Clause" src="https://img.shields.io/badge/license-BSD%203%20Clause-blue.svg"/></a>

Split Korean text into sentences using heuristic algorithm.

<br><br>

## 1. Installation
- Maven
```console
<dependency>
  <groupId>io.github.sangdee</groupId>
  <artifactId>kss-java</artifactId>
  <version>2.6.1</version>
</dependency>
```
- Gradle
```console
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.sangdee:kss-java:2.6.1'
}
```

<br><br>

## 2. Usage of `splitSentences`

```java
ArrayList<String> splitSentences(
        String text,
        boolean useHeuristic,  //default = true
        boolean useQuotesBracketsProcessing, //default = true
        int maxRecoverStep, //default = 5
        int maxRecoverLength, // default = 20000
        int recoverStep //default = 0
    ) 
```

### 2.1. Split sentences with heuristic algorithm.
- `splitSentences` is the key method of Kss.
- You can segment text to sentences with this method.

```java
import kss.Kss;

Kss kss = new Kss();
String text = "회사 동료 분들과 다녀왔는데 분위기도 좋고 음식도 맛있었어요 다만, 강남 토끼정이 강남 쉑쉑버거 골목길로 쭉 올라가야 하는데 다들 쉑쉑버거의 유혹에 넘어갈 뻔 했답니다 강남역 맛집 토끼정의 외부 모습.";
kss.splitSentences(text);
```
```java
["회사 동료 분들과 다녀왔는데 분위기도 좋고 음식도 맛있었어요,"
 "다만, 강남 토끼정이 강남 쉑쉑버거 골목길로 쭉 올라가야 하는데 다들 쉑쉑버거의 유혹에 넘어갈 뻔 했답니다,"
 "강남역 맛집 토끼정의 외부 모습."]
```

### 2.2. Split sentences without heuristic algorithm.
- If your articles follow the punctuation rules reratively well, we recommend to you set the `useHeuristic = false`. (default is `true`)
- In these cases, Kss segments text depending only on punctuataion and you can segment text much more safely.
  - Formal articles (Wiki, News, Essay, ...) : recommend `useHeuristic = false`
  - Informal articles (SNS, Blogs, Messages, ...) : recommend `useHeuristic = true`

```java
import kss.Kss;

Kss kss = new Kss();
String text = "미리 예약을 할 수 있는 시스템으로 합리적인 가격에 여러 종류의 생선, 그리고 다양한 부위를 즐길 수 있기 때문이다. 계절에 따라 모둠회의 종류는 조금씩 달라지지만 자주 올려주는 참돔 마스까와는 특히 맛이 매우 좋다. 일반 모둠회도 좋지만 좀 더 특별한 맛을 즐기고 싶다면 특수 부위 모둠회를 추천한다 제철 생선 5~6가지 구성에 평소 접하지 못했던 부위까지 색다르게 즐길 수 있다.";
kss.splitSentences(text, false);  
```
```java
["미리 예약을 할 수 있는 시스템으로 합리적인 가격에 여러 종류의 생선, 그리고 다양한 부위를 즐길 수 있기 때문이다.", 
 "계절에 따라 모둠회의 종류는 조금씩 달라지지만 자주 올려주는 참돔 마스까와는 특히 맛이 매우 좋다.", 
 "제철 생선 5~6가지 구성에 평소 접하지 못했던 부위까지 색다르게 즐길 수 있다."]
```

### 2.3. Brackets and quotation marks processing
- Kss provides a technique for not segmenting sentences enclosed in brackets (괄호) or quotation marks (따옴표).

```java
import kss.Kss;

Kss kss = new Kss();
String text = "그가 말했다. '거기는 가지 마세요. 위험하니까요. 알겠죠?' 그러자 그가 말했다. 알겠어요.";
kss.splitSentences(text)
        
["그가 말했다.","'거기는 가지 마세요. 위험하니까요. 알겠죠?' 그러자 그가 말했다.","알겠어요."]
```

#### 2.3.1. Several options to optimize recursion
- However, this can cause problem when brackets and quotation marks are misaligned, and it was a cronic problem of Kss 1.x (C++ version).
- From Kss 2.xx, we provide quotes and brocket calibration feature to solve this problem, but it uses recursion and has very poor time complexity O(2^n).
- So, we also provide several options to optimize recursion. You can save your precious time with these options.
  - The depth of the recursion can be modified through a parameter `maxRecoverStep`. (default is 5)
  - You can turn off calibration using the `maxRecoverLength` parameter. (default is 20,000)

```java
import kss.Kss;

Kss kss = new Kss();
String text = "VERY_LONG_TEXT";

splitSentences(text, true, true, 5);
// you can adjust recursion depth using `maxRecoverStep` (default is 5)
splitSentences(text, true, true, 5, 20000);
// you can turn it off when you input very long text using `maxRecoverLength` (default is 20000)
```

#### 2.3.2. Turn off brackets and quotation marks processing
- You can also turn off brackets and quotation marks processing if you want.
- Set `useQuotesBracketsProcessing = false` to turn it off.

```java
import kss.Kss;

Kss kss = new Kss();
String text = "그가 말했다. (거기는 가지 마세요. 위험하니까요. 알겠죠?) 그러자 그가 말했다. 알겠어요.";

kss.splitSentences(text);
['그가 말했다.','(거기는 가지 마세요. 위험하니까요. 알겠죠?) 그러자 그가 말했다.','알겠어요.']

kss.splitSentences(text, true, false);
['그가 말했다.','(거기는 가지 마세요.','위험하니까요.','알겠죠?',') 그러자 그가 말했다.','알겠어요.']
```

<br><br>

## 3. Usage of `splitChunks`
```java
 ArrayList<ChunkWithIndex> splitChunks(
        String text, 
        int maxLength,
        boolean overlap, //default = false
        boolean useHeuristic, //default = true
        boolean useQuotesBracketsProcessing,  //default = true
        int maxRecoverStep,  //default = 5
        int maxRecoverLength  //default = 20000
    ) 
```

### 3.1. Set maximum length of chunks via `maxLength`
- `splitChunks` combine sentences into chunks of a `maxlength` or less.
- You can set the maximum length of one chunk to `maxLength`.

```java
import kss.Kss;

Kss kss = new Kss();
String text = "NoSQL이라고 하는 말은 No 'English'라고 하는 말과 마찬가지다. 세상에는 영어 말고도 수많은 언어가 존재한다. MongoDB에서 사용하는 쿼리 언어와 CouchDB에서 사용하는 쿼리 언어는 서로 전혀 다르다. 그럼에도 이 두 쿼리 언어는 같은 NoSQL 카테고리에 속한다. 어쨌거나 SQL이 아니기 때문이다. 또한 NoSQL이 No RDBMS를 의미하지는 않는다. BerkleyDB같은 예외가 있기 때문이다. 그리고 No RDBMS가 NoSQL인 것도 아니다. SQL호환 레이어를 제공하는 KV-store라는 예외가 역시 존재한다. 물론 KV-store의 특징상 range query를 where절에 넣을 수 없으므로 완전한 SQL은 못 되고 SQL의 부분집합 정도를 제공한다.";
kss.splitChunks(text, 128);
```
```java
[ChunkWithIndex(start = 0, text = "NoSQL이라고 하는 말은 No 'English'라고 하는 말과 마찬가지다. 세상에는 영어 말고도 수많은 언어가 존재한다. MongoDB에서 사용하는 쿼리 언어와 CouchDB에서 사용하는 쿼리 언어는 서로 전혀 다르다."),
 ChunkWithIndex(start = 124, text = "그럼에도 이 두 쿼리 언어는 같은 NoSQL 카테고리에 속한다. 어쨌거나 SQL이 아니기 때문이다. 또한 NoSQL이 No RDBMS를 의미하지는 않는다. BerkleyDB같은 예외가 있기 때문이다."),
 ChunkWithIndex(start = 236, text = "그리고 No RDBMS가 NoSQL인 것도 아니다. SQL호환 레이어를 제공하는 KV-store라는 예외가 역 시 존재한다."),
 ChunkWithIndex(start = 305, text = "물론 KV-store의 특징상 range query를 where절에 넣을 수 없으므로 완전한 SQL은 못 되고 SQL의 부분집합 정도를 제공한다.")]
```

### 3.2. Overlap sentences across chunks
- If `overlap` is `true`, text will be chunked similar with sliding window.
- Each chunk allows for duplicate sentences if you turn this feature on.

```java
import kss.Kss;

Kss kss = new Kss();
String text = "NoSQL이라고 하는 말은 No 'English'라고 하는 말과 마찬가지다. 세상에는 영어 말고도 수많은 언어가 존재한다. MongoDB에서 사용하는 쿼리 언어와 CouchDB에서 사용하는 쿼리 언어는 서로 전혀 다르다. 그럼에도 이 두 쿼리 언어는 같은 NoSQL 카테고리에 속한다. 어쨌거나 SQL이 아니기 때문이다. 또한 NoSQL이 No RDBMS를 의미하지는 않는다. BerkleyDB같은 예외가 있기 때문이다. 그리고 No RDBMS가 NoSQL인 것도 아니다. SQL호환 레이어를 제공하는 KV-store라는 예외가 역시 존재한다. 물론 KV-store의 특징상 range query를 where절에 넣을 수 없으므로 완전한 SQL은 못 되고 SQL의 부분집합 정도를 제공한다.";
kss.splitChunks(text, 128, false, true); // text maxLength, overlap, useHeuristic,
```
```java
[ChunkWithIndex(start = 0, text = "NoSQL이라고 하는 말은 No 'English'라고 하는 말과 마찬가지다. 세상에는 영어 말고도 수많은 언어가 존재한다. MongoDB에서 사용하는 쿼리 언어와 CouchDB에서 사용하는 쿼리 언어는 서로 전혀 다르다."),
 ChunkWithIndex(start = 43, text = "세상에는 영어 말고도 수많은 언어가 존재한다. MongoDB에서 사용하는 쿼리 언어와 CouchDB에서 사용하는 쿼리 언어는 서로 전혀 다르다. 그럼에도 이 두 쿼리 언어는 같은 NoSQL 카테고리에 속한다."),
 ChunkWithIndex(start = 69, text = "MongoDB에서 사용하는 쿼리 언어와 CouchDB에서 사용하는 쿼리 언어는 서로 전혀 다르다. 그럼 에도 이 두 쿼리 언어는 같은 NoSQL 카테고리에 속한다. 어쨌거나 SQL이 아니기 때문이다."),
 ChunkWithIndex(start = 124, text = "그럼에도 이 두 쿼리 언어는 같은 NoSQL 카테고리에 속한다. 어쨌거나 SQL이 아니기 때문이다. 또한 NoSQL이 No RDBMS를 의미하지는 않는다. BerkleyDB같은 예외가 있기 때문이다."),
 ChunkWithIndex(start = 180, text = "또한 NoSQL이 No RDBMS를 의미하지는 않는다. BerkleyDB같은 예외가 있기 때문이다. 그리고 No RDBMS가 NoSQL인 것도 아니다. SQL호환 레이어를 제공하는 KV-store라는 예외가 역시 존재한다."),
 ChunkWithIndex(start = 236, text = "그리고 No RDBMS가 NoSQL인 것도 아니다. SQL호환 레이어를 제공하는 KV-store라는 예외가 역 시 존재한다. 물론 KV-store의 특징상 range query를 where절에 넣을 수 없으므로 완전한 SQL은 못 되고 SQL의 부분집합 정도를 제공한다.")]
```

### 3.3. Use every options used in `splitSentences`
- You can use the EVERY options used in `splitSentences`.
- For example, if you want to turn off the processing about quotation marks, you can set `useQuotesBracketsProcessing` the same as split_sentences.

```java
import kss.Kss;

Kss kss = new Kss();
String text = "NoSQL이라고 하는 말은 No 'English'라고 하는 말과 마찬가지다. 세상에는 영어 말고도 수많은 언어가 존재한다. MongoDB에서 사용하는 쿼리 언어와 CouchDB에서 사용하는 쿼리 언어는 서로 전혀 다르다. 그럼에도 이 두 쿼리 언어는 같은 NoSQL 카테고리에 속한다. 어쨌거나 SQL이 아니기 때문이다. 또한 NoSQL이 No RDBMS를 의미하지는 않는다. BerkleyDB같은 예외가 있기 때문이다. 그리고 No RDBMS가 NoSQL인 것도 아니다. SQL호환 레이어를 제공하는 KV-store라는 예외가 역시 존재한다. 물론 KV-store의 특징상 range query를 where절에 넣을 수 없으므로 완전한 SQL은 못 되고 SQL의 부분집합 정도를 제공한다.";
splitChunks(text, 128, false, true, false); // text maxLength, overlap, useHeuristic, useQuotesBracketsProcessing,
```
<br><br>

## 4. References
Kss is available in various programming languages.
- [Java version (this repo, ver 2.6.1)](https://github.com/sangdee/kss-java) is based on [Kss 2.6.0](https://github.com/hyunwoongko/kss/blob/main/docs/UPDATE.md#kss-260) and will be updated to 3.xx in the future.
- [Python version](https://github.com/hyunwoongko/kss) contains the most recent changes to Kss.
- [C++ version (ver 1.3.1)](https://github.com/likejazz/korean-sentence-splitter) has the original implementation of Kss but is deprecated now.
