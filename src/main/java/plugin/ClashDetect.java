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
    static HashMap<String, String> fileNames;
    final static Logger log = LoggerFactory.getLogger(ClashDetect.class);
    public static String path;
    public static Project project;

    public ClashDetect(){
        this.fileNames = new HashMap<String, String>();
    }

    public ClashDetect(String path, Project project){
        this.fileNames = new HashMap<String, String>();
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
                this.addToMap(p.getName(), this.path);
            }
        }
//        if package doesnt exist then data structure should be initialized []
        else{
            log.warn("Package doesnt exist");
        }
    }

    public void addToMap(String name, String ps){
        log.debug("Inserting {} into data structure", name);
        fileNames.put(name, ps);
    }

    public boolean isFound(String name, String dir) {
        if(fileNames.containsKey(name)){
            if(fileNames.get(name).equals(dir)){
                return true;
            }
        }
        return false;
    }

    public HashMap<String, String> listOfFiles(){
        return fileNames;
    }
}
