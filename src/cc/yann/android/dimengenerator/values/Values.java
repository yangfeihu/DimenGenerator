package cc.yann.android.dimengenerator.values;

import cc.yann.android.dimengenerator.dimen.Dimen;
import cc.yann.android.dimengenerator.element.DimenElement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yann Chou
 * @mail zhouyanbin1029@gmail.com
 * @time 16/6/4.18:41
 */
public class Values {

    private int width;
    private int height;
    private float scale;
    private File dimenFile;
    private List<Dimen> dimens;
    private List<DimenElement> elements;

    public Values(int width, int height) {
        this.width = width;
        this.height = height;
        elements = new ArrayList<DimenElement>();
    }

    public void addDimenElement(DimenElement element) {
        elements.add(element);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public File getDimenFile() {
        return dimenFile;
    }

    public void setDimenFile(File dimenFile) {
        this.dimenFile = dimenFile;
    }

    public List<Dimen> getDimens() {
        return dimens;
    }

    public List<Dimen> generate() {
        dimens = new ArrayList<Dimen>();
        for (DimenElement element : elements) {
            List<Dimen> generate = element.generate(scale);
            dimens.addAll(generate);
        }
        return dimens;
    }

}
