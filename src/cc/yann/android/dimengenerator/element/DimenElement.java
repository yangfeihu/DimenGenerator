package cc.yann.android.dimengenerator.element;

import cc.yann.android.dimengenerator.dimen.Dimen;
import cc.yann.android.dimengenerator.dimen.DimenNameGenerator;
import cc.yann.android.dimengenerator.transform.ElementTransformer;

import java.util.List;

/**
 * @author Yann Chou
 * @mail zhouyanbin1029@gmail.com
 * @time 16/6/4.19:03
 */
public abstract class DimenElement {
    private int start;
    private int end;
    private ElementTransformer transformer;
    private DimenNameGenerator nameGenerator;

    public void setTransformer(ElementTransformer transformer) {
        this.transformer = transformer;
    }

    public ElementTransformer getTransformer() {
        return transformer;
    }

    public void setNameGenerator(DimenNameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public abstract List<Dimen> generate(float scale);

    protected Dimen internalGenerate(float scale, int value) {
        float target = value * scale;
        target = transform(target, transformer);
        Dimen dimen = new Dimen();
        dimen.setValue(target);
        dimen.setName(nameGenerator.generate(value));
        return dimen;
    }

    private float transform(float value, ElementTransformer transformer) {
        if (transformer != null) {
            value = transformer.transform(value);
        }
        return value;
    }

}
