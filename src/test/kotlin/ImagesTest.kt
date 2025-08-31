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
        DocxNew(Paths.TEST_DOCX_DIR + "image_single.docx") {
            body {
                paragraph {
                    imageFromFile(bindImage(Paths.TEST_IMAGES_DIR + "test.png"))
                }
            }
        }
    }

    @Test
    fun `Create docx with simple image from file with defined size`() {
        DocxNew(Paths.TEST_DOCX_DIR + "image_both_size.docx") {
            body {
                paragraph {
                    imageFromFile(
                        bindImage(
                            Paths.TEST_IMAGES_DIR + "test.png",
                            "maven",
                            5.fromCmToEMU(),
                            3.fromCmToEMU()
                        )
                    )
                }
            }
        }
    }

    @Test
    fun `Create docx with simple image from file with defined width`() {
        DocxNew(Paths.TEST_DOCX_DIR + "image_width.docx") {
            body {
                paragraph {
                    imageFromFile(
                        bindImage(
                            Paths.TEST_IMAGES_DIR + "test.png",
                            "maven",
                            3.fromCmToEMU()
                        )
                    )
                }
            }
        }
    }

    @Test
    fun `Create docx with simple image from file with defined height`() {
        DocxNew(Paths.TEST_DOCX_DIR + "image_height.docx") {
            body {
                paragraph {
                    imageFromFile(
                        bindImage(
                            Paths.TEST_IMAGES_DIR + "test.png",
                            "maven",
                            null,
                            3.fromCmToEMU()
                        )
                    )
                }
            }
        }
    }
}