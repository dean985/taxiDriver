package utils;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import GameUtils.Robot;
import dataStructure.NodeData;
import dataStructure.node_data;
import gameClient.myGameGUI;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.util.Iterator;


public class KML_Logger
{
    public KML_Logger(myGameGUI mgg)
    {
        String temp ="";
        DocumentBuilderFactory dbFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {

            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();



            Element supercar = doc.createElement("kml");
            doc.appendChild(supercar);

            // setting attribute to element

            supercar.setAttribute("xmlns","http://www.opengis.net/kml/2.2");
            supercar.setAttribute("xmlns:gx","http://www.google.com/kml/ext/2.2");
            supercar.setAttribute("xmlns:kml","http://www.opengis.net/kml/2.2");
            supercar.setAttribute("xmlns:atom","http://www.w3.org/2005/Atom");

            Element document = doc.createElement("Document");
            supercar.appendChild(document);

            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode("sencerio_" + mgg.getScenario()+".kml"));
            document.appendChild(name);

            Element open = doc.createElement("open");
            open.appendChild(doc.createTextNode("1"));
            document.appendChild(open);

            ///////// style /////////////////

            Element Style = doc.createElement("Style");
            doc.appendChild(Style);
            Style.setAttribute("id","Robots_style");

                Element IconStyle =  doc.createElement("IconStyle");
                    Element scale = doc.createElement("scale");
                    scale.appendChild(doc.createTextNode("1.2"));
                    IconStyle.appendChild(scale);

                    Element Icon = doc.createElement("Icon");
                        Element href  = doc.createElement("href");
                        href.appendChild(doc.createTextNode("https://cdn0.iconfinder.com/data/icons/black-logistics-icons/256/Robot_head.png"));
                IconStyle.appendChild(Icon);

            Style.appendChild(IconStyle);


            Element placemark = doc.createElement("Placemark");
                name = doc.createElement("name");
                name.appendChild(doc.createTextNode("sencerio_" + mgg.getScenario()));
                placemark.appendChild(name);

                name = doc.createElement("styleUrl");
                name.appendChild(doc.createTextNode("#m_ylw-pushpin0"));
                placemark.appendChild(name);

                    Element LineString = doc.createElement("LineString");
                        name = doc.createElement("tessellate");
                        name.appendChild(doc.createTextNode("1"));
                        LineString.appendChild(name);
                        name = doc.createElement("coordinates");

                        ///////// draw edge //////////

            Iterator<node_data> iter = mgg.getdGraph().getV().iterator();
            String s= iter.next().getLocation().toString();
                        temp += s +" ";
                        while (iter.hasNext())
                        {
                            temp += iter.next().getLocation().toString()+" ";
                        }
                        temp +=s;
                        name.appendChild(doc.createTextNode(temp));
                        LineString.appendChild(name);
            placemark.appendChild(LineString);
            document.appendChild(placemark);

            Element placemark2 ;

            /////////////// draw the node ///////////////////

            iter = mgg.getdGraph().getV().iterator();
            while (iter.hasNext())
            {
                placemark2 = doc.createElement("Placemark");
                Element Point = doc.createElement("Point");
                name = doc.createElement("coordinates");
                name.appendChild(doc.createTextNode(iter.next().getLocation().toString()));
                Point.appendChild(name);
                placemark2.appendChild(Point);
                document.appendChild(placemark2);
            }

            Iterator<Robot> robotIterator =  mgg.getGame_robots().getAllRobots().iterator();


            ////////////////// draw the robots ///////////////

            while (robotIterator.hasNext())
            {

                placemark2 = doc.createElement("Placemark");
                Element style_mark = doc.createElement("styleUrl");
                style_mark.appendChild(doc.createTextNode("Robots_style"));
                Element Point = doc.createElement("Point");
                name = doc.createElement("coordinates");
                name.appendChild(doc.createTextNode(robotIterator.next().getLocation().toString()));
                Point.appendChild(name);
                placemark2.appendChild(Point);
                document.appendChild(placemark2);
            }




            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File("data/"+"sencerio_" + mgg.getScenario()+".kml"));
            transformer.transform(source, result);

            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);



        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        KML_Logger kml_logger = new KML_Logger(new myGameGUI(19));
    }


}
