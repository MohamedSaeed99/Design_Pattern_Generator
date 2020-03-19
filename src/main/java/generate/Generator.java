package generate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//generates the deign patterns specified
public class Generator {
    protected static final Logger log = LoggerFactory.getLogger(Generator.class);
    //    Uses a Facade to generate the different design pattern
    public void generate(String designPattern, String configPackage, String configPath){

//        instantiates factory to create instances of the design pattern
        Factory factory = new Factory();
        Composite co = new Composite();
        Pattern pat;

        pat = factory.designPattern(designPattern);
        if(pat != null){
            co.add(pat);
        }
//        generates code for all design patterns specified
        co.generateCode(configPackage, configPath);
    }
}
