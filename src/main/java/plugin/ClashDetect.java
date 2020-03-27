package plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import generate.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClashDetect {

//    static filenames to make it consistent throughout the program
    static HashMap<String, String> fileNames;
    final static Logger log = LoggerFactory.getLogger(ClashDetect.class);

    static boolean createdStructure = false;

//    public static path and project allows for the access of these throughout the program
//    necessary because when program runs it has 1 project and path variable
    public static String path;
    public static Project project;

    public ClashDetect(){
        if(!createdStructure){
            this.fileNames = new HashMap<String, String>();
            this.createdStructure = true;
        }
    }

    public ClashDetect(String path, Project project){
        if(!createdStructure) {
            this.fileNames = new HashMap<String, String>();
            createdStructure = true;
        }
        this.path = path;
        this.project = project;
        this.getInitialFiles();
    }

//    gets all the files present in the directory specified by the user, configuration file
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

//    Adds string format of file name and directory into the datastructor
    public void addToMap(String name, String ps){
        log.debug("Inserting {} into data structure", name);
        fileNames.put(name, ps);
    }

//    checks if the file with the same directory already exists
    public boolean isFound(String name, String dir) {
        if(fileNames.containsKey(name)){
            if(fileNames.get(name).equals(dir)){
                log.info("File name: {} is found", name);
                return true;
            }
        }
        return false;
    }

//    looks for missing files
    public void updateFiles(){
//        go through hash map and compare with the new files retrieved
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(this.path);

//        data structure should be initialized to the classes
        if(virtualFile != null) {
            PsiDirectory psiDir = PsiManager.getInstance(this.project).findDirectory(virtualFile);

//        Create a PsiDirectory and call the getFiles method to retrieve all the files within the directory
            PsiFile[] ps = psiDir.getFiles();
            ArrayList<String> files = new ArrayList<String>();
            ArrayList<String> removedFiles = new ArrayList<String>();

//            converts psi into strings
            for(int i = 0; i < ps.length; i++){
                files.add(ps[i].getName());
            }

//            checks and updates the filename data structure
            for(Map.Entry<String, String> mp : fileNames.entrySet()){
                if(!files.contains(mp.getKey())){
                    removedFiles.add(mp.getKey());
                }
            }

//            removes the key from map
            removeKey(removedFiles);
        }
//        if package doesnt exist then data structure should be initialized []
        else{
            log.warn("Package was deleted");
            removeAll();
        }
    }

//    clears the hash map
    public void removeAll(){
        log.info("Removing all elements in data structure");
        this.fileNames.clear();
    }

//    removes specific elements/keys from map
    public void removeKey(ArrayList<String> key){
        for(String k : key) {
            log.info("Removing {} from data structure", k);
            this.fileNames.remove(k);
        }
    }

//    retrieves the list of files accumulated
    public HashMap<String, String> listOfFiles(){
        return fileNames;
    }
}
