package com.hacp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JSONManager {

    private static final File file = new File("catalog.json");

    public static void save(List<Artifact> artifacts) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("[\n");
            for (int i = 0; i < artifacts.size(); i++) {
                Artifact a = artifacts.get(i);
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
                writer.write("    \"tags\": \"" + a.getTags() + "\",\n");
                writer.write("    \"imagePath\": \"" + a.getImagePath() + "\"\n");
                writer.write("  }" + (i < artifacts.size() - 1 ? "," : "") + "\n");

            }
            writer.write("]");
        }
    }

    public static List<Artifact> load() throws IOException {
        List<Artifact> list = new ArrayList<>();
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
                String imagePath = extract(item, "imagePath");

                list.add(new Artifact(artifactId, artifactName, category, civilization, discoveryLocation,
                        composition, discoveryDate, currentPlace, dimensions, weight, tags, imagePath));
            }
        }
        return list;
    }

    private static String extract(String json, String key) {
        try {
            String regex = "\"" + key + "\"\\s*:\\s*\"(.*?)\"";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(json);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
            System.err.println("Extract error for key: " + key);
            e.printStackTrace();
        }
        return "";
    }
}


