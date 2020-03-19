package plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import java.util.Arrays;
import java.util.HashMap;

import generate.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClashDetect {
    static HashMap<PsiFile, String> fileNames = new HashMap<PsiFile, String>();
    final static Logger log = LoggerFactory.getLogger(ClashDetect.class);
    private String path;
    private Project project;

    ClashDetect(String path, Project project){
        this.path = path;
        this.project = project;
        this.getInitialFiles();
    }

    public void getInitialFiles(){
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(this.path);

//        data structure should be initialized to the classes
        if(virtualFile != null) {
            PsiDirectory psiDir = PsiManager.getInstance(this.project).findDirectory(virtualFile);

//        Create a PsiDirectory and call the getFiles method to retrieve all the files within the directory
            PsiFile[] ps = psiDir.getFiles();

            for(PsiFile p : ps){
                this.addToMap(p, "temp");
            }
        }
//        if package doesnt exist then data structure should be initialized []
        else{
            log.warn("Package doesnt exist");
        }
    }

    public void addToMap(PsiFile ps, String pack){
        log.debug("Inserting {} into data structure", ps);
        fileNames.put(ps, pack);
    }

    public boolean isFound(PsiFile ps) {

        return fileNames.containsValue(ps);
    }

}
