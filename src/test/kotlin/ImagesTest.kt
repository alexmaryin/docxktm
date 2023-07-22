package io.github.alexmaryin.docxktm

import io.github.alexmaryin.docxktm.extensions.body
import io.github.alexmaryin.docxktm.models.fromCmToEMU
import io.github.alexmaryin.docxktm.parts.imageFromFile
import io.github.alexmaryin.docxktm.parts.paragraph
import io.github.alexmaryin.docxktm.values.Paths
import kotlin.test.Test

class ImagesTest {

    @Test
    fun `Crete docx with simple image from file`() {
        DocxNew(Paths.TEST_DOCX_DIR + "image.docx") {
            body {
                paragraph {
                    imageFromFile(bindImage(Paths.TEST_IMAGES_DIR + "test.png"))
                }
            }
        }
    }

    @Test
    fun `Create docx with simple image from file with defined size`() {
        DocxNew(Paths.TEST_DOCX_DIR + "image.docx") {
            body {
                paragraph {
                    imageFromFile(
                        bindImage(
                            Paths.TEST_IMAGES_DIR + "test.png",
                            "maven",
                            10.fromCmToEMU(),
                            10.fromCmToEMU()
                        )
                    )
                }
            }
        }
    }
}