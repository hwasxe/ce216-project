package com.hapc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JSONManager {

    private static final File file = new File("catalog.json");

    public static void save(List<com.hapc.Artifact> artifacts) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("[\n");
            for (int i = 0; i < artifacts.size(); i++) {
                com.hapc.Artifact a = artifacts.get(i);
                writer.write("  {\n");
                writer.write("    \"id\": \"" + a.getArtifactId() + "\",\n");
                writer.write("    \"name\": \"" + a.getArtifactName() + "\",\n");
                writer.write("    \"description\": \"" + a.getCategory() + "\",\n");
                writer.write("    \"era\": \"" + a.getCivizilation() + "\",\n");
                writer.write("    \"origin\": \"" + a.getDiscoveryLocation() + "\"\n");
                writer.write("  }" + (i < artifacts.size() - 1 ? "," : "") + "\n");
            }
            writer.write("]");
        }
    }

    public static List<com.hapc.Artifact> load() throws IOException {
        List<com.hapc.Artifact> list = new ArrayList<>();
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line.trim());
            }

            String data = json.toString()
                    .replace("[", "").replace("]", "")
                    .replace("},{", "}###{");

            String[] items = data.split("###");

            for (String item : items) {
                String id = extract(item, "id");
                String name = extract(item, "name");
                String desc = extract(item, "description");
                String era = extract(item, "era");
                String origin = extract(item, "origin");

                list.add(new com.hapc.Artifact(id, name, desc, era, origin));
            }
        }

        return list;
    }

    private static String extract(String json, String key) {
        String pattern = "\"" + key + "\":\\s*\"";
        int start = json.indexOf(pattern) + pattern.length();
        int end = json.indexOf("\"", start);
        if (start >= pattern.length() && end > start) {
            return json.substring(start, end);
        }
        return "";
    }
}
