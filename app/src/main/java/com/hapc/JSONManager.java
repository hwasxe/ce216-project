package com.hapc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JSONManager {

    // Use an absolute path to ensure consistency
    private static final File file = new File(System.getProperty("user.dir") + "/catalog.json");

    public static void save(List<com.hapc.Artifact> artifacts) throws IOException {
        System.out.println("Saving to: " + file.getAbsolutePath());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("[\n");
            for (int i = 0; i < artifacts.size(); i++) {
                com.hapc.Artifact a = artifacts.get(i);
                writer.write("  {\n");
                writer.write("    \"artifactId\": \"" + a.getArtifactId() + "\",\n");
                writer.write("    \"artifactName\": \"" + a.getArtifactName() + "\",\n");
                writer.write("    \"category\": \"" + a.getCategory() + "\",\n");
                writer.write("    \"civilization\": \"" + a.getCivilization() + "\",\n");
                writer.write("    \"discoveryLocation\": \"" + a.getDiscoveryLocation() + "\",\n");
                writer.write("    \"composition\": \"" + a.getComposition() + "\",\n");
                writer.write("    \"discoveryDate\": \"" + a.getDiscoveryDate() + "\",\n");
                writer.write("    \"currentPlace\": \"" + a.getCurrentPlace() + "\",\n");
                writer.write("    \"dimensions\": \"" + a.getDimensions() + "\",\n");
                writer.write("    \"weight\": \"" + a.getWeight() + "\",\n");
                writer.write("    \"tags\": \"" + a.getTags() + "\"\n");  // No trailing comma
                writer.write("  }" + (i < artifacts.size() - 1 ? "," : "") + "\n");
            }
            writer.write("]");
            System.out.println("Successfully saved " + artifacts.size() + " artifacts");
        } catch (IOException e) {
            System.err.println("Error saving artifacts: " + e.getMessage());
            throw e;
        }
    }

    public static List<com.hapc.Artifact> load() throws IOException {
        List<com.hapc.Artifact> list = new ArrayList<>();
        System.out.println("Loading from: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.out.println("Catalog file doesn't exist at: " + file.getAbsolutePath());
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line.trim());
            }

            String data = json.toString()
                    .replace("[", "").replace("]", "")
                    .replace("},{", "}###{");

            if (data.trim().isEmpty()) {
                System.out.println("Catalog file is empty or contains only whitespace");
                return list;
            }

            String[] items = data.split("###");
            System.out.println("Found " + items.length + " artifacts in catalog");

            for (String item : items) {
                try {
                    String artifactId = extract(item, "artifactId");
                    String artifactName = extract(item, "artifactName");
                    String category = extract(item, "category");
                    String civilization = extract(item, "civilization");
                    String discoveryLocation = extract(item, "discoveryLocation");
                    String composition = extract(item, "composition");
                    String discoveryDate = extract(item, "discoveryDate");
                    String currentPlace = extract(item, "currentPlace");
                    String dimensions = extract(item, "dimensions");
                    String weight = extract(item, "weight");
                    String tags = extract(item, "tags");

                    list.add(new com.hapc.Artifact(artifactId, artifactName, category, civilization, discoveryLocation,
                            composition, discoveryDate, currentPlace, dimensions, weight, tags));
                } catch (Exception e) {
                    System.err.println("Error parsing artifact: " + e.getMessage());
                }
            }
            System.out.println("Successfully loaded " + list.size() + " artifacts");
        } catch (IOException e) {
            System.err.println("Error loading artifacts: " + e.getMessage());
            throw e;
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