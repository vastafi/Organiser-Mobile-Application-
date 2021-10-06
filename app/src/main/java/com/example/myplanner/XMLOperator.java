package com.example.myplanner;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class XMLOperator {
    public static ArrayList<Planner> parseXml(Context context) {
        XmlPullParserFactory parserFactory;
        ArrayList<Planner> plannerList = null;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            FileInputStream fileInputStream;
            fileInputStream = context.openFileInput("data.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fileInputStream, null);
            plannerList = processParcing(parser);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return plannerList;
    }

    private static ArrayList<Planner> processParcing(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<Planner> plannerList = new ArrayList<>();
        int eventType = parser.getEventType();
        Planner planner = null;

        while(eventType != XmlPullParser.END_DOCUMENT) {
            String eltName;
            if (eventType == XmlPullParser.START_TAG) {
                eltName = parser.getName();

                if ("doc".equals(eltName)) {
                    planner = new Planner();
                    plannerList.add(planner);
                } else if (planner != null) {
                    if ("title".equals(eltName)) {
                        planner.title = parser.nextText();
                    } else if ("date".equals(eltName)) {
                        planner.date = parser.nextText();
                    } else if ("time".equals(eltName)) {
                        planner.time = parser.nextText();
                    }
                }
            }
            eventType = parser.next();
        }
        return plannerList;
    }
    public static void writeXml(Context context, ArrayList<Planner> plannerList) throws IOException {
        try {
            FileOutputStream fileOutputStream;
            File file = context.getApplicationContext().getFileStreamPath("data.xml");
            boolean exists = false;
            if(file.exists()) {
                exists = true;
            }
            fileOutputStream = context.openFileOutput("data.xml", Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            xmlSerializer.setOutput(writer);
            if(!exists) {
                xmlSerializer.startDocument("UTF-8", true);
            }
            for(Planner planner: plannerList) {
                xmlSerializer.startTag("", "doc");
                xmlSerializer.startTag("", "title");
                xmlSerializer.text(planner.title);
                xmlSerializer.endTag("", "title");
                xmlSerializer.startTag("", "date");
                xmlSerializer.text(planner.date);
                xmlSerializer.endTag("", "date");
                xmlSerializer.startTag("", "time");
                xmlSerializer.text(planner.time);
                xmlSerializer.endTag("", "time");
                xmlSerializer.endTag("", "doc");
            }
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileOutputStream.write(dataWrite.getBytes());
            fileOutputStream.close();

        } catch (IllegalArgumentException | IOException | IllegalStateException e) {
            e.printStackTrace();

        }
    }
}
