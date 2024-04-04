package com.fatherofapps.androidbase.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri

object ReadPathUtil {

    fun getPath(context: Context, uri: Uri): String? {
        var path: String? = null
        // "content"://
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            path = getDataColumn(context, uri, null, null)
        }
        // "file"://
        else if ("file".equals(uri.scheme, ignoreCase = true)) {
            path = uri.path
        }
        return path
    }

    private fun getDataColumn(context: Context, uri: Uri, selection: String?,
                              selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs,
                null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

}
