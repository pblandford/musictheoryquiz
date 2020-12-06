package com.philblandford.musictheory.resources

import android.content.Context
import org.apache.commons.io.IOUtils
import java.nio.charset.Charset

class AndroidAssetManager(context: Context) : AssetManager {

  private var terms:Map<String,String>

  init {
    val inputStream = context.assets.open("terms.txt")
    val string = IOUtils.toString(inputStream, Charset.defaultCharset())
    val lines = string.split('\n')
    terms = lines.map {
      val fields = it.split('\t')
      fields.first() to fields.last()
    }.filterNot { it.first.isEmpty() || it.second.isEmpty() }.toMap()
  }

  override fun getTerms(): Map<String, String> {
    return terms
  }
}