package com.jsnjwj.compare.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.jsnjwj.compare.constants.GlobalConstant;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class FileUtils {

	public static float DEFAULT_DPI = 300;

	private static File getUploadPath(String dir) {
		File fileRealPath = new File(GlobalConstant.FILE_REAL_PATH + dir);
		if (!fileRealPath.exists()) {
			fileRealPath.mkdirs();
		}
		return fileRealPath;
	}

	public static Map<String, Object> uploadFile(MultipartFile uploadFile) throws Exception {

		File fileRealPath = getUploadPath("contract-file");

		String uuid = IdUtil.fastSimpleUUID();
		File file = convert2File(uploadFile);

		String suffix = FileUtil.getSuffix(file);
		String fileName = uuid + "." + suffix;
		File targetFile = new File(fileRealPath, fileName);
		uploadFile.transferTo(targetFile);
		Boolean rst = file.delete();

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("location", "http://127.0.0.1:8082/file/contract-file/" + fileName);
		result.put("file", targetFile);

		return result;
	}

	private static File convert2File(MultipartFile multipartFile) {
		String fileName = multipartFile.getOriginalFilename();
		assert fileName != null;
		File file = new File(fileName);
		OutputStream out = null;
		try {
			// 获取文件流，以文件流的方式输出到新文件
			out = Files.newOutputStream(file.toPath());
			byte[] ss = multipartFile.getBytes();
			for (int i = 0; i < ss.length; i++) {
				out.write(ss[i]);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (out != null) {
				try {
					out.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	public static List<Map<String, Object>> pdf2Image(File ufile) throws Exception {
		List<Map<String, Object>> result = new ArrayList<>();
		String uuid = IdUtil.fastSimpleUUID();

		// 创建文件夹
		File fileRealPath = getUploadPath("contract-page/" + uuid);
		String outPath = fileRealPath.getPath();

		try {
			PDDocument source = PDDocument.load(ufile);
			split(1, source.getNumberOfPages(), 1, ufile.getAbsolutePath(), outPath);
			for (int i = 1; i <= source.getNumberOfPages(); i++) {
				String imgPath = outPath + File.separator + i + ".jpg";
				writeToImage(outPath + File.separator + i + ".pdf", imgPath);

				String imageLocation = "http://127.0.0.1:8082/file/contract-page/" + uuid + "/" + i + ".jpg";
				Map<String, Object> fileObj = new HashMap<String, Object>();
				// 文件绝对地址
				fileObj.put("location", imageLocation);
				// 文件访问路径
				fileObj.put("filePath", imgPath);
				result.add(fileObj);
			}
			source.close();
		}
		catch (IOException e) {
			throw new Exception("文件上传解析失败");
		}
		return result;
	}

	public static void writeToImage(String pdf, String out) throws IOException {
		Path pdfPath = Paths.get(pdf);
		byte[] bytes = Files.readAllBytes(pdfPath);
		byte[] data = pdfToImage2(bytes);
		File file = new File(out);
		// 打开输入流
		FileImageOutputStream imageOutput = new FileImageOutputStream(file);
		// 将byte写入硬盘
		imageOutput.write(data, 0, data.length);
		imageOutput.flush();
		;
		imageOutput.close();
	}

	public static void split(int start, int end, int step, String path, String out) throws IOException {
		Splitter splitter = new Splitter();
		// 设置起始页、结束页，每个文件的页数
		splitter.setStartPage(start);
		splitter.setEndPage(end);
		splitter.setSplitAtPage(step);
		PDDocument source = PDDocument.load(new File(path));
		List<PDDocument> list = splitter.split(source);

		int i = 0;
		for (PDDocument document : list) {
			i++;
			document.save(out + File.separator + i + ".pdf");
			document.close();
		}
	}

	/**
	 * pdf文件转换成jpg图片流
	 * @param pdfBytes 要转换的pdf文件的流
	 */
	public static byte[] pdfToImage2(byte[] pdfBytes) {
		try {
			PDDocument doc = PDDocument.load(pdfBytes);
			int pageCount = doc.getNumberOfPages();
			// log.info("PDF转图片流，总页数:{}", pageCount);
			PDFRenderer pdfRenderer = new PDFRenderer(doc);
			// 不知道图片的宽和高，所以先定义个null
			BufferedImage pdfImage = null;
			// pdf有多少页
			int y = 0;
			List<BufferedImage> list = new ArrayList<>(pageCount);
			// 所有页高度综合
			int totalHeight = 0;
			if (pageCount > 0) {
				for (int i = 0; i < pageCount; i++) {
					// 每页pdf内容
					BufferedImage bim = pdfRenderer.renderImageWithDPI(i, DEFAULT_DPI, ImageType.RGB);
					totalHeight += bim.getHeight();
					list.add(bim);
				}
			}
			for (BufferedImage bim : list) {
				// 如果是第一页需要初始化 BufferedImage
				if (Objects.isNull(pdfImage)) {
					// 创建一个总高、总宽 的图片缓冲区
					pdfImage = new BufferedImage(bim.getWidth(), totalHeight, BufferedImage.TYPE_INT_RGB);
				}
				// 将每页pdf画到总的pdfImage上,x坐标=0，y坐标=之前所有页的高度和，属于向下偏移
				pdfImage.getGraphics().drawImage(bim, 0, y, null);
				y += bim.getHeight();
			}
			doc.close();
			if (pdfImage != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(pdfImage, "jpg", baos);
				baos.flush();
				byte[] imageInByte = baos.toByteArray();

				return imageInByte;
			}
			return null;
		}
		catch (Exception e) {

			return null;
		}
	}

}
