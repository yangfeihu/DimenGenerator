package cc.yann.android.dimengenerator.element;

import cc.yann.android.dimengenerator.dimen.Dimen;
import cc.yann.android.dimengenerator.dimen.DimenNameGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yann Chou
 * @mail zhouyanbin1029@gmail.com
 * @time 16/6/4.19:09
 */
public class NegativeDimenElement extends DimenElement {
    @Override
    public List<Dimen> generate(float scale) {
        List<Dimen> list = new ArrayList<Dimen>();
        for (int value = getStart(); value >= getEnd(); value--) {
            Dimen dimen = internalGenerate(scale, value);
            list.add(dimen);
        }
        return list;
    }

}
