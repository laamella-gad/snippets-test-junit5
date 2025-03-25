import java.util.Arrays;
import java.util.Comparator;

public class Service {
    public void sortSourceClasses(SourceClass[] sourceClasses) {
        Arrays.sort(sourceClasses, Comparator.comparing(SourceClass::getIntValue));
    }
}
