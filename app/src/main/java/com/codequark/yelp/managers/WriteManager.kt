package com.codequark.yelp.managers

import android.content.Context
import androidx.annotation.NonNull
import com.codequark.yelp.utils.Utils
import com.codequark.yelp.utils.LogUtils
import java.io.*

@Suppress("deprecation")
class WriteManager {
    companion object {
        private var rootFolder = ""

        private lateinit var logFile: File
        private var fileExists = false
        private var ENABLE_WRITE_LOGS = true

        fun initProject(@NonNull context: Context, @NonNull organization: String, @NonNull projectName: String) {
            val mediaDirs = context.externalMediaDirs
            val mediaDir = mediaDirs[0]

            if(mediaDir != null) {
                LogUtils.print("mediaDir: " + mediaDir.absolutePath)

                val file1 = File(mediaDir, organization)
                val file2 = File(file1, projectName)
                val file3 = File(file2, "Requests")

                val filePath = file3.absolutePath

                if(file3.exists()) {
                    LogUtils.print("File exists: $filePath")
                } else {
                    val mkdirs = file3.mkdirs()

                    if(mkdirs) {
                        LogUtils.print("File created: $filePath")
                    } else {
                        LogUtils.print("File not created: $filePath")
                    }
                }

                rootFolder = filePath + File.separator

                ENABLE_WRITE_LOGS = true
            } else {
                ENABLE_WRITE_LOGS = false
            }
        }

        fun init(@NonNull prefix: String) {
            if(ENABLE_WRITE_LOGS) {
                val fileName = prefix + "-" + Utils.getTimestamp() + ".txt"
                val folder = File(rootFolder)

                logFile = File(rootFolder + File.separator + fileName)

                if(folder.exists()) {
                    LogUtils.print()
                } else {
                    LogUtils.print("Trying to make folder: $rootFolder")

                    if(folder.mkdirs()) {
                        LogUtils.print("Folder created: $prefix")
                    } else {
                        LogUtils.print("Folder not created: $prefix")
                        return
                    }
                }

                if(logFile.exists()) {
                    LogUtils.print("File exists: $logFile")
                    fileExists = true
                } else {
                    fileExists = try {
                        if(logFile.createNewFile()) {
                            LogUtils.print("File created: $logFile")
                            true
                        } else {
                            LogUtils.print("File can't be created: $logFile")
                            false
                        }
                    } catch (ex: Exception) {
                        LogUtils.print("WriteManager Init Ex: $ex")
                        false
                    }
                }
            }
        }

        fun writeLog(@NonNull text: String) {
            if(ENABLE_WRITE_LOGS) {
                if(text.isEmpty()) {
                    return
                }

                if(fileExists) {
                    try {
                        val content = getFileContent()
                        val fileWriter = FileWriter(logFile)

                        fileWriter.write(content)
                        fileWriter.write("\n")
                        fileWriter.write(text)
                        fileWriter.close()
                    } catch (ex: IOException) {
                        LogUtils.print("An error occurred writing file (writeLog): $ex")
                    }
                }
            }
        }

        fun writeTitle(@NonNull text: String) {
            if(ENABLE_WRITE_LOGS) {
                if(text.isEmpty()) {
                    return
                }

                if(fileExists) {
                    try {
                        val content = getFileContent()
                        val fileWriter = FileWriter(logFile)

                        fileWriter.write(content)
                        fileWriter.write("\n")
                        fileWriter.write("\n")
                        fileWriter.write(text)
                        fileWriter.write("\n")
                        fileWriter.write("\n")
                        fileWriter.close()
                    } catch (ex: IOException) {
                        LogUtils.print("An error occurred writing file (writeLog): $ex")
                    }
                }
            }
        }

        @NonNull
        private fun getFileContent(): String {
            if(ENABLE_WRITE_LOGS) {
                val fileReader = FileReader(logFile)
                val reader = BufferedReader(fileReader)
                val stringBuilder = StringBuilder()
                val ls = System.getProperty("line.separator")
                var line: String

                while(
                    reader.readLine().also {
                        line = it ?: ""
                    } != null
                ) {
                    stringBuilder.append(line)
                    stringBuilder.append(ls)
                }

                if(stringBuilder.isNotEmpty()) {
                    stringBuilder.deleteCharAt(stringBuilder.length - 1)
                }

                reader.close()

                return stringBuilder.toString()
            }

            return ""
        }
    }
}