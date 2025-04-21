package com.hapc;

import java.util.Arrays;
import java.util.List;

public class SearchServiceTest {
    public static void main(String[] args) {
        // Create some test artifacts
        Artifact artifact1 = new Artifact("2012gizaros", "Rosetta Stone", "Manuscript",
                "Ancient Egyptian", "Rosetta, Egypt", "Stone", "1799-07-15",
                "British Museum", "112.3x75.7x28.4 cm", "760 kg", "ancient egypt,hieroglyphics,manuscript");

        Artifact artifact2 = new Artifact("1940athsgld", "Mask of Agamemnon", "Sculpture",
                "Mycenaean", "Athens, Greece", "Gold", "1876-11-30",
                "National Archaeological Museum", "25x25x10 cm", "2.5 kg", "mycenaean,gold,mask,funeral");

        Artifact artifact3 = new Artifact("1980romswrd", "Gladius", "Weapon",
                "Roman Empire", "Rome, Italy", "Iron", "1950-06-15",
                "Roman History Museum", "60x8x2 cm", "1.2 kg", "roman,weapon,sword,iron");

        List<Artifact> artifacts = Arrays.asList(artifact1, artifact2, artifact3);

        // Create the search service
        SearchService searchService = new SearchService();

        // Test search functionality
        System.out.println("Search for 'gold':");
        List<Artifact> goldResults = searchService.searchArtifacts(artifacts, "gold");
        for (Artifact a : goldResults) {
            System.out.println(" - " + a);
        }

        System.out.println("\nSearch for 'stone':");
        List<Artifact> stoneResults = searchService.searchArtifacts(artifacts, "stone");
        for (Artifact a : stoneResults) {
            System.out.println(" - " + a);
        }

        // Test tag filtering
        System.out.println("\nFilter by tag 'weapon':");
        List<Artifact> weaponResults = searchService.filterByTags(artifacts, Arrays.asList("weapon"));
        for (Artifact a : weaponResults) {
            System.out.println(" - " + a);
        }

        // Extract all unique tags
        System.out.println("\nAll unique tags:");
        List<String> allTags = searchService.extractAllTags(artifacts);
        for (String tag : allTags) {
            System.out.println(" - " + tag);
        }
    }
}
