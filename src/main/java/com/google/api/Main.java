package com.google.api;


import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class Main {
  public static void main(String[] args) throws Exception {
    // Set the HTTP referrer to your website address.
     
    GoogleAPI.setHttpReferrer("https://developers.google.com/console/help/new/");

    // Set the Google Translate API key
    // See: http://code.google.com/apis/language/translate/v2/getting_started.html
    GoogleAPI.setKey("AIzaSyCkEb0LyEkRkYgHIBRzKbaPf0Mg5Xop1l8");

    String translatedText = Translate.DEFAULT.execute("드디어 마지막 한 주다! 신난닼ㅋㅋ 보람 찬 시간이었다 행복햏ㅎㅎ 여행 가고 싶닼ㅋ! #방학 #여행", Language.KOREAN, Language.ENGLISH);

    System.out.println(translatedText);
  }
}