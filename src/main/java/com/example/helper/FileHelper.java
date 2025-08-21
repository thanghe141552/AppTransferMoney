package com.example.helper;

import java.util.List;

public interface FileHelper<T> {

    void writeToFile(String fileName, List<T> content) ;

    List<T> readFromFile(String fileName, EntityParseHelper<T> parseHelper);
}
