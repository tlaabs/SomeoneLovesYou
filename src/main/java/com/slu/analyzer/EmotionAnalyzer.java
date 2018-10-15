package com.slu.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import com.google.api.GoogleAPI;
import com.google.api.GoogleAPIException;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import com.twitter.penguin.korean.TwitterKoreanProcessorJava;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;

/**
 * Hello world!
 *
 */
public class EmotionAnalyzer 
{
	public static AnalyzeResult run(String txt) throws GoogleAPIException, JWNLException {

		/*
		 * try { // initialize JWNL (this must be done before JWNL can be used)
		 * JWNL.initialize(); // new Examples().go();
		 * 
		 * } catch (Exception ex) { ex.printStackTrace(); System.exit(-1); }
		 */

		String text = txt;

		System.out.println("원본: " + text);

		// Normalize
		CharSequence normalized = TwitterKoreanProcessorJava.normalize(text);
		System.out.println("정규화: " + normalized);

		String key = "AIzaSyCkEb0LyEkRkYgHIBRzKbaPf0Mg5Xop1l8";
		GoogleAPI.setHttpReferrer("https://developers.google.com/console/help/new/");

		GoogleAPI.setKey(key);
		String normalized2 = normalized.toString();
		String translatedText = Translate.DEFAULT.execute(normalized2, Language.KOREAN, Language.ENGLISH);
		System.out.println(translatedText);// 번역된 문장

		// 토큰화하여 words 배열에 저장a
		String[] words2 = translatedText.split("\\s");

		// 특수문자 제거
		int k = 0;
		for (String wo : words2) {
			words2[k] = getSTRFilter(wo);
			k++;
		}
		final ArrayList<String> list = new ArrayList<String>();
		Collections.addAll(list, words2);
		list.remove("");

		k = 0;
		VerbLemmatizer VerbLemmatizer = new VerbLemmatizer();
		for (String wo : words2) {
			words2[k] = VerbLemmatizer.lemmatize(wo);
			System.out.println(words2[k]);
			k++;
		}

		String result = "";
		for (int i = 0; i < words2.length; i++) {
			result = result.concat(words2[i] + " ");
		}
		System.out.println(result);

		// 2차원 배열 생성
		String[][] word = null;
		// 배열의 행을 동적 할당
		word = new String[1034][];

		try {
			// 파일 객체 생성
			String path = EmotionAnalyzer.class.getResource("").getPath();
			File file = new File(path + "dictionary.txt");
			if(file.exists()) System.out.println("존재");
			else System.out.println("존재하지 않음");
			// 입력 스트림 생성
			FileReader fileReader = new FileReader(file);
			// 입력 버퍼 생성
			BufferedReader bufReader = new BufferedReader(fileReader);
			String line = "";

			int row = 0;

			while ((line = bufReader.readLine()) != null) {
				// 한 줄 읽어와서 띄어쓰기 단위 토큰화
				StringTokenizer st = new StringTokenizer(line);
				// 토큰화 갯수만큼 열을 동적 할당
				word[row] = new String[st.countTokens()];
				int col = 0;
				while (st.hasMoreTokens()) {
					// 토큰화된 단어 2차원 배열에 넣기
					word[row][col] = st.nextToken();
					col++;
				}
				row++;
			}

			// .readLine()은 끝에 개행 문자를 읽지 않는다.
			bufReader.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			System.out.println(e);
		}

		StringTokenizer st = new StringTokenizer(result);
		String[] testWord = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			// 토큰화된 단어 배열에 넣기
			testWord[i] = st.nextToken();
			i++;
		}
		double valance = 0, arousal = 0, avgValance = 0, avgArousal = 0;
		int count = 0;

		// break문 추가해야해
		for (i = 0; i < testWord.length; i++) {
			for (int r = 0; r < word.length; r++) {
				for (int c = 2; c < word[r].length; c++) {
					if (testWord[i].equalsIgnoreCase(word[r][c])) {
						System.out.println(word[r][c]);
						valance += Double.parseDouble(word[r][0]);
						arousal += Double.parseDouble(word[r][1]);
						count++;
					}
				}
			}
		}

		avgValance = valance / count;
		avgArousal = arousal / count;

		System.out.println(avgValance);
		System.out.println(avgArousal);
		
		AnalyzeResult rs = new AnalyzeResult(avgValance, avgArousal, null);
		
		if (avgValance >= 5 && avgArousal >= 5) {
//			System.out.println("Happy / Exciting");
			System.out.println("행복");
			rs.setEmotion("행복");
		} else if (avgValance < 5 && avgArousal >= 5) {
//			System.out.println("Anxious / Angry");
			System.out.println("불안");
			rs.setEmotion("불안");
		} else if (avgValance < 5 && avgArousal < 5) {
//			System.out.println("Sad / Depressed");
			System.out.println("슬픔");
			rs.setEmotion("슬픔");
		} else {
//			System.out.println("Relaxed / Calm");
			System.out.println("평온");
			rs.setEmotion("평온");
		}
		
		return rs;

	}

	public static String getSTRFilter(String str) {

		String str_imsi = "";

		String[] filter_word = { "\\p{Z}", "", "\\.", "\\?", "\\/", ">\\~", "\\!", "\\@", "\\#", "\\$", "\\%", "\\^",
				"\\&", "\\*", "\\(", "\\)", "\\_", "\\+", "\\=", "\\|", "\\\\", "\\}", "\\]", "\\{", "\\[", "\\\"",
				"\\'", "\\:", "\\;", "\\<", "\\,", "\\>", "\\.", "\\?", "\\/" };

		for (int i = 0; i < filter_word.length; i++) {
			// while(str.indexOf(filter_word[i]) >= 0){
			str_imsi = str.replaceAll(filter_word[i], "");
			str = str_imsi;
			// }
		}
		return str;
	}

	public static int[] polysemy(String word) throws JWNLException {
		int[] polysemies = new int[4];
		int poly = -1;
		String wordclass = "";

		Dictionary d = Dictionary.getInstance();
		IndexWord noun_form = d.getIndexWord(POS.NOUN, word);
		polysemies[0] = (noun_form == null) ? 0 : noun_form.getSenses().length;
		IndexWord verb_form = d.getIndexWord(POS.VERB, word);
		polysemies[1] = (verb_form == null) ? 0 : verb_form.getSenses().length;
		IndexWord adj_form = d.getIndexWord(POS.ADJECTIVE, word);
		polysemies[2] = (adj_form == null) ? 0 : adj_form.getSenses().length;
		IndexWord adv_form = d.getIndexWord(POS.ADVERB, word);
		polysemies[3] = (adv_form == null) ? 0 : adv_form.getSenses().length;
		if ((polysemies[0] > polysemies[1]) && (polysemies[0] > polysemies[2]) && (polysemies[0] > polysemies[3])) {
			poly = 1;// poly=1 -> noun
			wordclass = "noun";
		} else if ((polysemies[1] > polysemies[0]) && (polysemies[1] > polysemies[2])
				&& (polysemies[1] > polysemies[3])) {
			poly = 2;// poly=2 -> verb
			wordclass = "verb";
		} else if ((polysemies[2] > polysemies[0]) && (polysemies[2] > polysemies[1])
				&& (polysemies[2] > polysemies[3])) {
			poly = 3;// poly=3 -> adjective
			wordclass = "adjective";
		} else if ((polysemies[3] > polysemies[0]) && (polysemies[3] > polysemies[1])
				&& (polysemies[3] > polysemies[2])) {
			poly = 4;// poly=4 -> adverb
			wordclass = "adverb";
		}
		System.out.print(wordclass + " ");

		return polysemies;
	}
}
