package cc.yann.android.dimengenerator.dimen;

/**
 * @author Yann Chou
 * @mail zhouyanbin1029@gmail.com
 * @time 16/6/4.20:50
 */
public class DefaultPositiveNameGenerator implements DimenNameGenerator {
    @Override
    public String generate(int value) {
        return "px_"+value;
    }
}
