package com.comicshack.comic;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.comicshack.tools.FileArchive;
import com.comicshack.comic.ComicPage;

/**
 *  Comic is intended as the container class for comic books
 *  Should be the class we use to get anything about a specific comic,
 *  Should be kept in a comic library
 *  should contain at least the info we need when browsing through different comics
 *  should be used as the container for any cached data for a specific comic (thumbnails etc)
 */
public class Comic
{
    private String title;
    private String filename;
    private FileArchive archive;
    private ComicPage currentpage;
    //add more fields to match





}