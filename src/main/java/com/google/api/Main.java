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

    String translatedText = Translate.DEFAULT.execute("���� ������ �� �ִ�! �ų������� ���� �� �ð��̾��� �ູ�d���� ���� ���� �͈���! #���� #����", Language.KOREAN, Language.ENGLISH);

    System.out.println(translatedText);
  }
}