package com.poledisplayapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLParser {

    public String getXmlFromUrl(String url, List<NameValuePair> data) {
        String xml = "";

        try {

            HttpPost httpPost = new HttpPost(url);// replace

            httpPost.setHeader("Authorization",
                    "Content-Type: application/x-www-form-urlencoded");

            try {

                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                        data);

                httpPost.setEntity(urlEncodedFormEntity);

                try {
                    HttpResponse httpResponse = Constant.httpClient
                            .execute(httpPost);
                    InputStream inputStream = httpResponse.getEntity()
                            .getContent();
                    InputStreamReader inputStreamReader = new InputStreamReader(
                            inputStream);
                    BufferedReader bufferedReader = new BufferedReader(
                            inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String bufferedStrChunk = null;
                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                    }

                    xml = stringBuilder.toString();

                    return xml;

                } catch (ClientProtocolException cpe) {
                   //Log.d("TEST", "First Exception coz of HttpResponese :"
                            //+ cpe);
                    cpe.printStackTrace();
                    return xml;
                } catch (IOException ioe) {
                   //Log.d("TEST", "Second Exception coz of HttpResponse :"
                           // + ioe);
                    ioe.printStackTrace();
                    return xml;
                }

            } catch (UnsupportedEncodingException uee) {
               //Log.d("TEST",
                        //"An Exception given because of UrlEncodedFormEntity argument :"
                                //+ uee);
                uee.printStackTrace();
                return xml;
            }
        } catch (Exception e) {

           //Log.d("TEST",
                 //   "An Exception given because of UrlEncodedFormEntity argument :"
                   //         + e);
            e.printStackTrace();
            return xml;
        }
        // return XML

    }

    public String getXmlFromUrlTest(String url) {
        String xml = "";

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);// replace

            httpPost.setHeader("Authorization",
                    "Content-Type: application/x-www-form-urlencoded");

            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);
                InputStream inputStream = httpResponse.getEntity().getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(
                        inputStream);
                BufferedReader bufferedReader = new BufferedReader(
                        inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String bufferedStrChunk = null;
                while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                    stringBuilder.append(bufferedStrChunk);
                }

                xml = stringBuilder.toString();

                return xml;

            } catch (ClientProtocolException cpe) {
               //Log.d("TEST", "First Exception coz of HttpResponese :" + cpe);
                cpe.printStackTrace();
                return xml;
            } catch (IOException ioe) {
               //Log.d("TEST", "Second Exception coz of HttpResponse :" + ioe);
                ioe.printStackTrace();
                return xml;
            }

        } catch (Exception e) {

           //Log.d("TEST",
                    //"An Exception given because of UrlEncodedFormEntity argument :"
                            //+ e);
            e.printStackTrace();
            return xml;
        }
        // return XML

    }

    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
           //Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
           //Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
           //Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child
                        .getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

}
