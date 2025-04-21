package com.hapc;

public class Artifact {
    private String artifactId;
    private String artifactName;
    private String category;
    private String civizilation;
    private String discoveryLocation;

    // Required no-arg constructor for Jackson
    public Artifact() {
    }

    public Artifact(String artifactId, String artifactName, String category, String civizilation, String discoveryLocation) {
        this.artifactId = artifactId;
        this.artifactName = artifactName;
        this.category = category;
        this.civizilation = civizilation;
        this.discoveryLocation = discoveryLocation;
    }

    // Getters and setters (can be generated automatically)
    public String getArtifactId() { return artifactId; }
    public void setArtifactId(String artifactId) { this.artifactId = artifactId; }

    public String getArtifactName() { return artifactName; }
    public void setArtifactName(String artifactName) { this.artifactName = artifactName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCivizilation() { return civizilation; }
    public void setCivizilation(String civizilation) { this.civizilation = civizilation; }

    public String getDiscoveryLocation() { return discoveryLocation; }
    public void setDiscoveryLocation(String discoveryLocation) { this.discoveryLocation = discoveryLocation; }

    @Override
    public String toString() {
        return artifactName + " (" + civizilation + ", " + discoveryLocation + ")";
    }
}
