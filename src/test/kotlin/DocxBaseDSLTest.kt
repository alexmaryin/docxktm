package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.values.PageSize
import io.github.alexmaryin.docxktm.values.Paths
import org.docx4j.model.structure.PageSizePaper
import org.docx4j.wml.STPageOrientation
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class DocxBaseDSLTest {

    @Test
    fun `Create and save blank docx file in temporary directory`() {
        DocxNew(Paths.TEST_DOCX_DIR + "test.docx") {}
        val file = File(Paths.TEST_DOCX_DIR + "test.docx")
        assertTrue { file.isFile }
    }

    @Test
    fun `Open docx file in temporary directory`() {
        DocxOpen(Paths.TEST_DOCX_DIR + "test.docx", Paths.TEST_DOCX_DIR + "newfile.docx") {}
        val file = File(Paths.TEST_DOCX_DIR + "newfile.docx")
        assertTrue { file.isFile }
    }

    @Test
    fun `Create and save blank docx with landscape orientation and custom size`() {
        DocxNew(
            Paths.TEST_DOCX_DIR + "landscape.docx",
            pageSize = PageSizePaper.LETTER,
            landscape = true
        ) {}
        DocxOpen(Paths.TEST_DOCX_DIR + "landscape.docx", autoSave = false) {
            assertTrue { pageSize() == PageSize.Letter }
            assertTrue { orientation() == STPageOrientation.LANDSCAPE }
        }
    }
}