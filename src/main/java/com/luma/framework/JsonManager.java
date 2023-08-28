package com.luma.framework;


import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JsonManager {
    private static JsonManager jsonManager= null;
    private static String fileName= null;
    static String fileSeparator = File.separator;
    Gson gson;
    String dataFile;
    Reader reader;

    private JsonManager(String fileName) {
        this.fileName= fileName;
        this.gson = new Gson();
        try {
            this.dataFile = System.getProperty("user.dir") + fileSeparator + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static JsonManager getInstance(String fileName) {
        if (jsonManager== null || !jsonManager.fileName.equalsIgnoreCase(fileName)) {
            jsonManager= new JsonManager(fileName);

        }
        return jsonManager;
    }

    public HashMap<String,String> getData(String environment,String program) {
        HashMap<String,String> programData = new HashMap<String,String>();
        try {
            reader = Files.newBufferedReader(Paths.get(this.dataFile));
            Map<?,?> map = this.gson.fromJson(reader,Map.class);
            for (Map.Entry<?,?> entry : map.entrySet()) {

                if (entry.getKey().toString().equalsIgnoreCase(environment)) {
                    for (Map.Entry<?,?> pData : ((Map<?,?>) entry.getValue()).entrySet()) {
                        if (pData.getValue() instanceof LinkedTreeMap) {
                            if (pData.getKey().toString().equalsIgnoreCase(program)) {
                                for (Map.Entry<?,?> programConfig : ((Map<?,?>) pData.getValue()).entrySet()) {
                                    programData.put(programConfig.getKey().toString(),
                                            programConfig.getValue().toString());
                                }
                            }
                        } else {
                            programData.put(pData.getKey().toString(),pData.getValue().toString());
                        }
                    }
                } else {
                    programData.put(entry.getKey().toString(),entry.getValue().toString());// Top Tree
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return programData;
    }

    public void cleanUp() {
        try {
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

