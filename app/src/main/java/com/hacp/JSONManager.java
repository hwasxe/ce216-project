package com.hacp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JSONManager {

    private static final File defaultFile = new File("jsonArchive/catalog.json");

    static {
        File folder = new File("jsonArchive");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    public static List<Artifact> load() throws IOException {
        return load(defaultFile);
    }

    public static void save(List<Artifact> artifacts) throws IOException {
        save(artifacts, defaultFile);
    }

    public static List<Artifact> load(File file) throws IOException {
        if (!file.exists()) return Collections.emptyList();
        return mapper.readValue(file, new TypeReference<>() {});
    }

    public static void save(List<Artifact> artifacts, File file) throws IOException {
        writer.writeValue(file, artifacts);
    }
}


