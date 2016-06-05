package cc.yann.android.dimengenerator;

import cc.yann.android.dimengenerator.dimen.DefaultNegativeNameGenerator;
import cc.yann.android.dimengenerator.dimen.DefaultPositiveNameGenerator;
import cc.yann.android.dimengenerator.element.NegativeDimenElement;
import cc.yann.android.dimengenerator.element.PositiveDimenElement;
import cc.yann.android.dimengenerator.values.Values;
import cc.yann.android.dimengenerator.xml.DOMGenerator;
import cc.yann.android.dimengenerator.xml.SAXGenerator;
import cc.yann.android.dimengenerator.xml.XmlGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static final String ARGS_FOUT = "-o";
    static final String ARGS_SCREEN = "-s";
    static final String ARGS_BASE = "-b";

    static final String[] SUPPORT_ARGS = {ARGS_FOUT, ARGS_SCREEN, ARGS_BASE};

    static final String DEFAULT_SUPPORT_SCREEN = "800,480;854,480;960,540;960,640;1024,600;1024,720;1024,768;1196,720;1196,768;" +
            "1280,720;1280,800;1366,768;1812,1080;1920,1080;2048,1460;2560,1440;2560,1660;3840,2160;";

    public static void main(String[] args) {

        String ouput = null;
        String screens = null;
        int baseWidth = 0;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            boolean findArgs = false;
            for (String supportArg : SUPPORT_ARGS) {
                if (supportArg.equalsIgnoreCase(arg)) {
                    findArgs = true;
                }
            }
            if (findArgs && i + 1>= args.length) {
                printInvalid();
                continue;
            }
            if (ARGS_FOUT.equalsIgnoreCase(arg)) {
                ouput = args[i + 1];
            } else if (ARGS_SCREEN.equalsIgnoreCase(arg)) {
                screens = args[i+1];
            } else if (ARGS_BASE.equalsIgnoreCase(arg)) {
                try {
                    baseWidth = Integer.valueOf(args[i + 1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        if (ouput == null) {
            ouput = System.getProperty("user.dir");
        }

        if (screens == null) {
            screens = DEFAULT_SUPPORT_SCREEN;
        }

        if (!ouput.endsWith(File.separator)) {
            ouput = ouput + File.separator;
        }

        File outFile = new File(ouput);
        if (!outFile.exists()) {
            if (!outFile.mkdirs()) {
                System.err.println("Create out put directory failed!");
                return;
            }
        }

        List<Values> values = parseScreen(screens);
        if (values.isEmpty()) {
            System.err.println("Can not find any screen size!");
            printInvalid();
            return;
        }

        if (baseWidth == 0) {
            baseWidth = 1920;
        }

        SAXGenerator xmlGenerator = new SAXGenerator();

        genValuesFiles(values, xmlGenerator, ouput, baseWidth);

    }

    private static void genValuesFiles(List<Values> values, XmlGenerator xmlGenerator, String ouput, float default_width) {
        PositiveDimenElement positiveDimenElement = new PositiveDimenElement();
        NegativeDimenElement negativeDimenElement = new NegativeDimenElement();

        DefaultPositiveNameGenerator positiveNameGenerator = new DefaultPositiveNameGenerator();
        DefaultNegativeNameGenerator negativeNameGenerator = new DefaultNegativeNameGenerator();

        positiveDimenElement.setNameGenerator(positiveNameGenerator);
        negativeDimenElement.setNameGenerator(negativeNameGenerator);

        positiveDimenElement.setStart(1);
        positiveDimenElement.setEnd(1000);

        negativeDimenElement.setStart(-1);
        negativeDimenElement.setEnd(-100);

        for (Values value : values) {
            String fileName = genValuesName(value);
            String dicPath = ouput + fileName + File.separator;

            File dic = new File(dicPath);
            if (dic.exists()) {
                dic.delete();
            }
            dic.mkdirs();
            String dimenFile = dicPath + "px_dimen.xml";
            File dimen = new File(dimenFile);
            if (dimen.exists()) {
                dimen.delete();
            }
            try {
                dimen.createNewFile();
                value.setDimenFile(dimen);
                value.setScale((float) value.getWidth() / default_width);
                value.addDimenElement(positiveDimenElement);
                value.addDimenElement(negativeDimenElement);
                value.generate();
                xmlGenerator.generate(value);
            } catch (IOException e) {
                System.err.println("Can not create dimen file:"+e);
                System.exit(-1);
            }
        }
    }

    private static String genValuesName(Values values) {
        return "values-nodpi-"+values.getWidth()+"x"+values.getHeight();
    }

    private static List<Values> parseScreen(String screen) {
        String[] split = screen.split(";");
        List<Values> valuesList = new ArrayList<Values>();
        for (String scn : split) {
            String[] w_h = scn.split(",");
            if (w_h.length<2) {
                continue;
            }
            try {
                int width = Integer.valueOf(w_h[0]);
                int height = Integer.valueOf(w_h[1]);
                Values values = new Values(width, height);
                valuesList.add(values);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return valuesList;
    }

    private static void printInvalid() {
        System.err.println("invalid args\n" +
                "Usage: java -jar dimen-generator.jar -b <base width> -o <output path> -s <width,height;...;width,height;>");
    }


}
