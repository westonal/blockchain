package info.blockchain.challenge

import org.junit.Assert.assertNotNull
import java.io.InputStream

fun resource(resourceFileName: String) =
        ClassLoader.getSystemClassLoader()
                .inputStream(resourceFileName)
                .readAllAsString()


private fun InputStream.readAllAsString(): String =
        this.bufferedReader()
                .use { it.readText() }

private fun ClassLoader.inputStream(file: String) =
        getResourceAsStream(file).also {
            assertNotNull("Resource not found $file", it)
        }