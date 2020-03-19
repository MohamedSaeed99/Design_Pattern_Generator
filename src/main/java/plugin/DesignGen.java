package plugin;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import CustomInput.MainContent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class DesignGen implements ToolWindowFactory {
    final static Logger log = LoggerFactory.getLogger(DesignGen.class);

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

//        tries to get directory if not found then sets to default which is "/src/main/java"
        String[] conf = getDirectory();
        if(conf == null){
            conf = new String[]{"/src/main/java", "GeneratedCode"};
            log.info("Using default configuration");
        }

//        Creates the contents of the ToolWindow
        MainContent mc = new MainContent(project.getBasePath()+conf[0], conf[1]);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mc.main(), "Design Patterns", false);

//        Displays the content
        toolWindow.getContentManager().addContent(content);

        log.info("Dockable Window Pane is set");


//        gets the absolute directory where files well be stored
        String path = project.getBasePath()+conf[0] + "/"+conf[1];

//        sets up the initial hashmap with the files already there
        ClashDetect cd = new ClashDetect(path, project);
    }

    //    Retrieves path and package from the configuration file
    public String[] getDirectory(){
        String[] conf = new String[]{"", ""};
        try {
            InputStream inStream = DesignGen.class.getResourceAsStream("/application.conf");
            if (inStream != null) {
                Reader reader = new InputStreamReader(inStream);

                // Load the configuration file
                Config config = ConfigFactory.parseReader(reader);

                // Load the destination directory for designs being generated
                conf[0] = config.getString("pathDir");
                conf[1] = config.getString("pack");

                log.info("Path: {}", conf[0]);
                log.info("Package Name: {}", conf[1]);
                inStream.close();

                return conf;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("IOException: {}", e.getMessage());
        }
        return null;
    }
}
