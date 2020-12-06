package com.philblandford.musictheory.resources

import android.content.Context

class AndroidStringResolver(private val context: Context) : StringResolver {

  override fun getString(id: Int): String {
    return context.resources.getString(id)
  }
}