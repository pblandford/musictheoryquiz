package com.philblandford.musictheory.resources

class StubAssetManager : AssetManager {

  override fun getTerms(): Map<String, String> {
  return mapOf("allegro" to "fast",
  "adagio" to "slow",
    "allegretto" to "quite fast",
    "largo" to "broad and slow",
    "andante" to "at a walking pace",
    "forte" to "loud",
    "piano" to "quiet",
    "presto" to "very fast",
    "diminuendo" to "becoming quieter",
    "crescendo" to "becoming louder",
    )
  }
}