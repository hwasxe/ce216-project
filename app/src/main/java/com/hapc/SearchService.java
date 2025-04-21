package com.hapc;

import java.util.ArrayList;
import java.util.List;

public class SearchService {

    /**
     * Searches artifacts by a query string across all fields
     * @param artifacts List of artifacts to search
     * @param query Search query string
     * @return List of artifacts matching the query
     */
    public List<Artifact> searchArtifacts(List<Artifact> artifacts, String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(artifacts);
        }

        String lowercaseQuery = query.toLowerCase().trim();
        List<Artifact> results = new ArrayList<>();

        for (Artifact artifact : artifacts) {
            if (matchesArtifact(artifact, lowercaseQuery)) {
                results.add(artifact);
            }
        }

        return results;
    }

    /**
     * Filters artifacts by tags
     * @param artifacts List of artifacts to filter
     * @param selectedTags List of tags to filter by
     * @return List of artifacts containing any of the selected tags
     */
    public List<Artifact> filterByTags(List<Artifact> artifacts, List<String> selectedTags) {
        if (selectedTags == null || selectedTags.isEmpty()) {
            return new ArrayList<>(artifacts);
        }

        List<Artifact> results = new ArrayList<>();

        for (Artifact artifact : artifacts) {
            if (containsAnyTag(artifact, selectedTags)) {
                results.add(artifact);
            }
        }

        return results;
    }

    /**
     * Extract all unique tags from a list of artifacts
     * @param artifacts List of artifacts to extract tags from
     * @return List of unique tags
     */
    public List<String> extractAllTags(List<Artifact> artifacts) {
        List<String> allTags = new ArrayList<>();

        for (Artifact artifact : artifacts) {
            if (artifact.getTags() != null && !artifact.getTags().isEmpty()) {
                // Split the tags string by commas and add each tag to the list
                String[] tags = artifact.getTags().split(",");
                for (String tag : tags) {
                    String trimmedTag = tag.trim();
                    if (!trimmedTag.isEmpty() && !allTags.contains(trimmedTag)) {
                        allTags.add(trimmedTag);
                    }
                }
            }
        }

        return allTags;
    }

    private boolean matchesArtifact(Artifact artifact, String query) {
        // Check basic fields
        if (containsIgnoreCase(artifact.getArtifactId(), query) ||
                containsIgnoreCase(artifact.getArtifactName(), query) ||
                containsIgnoreCase(artifact.getCategory(), query) ||
                containsIgnoreCase(artifact.getCivilization(), query) ||
                containsIgnoreCase(artifact.getDiscoveryLocation(), query) ||
                containsIgnoreCase(artifact.getComposition(), query) ||
                containsIgnoreCase(artifact.getDiscoveryDate(), query) ||
                containsIgnoreCase(artifact.getCurrentPlace(), query) ||
                containsIgnoreCase(artifact.getDimensions(), query) ||
                containsIgnoreCase(artifact.getWeight(), query)) {
            return true;
        }

        // Check tags
        return matchesTags(artifact.getTags(), query);
    }

    private boolean containsIgnoreCase(String text, String query) {
        return text != null && text.toLowerCase().contains(query);
    }

    private boolean matchesTags(String tagsString, String query) {
        if (tagsString == null || tagsString.isEmpty()) {
            return false;
        }

        String[] tags = tagsString.split(",");
        for (String tag : tags) {
            if (tag.trim().toLowerCase().contains(query)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsAnyTag(Artifact artifact, List<String> selectedTags) {
        if (artifact.getTags() == null || artifact.getTags().isEmpty()) {
            return false;
        }

        String[] artifactTags = artifact.getTags().split(",");
        for (String artifactTag : artifactTags) {
            String trimmedTag = artifactTag.trim().toLowerCase();
            for (String selectedTag : selectedTags) {
                if (trimmedTag.equals(selectedTag.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }
}