// AUTO_GENERATED BY JavaPoet
package output;

import java.util.ArrayList;
import java.util.List;
import testPackage.Circle;
import testPackage.Shape;

public class ComplexObjects {
  private List<Shape> list;

  public ComplexObjects() {
    list = new ArrayList<Shape>();
  }

  public void add(Circle child) {
    list.add(child);
  }
}