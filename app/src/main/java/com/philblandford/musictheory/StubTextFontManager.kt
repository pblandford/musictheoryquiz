package com.philblandford.musictheory

import TextFontManager
import com.philblandford.kscore.engine.core.area.factory.TextType
import java.io.InputStream

class StubTextFontManager : TextFontManager {

  override fun addTextFont(bytes: ByteArray, name: String) {

  }

  override fun deleteTextFonts() {

  }

  override fun getDefaultTextFont(): String {
    return ""
  }

  override fun getTextFont(name: String): InputStream? {
    return null
  }

  override fun getTextFontPath(name: String): String? {
    return null
  }

  override fun getTextFontPath(name: String?, textType: TextType?): String? {
    return null
  }

  override fun getTextFonts(): List<String> {
    return listOf()
  }
}