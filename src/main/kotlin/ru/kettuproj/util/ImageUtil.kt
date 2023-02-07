package ru.kettuproj.util

import io.ktor.http.content.*
import net.coobird.thumbnailator.Thumbnails
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*
import javax.imageio.ImageIO


object ImageUtil {
    const val systemPath: String = "/photo"
    const val tempPath: String   = "$systemPath/temp"

    fun loadFiles(parts: List<PartData>): List<String>{
        if(!File(systemPath).exists()) File(systemPath).mkdir()
        if(!File(tempPath).exists()) File(tempPath).mkdir()
        val images = mutableListOf<String>()
        for(part in parts){
            when (part) {
                is PartData.FileItem -> {
                    val fileBytes = part.streamProvider().readBytes()
                    val uuid = saveFile(fileBytes)
                    if(uuid!=null) images.add(uuid)
                }
                else -> {
                }
            }
            part.dispose()
        }
        return images
    }

    fun getImage(uuid: String): File?{
        val file =  File("$systemPath/$uuid.png")
        return if(file.exists()) file
        else null
    }

    private fun saveFile(file: ByteArray): String?{
        val fis = ByteArrayInputStream(file)
        val bufferedImage: BufferedImage = ImageIO.read(fis) ?: return null
        val uuid = generateUUID()
        ImageIO.write(bufferedImage, "png", File("$systemPath/$uuid.png"))
        return uuid
    }

    private fun generateUUID(): String{
        val sp = File(systemPath)
        if(!sp.exists()) sp.mkdir()
        val files = sp.listFiles()
        var uuid = UUID.randomUUID().toString()
        if(files==null) return uuid
        while (true){
            if(!files.map { it.name }.contains(uuid)) break
            uuid = UUID.randomUUID().toString()
        }
        return uuid
    }

    fun deleteImage(uuid: String){
        File("$systemPath/$uuid.png").delete()
    }
}