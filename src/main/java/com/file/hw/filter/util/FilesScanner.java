package com.file.hw.filter.util;

import com.file.hw.filter.chain.FileFilterChain;
import com.file.hw.filter.impl.SuffixSpecifiedFileFilter;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * 
 * @author Administrator
 * 
 */
public class FilesScanner
{

    private FileFilterChain fileFilterChain;

    /***
     * @param suffix : such as "java"
     * @param file   : File or file path
     * @return : ArrayList<File>
     */
    public static ArrayList<File> getAllFilesFiltSuffix(String suffix,
                                                        Object file) {
        FilesScanner filesScanner = new FilesScanner();
        FileFilterChain fileFilterChain = new FileFilterChain();
        fileFilterChain.add(new SuffixSpecifiedFileFilter(suffix));
        filesScanner.setFileFilterChain(fileFilterChain);
        return filesScanner.getAllFilesAfterFilt(file);
    }

    /**
     * @param directory
     * @return
     */
    public static ArrayList<File> getAllFiles(String directory)
    {
        if (!FileUtils.isFile(directory)) {
            return null;
        }
        File file = new File(directory);
        return getAllFiles(file);
    }

    /**
     * @param obj ��e:/test/a.txt��
     * @return
     */
    public static ArrayList<File> getAllFiles(File directory) {
        if (null == directory) {
            return null;
        }
        ArrayList<File> files = new ArrayList<File>();
        if (directory.isFile())
        {
            files.add(directory);
            return files;
        }
        else if (directory.isDirectory())
        {
            File[] fileArr = directory.listFiles();
            if(!ValueWidget.isNullOrEmpty(fileArr)){
            	for (int i = 0; i < fileArr.length; i++)
                {
                    File fileOne = fileArr[i];
                    files.addAll(getAllFiles(fileOne));
                }
            }
        }
        else
        {
            System.out.println("");
        }
        return files;
    }

    public static void main(String[] args) throws Exception {
        //        FileUtils.printFileList(getAllFilesFiltSuffix("java","e:/test/cde"));
        String filepath = "G:\\NA52QXHQ\\study\\计算机体系结构.zip";
        System.out.println(new File(filepath).length());

        FileInputStream fis = null;
        fis = new FileInputStream(filepath);
        System.out.println("File has " + fis.available() + " bytes");

    }

    public void setFileFilterChain(FileFilterChain fileFilterChain) {
        this.fileFilterChain = fileFilterChain;
    }

    /**
     *
     * @param obj : file or file path
     * @return : ArrayList<File>
     */
    public ArrayList<File> getAllFilesAfterFilt(Object obj) {
        if (null == fileFilterChain)
            fileFilterChain = new FileFilterChain();
        File directory = null;
        if (obj instanceof File) {
            directory = (File) obj;
        } else {
            directory = new File(obj.toString());
        }
        ArrayList<File> files = new ArrayList<File>();
        if (directory.isFile()) {
            if (fileFilterChain.isRight(directory)) {

                files.add(directory);
            }
            return files;
        } else if (directory.isDirectory()) {
            File[] fileArr = directory.listFiles();
            if (!ValueWidget.isNullOrEmpty(fileArr)) {
                for (int i = 0; i < fileArr.length; i++) {
                    File fileOne = fileArr[i];
                    files.addAll(getAllFilesAfterFilt(fileOne));
                }
            }
        } else {
        }
        return files;
    }

}
