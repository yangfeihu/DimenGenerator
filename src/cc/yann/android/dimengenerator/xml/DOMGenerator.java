package cc.yann.android.dimengenerator.xml;

import cc.yann.android.dimengenerator.values.Values;
import cc.yann.android.dimengenerator.dimen.Dimen;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author Yann Chou
 * @mail zhouyanbin1029@gmail.com
 * @time 16/6/4.21:00
 */
public class DOMGenerator implements XmlGenerator {

    private static DocumentBuilder sDocumentBuilder = null;

    static {
        try {
            sDocumentBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generate(Values values) throws IOException{
        File dimenFile = values.getDimenFile();
        Document document = sDocumentBuilder.newDocument();
        document.setXmlStandalone(true);
        Element root = document.createElement("resources");
        List<Dimen> dimens = values.getDimens();
        for (Dimen dimenItem : dimens) {
            Element dimenElement = document.createElement("dimen");

            Attr name = document.createAttribute("name");
            name.setValue(dimenItem.getName());

            Text value = document.createTextNode(String.format("%spx", dimenItem.getValue()));

            dimenElement.setAttributeNode(name);
            dimenElement.appendChild(value);
            root.appendChild(document.createTextNode("\n    "));
            root.appendChild(dimenElement);
        }
        document.appendChild(root);

        FileOutputStream fos = new FileOutputStream(dimenFile);
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StreamResult result = new StreamResult(fos);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
    }
}
