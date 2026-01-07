package org.yourorg.cicd

class Provenance {
  static void generateSbom(Map c, String image) {
    println "[Provenance] Generate SBOM for ${image} (예: Syft CycloneDX)"
  }
  static void signImage(Map c, String image) {
    println "[Provenance] Sign image ${image} (예: cosign)"
  }
}
