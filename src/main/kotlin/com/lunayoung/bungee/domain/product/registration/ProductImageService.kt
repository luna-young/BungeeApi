package com.lunayoung.bungee.domain.product.registration

import com.lunayoung.bungee.common.BungeeException
import com.lunayoung.bungee.domain.product.ProductImage
import com.lunayoung.bungee.domain.product.ProductImageRepository
import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Service
class ProductImageService @Autowired constructor(
    private val productImageRepository: ProductImageRepository
) {

    @Value("\${bungee.file-upload.default-dir}") //application.yml에 기입한 파일 업로드 디렉토리 설정을 읽은후 애너테이션이 붙은 변수에 대입
    var uploadPath: String? = ""

    fun uploadImage(image: MultipartFile)//MultipartFile: 컨트롤러로 인입되는 업로드 파일 데이터 객체
                :ProductImageUploadResponse {
        val filePath = saveImageFile(image)
        val productImage = saveImageData(filePath)

        return productImage.id?.let {
            ProductImageUploadResponse(it, filePath)
        } ?: throw BungeeException("이미지 저장 실패. 다시 시도해주세요.")
    }

    private fun saveImageFile(image: MultipartFile) : String {
        val extension = image.originalFilename
            ?.takeLastWhile { it != '.' } //takeLastWhile: 함수의 반환값이 true가 되기 전까지의 마지막 문자열 반환. 여기선 파일 확장자를 구하기 위해 사용
            ?: throw BungeeException("다른 이미지로 시도해주세요.")

        val uuid = UUID.randomUUID().toString()
        val date = SimpleDateFormat("yyyyMMdd").format(Date())

        val filePath = "/images/$date/$uuid.$extension"
        val targetFile = File("$uploadPath/$filePath")
        val thumbnail = targetFile.absolutePath
            .replace(uuid, "$uuid-thumb")
            .let(::File)

        targetFile.parentFile.mkdirs() //파일이 저장될 디렉토리 생성
        image.transferTo(targetFile) //MultipartFile 클래스에 선언된 함수로 업로드 파일을 파라미터로 지정된 파일 경로에 저장해줌

        Thumbnails.of(targetFile)
            .crop(Positions.CENTER)
            .size(300, 300)
            .outputFormat("jpg")
            .outputQuality(0.8f)
            .toFile(thumbnail)

        return filePath
    }

    private fun saveImageData(filePath: String) : ProductImage {
        val productImage = ProductImage(filePath)
        return productImageRepository.save(productImage)
    }


}