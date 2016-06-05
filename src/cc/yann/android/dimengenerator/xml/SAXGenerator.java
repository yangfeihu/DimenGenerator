package cc.yann.android.dimengenerator.xml;

import cc.yann.android.dimengenerator.dimen.Dimen;
import cc.yann.android.dimengenerator.values.Values;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author Yann Chou
 * @mail zhouyanbin1029@gmail.com
 * @time 16/6/5.18:20
 */
public class SAXGenerator implements XmlGenerator {

    @Override
    public void generate(Values values) throws IOException {
        File dimenFile = values.getDimenFile();

        FileOutputStream fos = new FileOutputStream(dimenFile);
        StreamResult result = new StreamResult(fos);
        SAXTransformerFactory sff = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        TransformerHandler th = null;
        try {
            th = sff.newTransformerHandler();
            Transformer transformer = th.getTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            th.setResult(result);

            th.startDocument();

            AttributesImpl attr = new AttributesImpl();
            char[] space = "\n".toCharArray();
            th.characters(space, 0, space.length);
            th.startElement("", "", "resources", attr);

            List<Dimen> dimens = values.getDimens();
            char[] spaceChar = "\n    ".toCharArray();
            for (Dimen dimen : dimens) {
                //white space
                th.characters(spaceChar, 0, spaceChar.length);

                //name attr
                attr.addAttribute("", "", "name", String.class.getName(), dimen.getName());

                th.startElement("", "", "dimen", attr);
                char[] valueChars = String.format("%spx", dimen.getValue()).toCharArray();
                th.characters(valueChars, 0, valueChars.length);

                th.endElement("", "", "dimen");

            }

            th.endElement("", "", "resources");

            th.endDocument();

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }

    }
}
