// AUTO_GENERATED BY JavaPoet
package output;

import testPackage.Circle;
import testPackage.ComplexObjects;
import testPackage.Shape;

public class Build implements Shape {
  private ComplexObjects co;

  public Build() {
    co = new ComplexObjects();
  }

  public void build() {
    co.add(new Circle());
  }

  public ComplexObjects gen() {
    return co;
  }
}