package com.comicshack.tools;

import com.comicshack.comic.ComicPage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;




import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;



public class FileArchive {

    public static final String TYPE_PDF = "pdf";
    public static final String TYPE_RAR = "rar";
    public static final String TYPE_ZIP = "zip";
    private static final int BUFFER_SIZE = 65536;

    /**
     *  Magic bytes to identify a PDF archive.
     */
    private static final String MAGIC_PDF = "%PDF";
    private static final int MAGIC_PDF_LENGTH = MAGIC_PDF.length();

    /**
     *  Magic bytes to identify a RAR archive.
     */
    private static final String MAGIC_RAR = "Rar!";
    private static final int MAGIC_RAR_LENGTH = MAGIC_RAR.length();

    /**
     *  Magic bytes to identify a ZIP archive.
     */
    private static final String MAGIC_ZIP = "PK";
    private static final int MAGIC_ZIP_LENGTH = MAGIC_ZIP.length();

    private File archiveFile;
    private String filename;
    private String fileType;
    int progress;
    int totalEntries;
    int currentEntry;

    public FileArchive(File newFile) {
        this.archiveFile = newFile;

        //find other parameters on your own

    }

    public FileArchive(File archiveFile, int currentEntry) {
        this.archiveFile = archiveFile;
        this.progress = progress;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getFilename() {
        return filename;
    }

    public ComicPage getCurrentZipEntry() throws IOException{


        long dataSize;
        ComicPage page;
        byte[] data;

        ZipFile zipFile = new ZipFile(archiveFile);

        try {

            Enumeration zipEntries = zipFile.getEntries();


            ZipEntry entry = (ZipEntry) zipEntries.nextElement();

            for (int i = 1; i <= currentEntry; i++) {

                entry = (ZipEntry) zipEntries.nextElement();

            }

            dataSize = entry.getSize();
            data = new byte[(int)dataSize];

            InputStream in = zipFile.getInputStream(entry);
            try{

                BufferedInputStream inBuffered = new BufferedInputStream(in);
                try{
                    int bytesRead;
                    while((bytesRead = inBuffered.read(data, 0, (int)dataSize)) != -1){};


                } finally {
                    inBuffered.close();
                }
            } finally {
                in.close();
            }
        }finally {
            zipFile.close();
        }


        page = new ComicPage(data, dataSize);
        return page;
    }


}
