package com.comicshack.tools;

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

    // Unrar error codes.
    private static final int UNRAR_ARCHIVE_MUST_BE_UNLOCKED = 4;
    private static final int UNRAR_CANNOT_CREATE_FILE = 9;
    private static final int UNRAR_CANNOT_OPEN_FILE = 6;
    private static final int UNRAR_CANNOT_PROCESS_COMMAND_LINE_OPTION = 7;
    private static final int UNRAR_CANNOT_WRITE = 5;
    private static final int UNRAR_CHECKSUM_ERROR = 3;
    private static final int UNRAR_FATAL_ERROR = 2;
    private static final int UNRAR_INTERRUPTED = 255;
    private static final int UNRAR_OUT_OF_MEMORY = 8;
    private static final int UNRAR_WARNING = 1;



}
