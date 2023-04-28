package ddwu.mobile.finalproject.ma02_20201018;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class PharmacyXmlParser {
    final static String TAG = "Parse";

    private enum TagType { NONE, PADDR, PYADMNM, PTELNO};     // 해당없음, addr, hospitalNm, telno

    private final static String ITEM_TAG = "item";
    private final static String PADDR_TAG = "addr";
    private final static String PYADMNM_TAG = "yadmNm";
    private final static String PTELNO_TAG = "telno";

    private XmlPullParser parser;

    public PharmacyXmlParser() {
//        xml 파서 관련 변수들은 필요에 따라 멤버변수로 선언 후 생성자에서 초기화
//        파서 준비
        XmlPullParserFactory factory = null;
//        파서 생성
        try {
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

   public ArrayList<Pharmacy> parse(String xml) {
        ArrayList<Pharmacy> resultList = new ArrayList<Pharmacy>();
        Pharmacy dbo = null;
        PharmacyXmlParser.TagType tagType = PharmacyXmlParser.TagType.NONE;     //  태그를 구분하기 위한 enum 변수 초기화
        Log.d(TAG, "Start parse ");

        try {
            parser.setInput(new StringReader(xml));// 파싱 대상 지정
            int eventType = parser.getEventType();// 태그 유형 구분 변수 준비

            while(eventType!=XmlPullParser.END_DOCUMENT){ // parsing 수행 - for 문 또는 while 문으로 구성
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if(tag.equals(ITEM_TAG)){
                            dbo = new Pharmacy();
                        }else if(tag.equals(PADDR_TAG)){
                            tagType = PharmacyXmlParser.TagType.PADDR;
                        }else if(tag.equals(PYADMNM_TAG)){
                            tagType = PharmacyXmlParser.TagType.PYADMNM;
                        }else if(tag.equals(PTELNO_TAG)){
                            tagType = PharmacyXmlParser.TagType.PTELNO;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals(ITEM_TAG)){
                            resultList.add(dbo);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType){
                            case PADDR:
                                dbo.setpAddr(parser.getText());
                                break;
                            case PYADMNM:
                                dbo.setpYadmNm(parser.getText());
                                break;
                            case PTELNO:
                                dbo.setpTelno(parser.getText());
                                break;
                        }
                        tagType = PharmacyXmlParser.TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Finish parse ");
        return resultList;
    }
}


