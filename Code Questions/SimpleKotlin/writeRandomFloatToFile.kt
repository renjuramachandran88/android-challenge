import java.io.BufferedOutputStream
import java.io.File
import java.nio.ByteBuffer
import kotlin.random.Random

fun writeRandomFloatsToFile(filePath: String, numFloats: Long = 50_000_000) {
    val random = Random
    val file = File(filePath)
    file.createNewFile()

    BufferedOutputStream(file.outputStream(), 8 * 1024 * 1024).use { out ->
        val byteBuffer = ByteBuffer.allocate(4 * 1024)
        var index = 0L

        while (index < numFloats) {
            byteBuffer.clear()
            val batchSize = minOf(1024, numFloats - index).toInt()

            for (i in 0 until batchSize) {
                byteBuffer.putFloat(random.nextFloat())
            }

            out.write(byteBuffer.array(), 0, byteBuffer.position())
            index += batchSize
        }
    }
}

fun main() {
    val filePath = "random_floats.txt"
    writeRandomFloatsToFile(filePath)
}