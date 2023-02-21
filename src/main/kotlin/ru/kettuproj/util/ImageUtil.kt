package ru.kettuproj.util

import io.ktor.http.content.*
import org.slf4j.LoggerFactory
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.io.path.pathString
import kotlin.io.path.toPath


object ImageUtil {
    val systemPath: String = "${ImageUtil::class.java.protectionDomain.codeSource.location.toURI().toPath().parent.pathString}/photo"
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun loadFiles(parts: List<PartData>): List<String>{
        if(!File(systemPath).exists()){
            if(File(systemPath).mkdir()) logger.info("Create photo path")
            else logger.warn("Can't create photo path")
        }
        val images = mutableListOf<String>()
        for(part in parts){
            when (part) {
                is PartData.FileItem -> {
                    val fileBytes = part.streamProvider().readBytes()
                    val uuid = saveFile(fileBytes)
                    if(uuid!=null) images.add(uuid)
                    else logger.info("Can't create image")
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
        val bufferedImage: BufferedImage? = ImageIO.read(fis)
        if(bufferedImage==null){
            logger.info("Can't read photo from bytes")
            return null
        }
        val uuid = generateUUID()
        return try {
            ImageIO.write(bufferedImage, "png", File("$systemPath/$uuid.png"))
            uuid
        }catch (e: Exception){
            logger.error("Can't save image: ${e.localizedMessage}")
            null
        }
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

    fun scanImageFormats(){
        ImageIO.scanForPlugins()
        val names = ImageIO.getReaderFormatNames()
        var str = ""
        for (i in names.indices) {
            str+= "${names[i]} "
        }
        logger.info("Supported image formats: $str")
    }

}