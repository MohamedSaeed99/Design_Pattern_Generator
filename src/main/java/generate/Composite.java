package generate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Composite extends Pattern {
    final static Logger log = LoggerFactory.getLogger(Composite.class);
    //    instantiates arrayList of designPatterns
    private ArrayList<Pattern> designPatterns = new ArrayList<Pattern>();

    //    Loops all through the design patterns and generates them
    @Override
    public void generateCode(String pack, String path){
        for(Pattern designs : designPatterns){
            log.debug("Generating {}", designs.toString().split("@")[0]);
            designs.generateCode(pack, path);
        }
        if(designPatterns.size() > 0) {
            genClient("Client", pack, path);
            log.info("Generated Client class");
        }
    }

    //    adds design patterns to the data structure arrayList
    public void add(Pattern design){
        designPatterns.add(design);
        log.debug("Added {} to list", design.toString().split("@")[0]);
    }

    //    Returns the list of added patterns
    public ArrayList<Pattern> patternsAdded(){
        return designPatterns;
    }
}
