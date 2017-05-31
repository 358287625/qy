package com.qy.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qy.beans.Doc;
import com.qy.common.Constrant;
import com.qy.common.Tools;

/**
 *    * @ClassName DocFormat    @Description TODO   * @author liujx 
 *  @date 2017年5月23日      
 */
public class DocConvert {
	private static Logger log = LoggerFactory.getLogger(DocConvert.class);
	private static OfficeManager officeManager;
	private static DocConvert instance = new DocConvert();
	private static String CONVERT_DOC_TO_PDF_SHELL=Class.class.getClass().getResource("/").getPath()+"/convert.sh"; 
	public static DocConvert getInstance() {
		return instance;
	}

	private DocConvert() {}

	/**
	 * http://blog.csdn.net/dongdong_919/article/details/44959237
	 * 
	 * txt:使用libreOffice来转换pdf，转换成功，但是中文有乱码!!!!!
	 * doc:这是office中的doc文档，可以转换成功，并且中文没有乱码 docx:这是office中的docx文档，可以转换成功，并且中文没有乱码
	 * ppt和pptx：转换成功。 xls:转换成功，没有中文乱码 xlsx：转换成功 jpg和png:成功
	 * 
	 * @Title: doDocToFdpLibre
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return 参数说明
	 * @return String 返回类型
	 */
	public String convert2PDF(Doc doc) {
		if (StringUtils.isEmpty(doc.getSrcpath())
				|| doc.getSrcpath().endsWith(".pdf"))
			return doc.getSrcpath();

		File srcFile = new File(doc.getSrcpath());
		if(!srcFile.exists())
			return null;
		
		String fileName = srcFile.getName().substring(0,
				srcFile.getName().lastIndexOf("."));
		String pdf_path=srcFile.getParent()+Constrant.PDF_PATH;
		int result = Tools.excShell(CONVERT_DOC_TO_PDF_SHELL,doc.getSrcpath() ,pdf_path);
		
		if(Constrant.REQUEST_SUCCESS!= result)
			return null;
		
		String path = getPdfFileByPage(pdf_path+ fileName+ ".pdf",doc.getPageFrom(),doc.getPageTo());
		//推长连接
		return path;
	}
	/**
	 * 抽取指定範圍頁面的內容，形成一個新的pdf文件
	 * @param pdfFile
	 * @param pageFrom
	 * @param pageEnd
	 * @return
	 */
	public String getPdfFileByPage(String pdfFile, int pageFrom, int pageEnd) {
		String file_name = null;
		PDDocument doc = null;
		PDDocument pdf = null;
		try {
			if (pageFrom > pageEnd)// 参数不对，不转
				return pdfFile;

			File srcPdf = new File(pdfFile);
			doc = PDDocument.load(srcPdf);
			int pages = doc.getNumberOfPages();
			
			if (pageFrom <= 0 || pageFrom > pages)
				return pdfFile;

			if (pageEnd <= 0 || pageEnd > pages)
				return pdfFile;

			file_name = srcPdf.getParent()+Constrant.PDF_SUB_PATH+srcPdf.getName().substring(0,
					srcPdf.getName().lastIndexOf("."))
					+ ".pdf";//部分頁面內容
			pdf = new PDDocument();

			for (int i = pageFrom - 1; i <= (pageEnd - 1); i++) {
				log.info(doc.getNumberOfPages() + " -- " + i);
				pdf.addPage(doc.getPages().get(i));
			}
			pdf.save(file_name);

		} catch (Exception e) {
			log.info(e.getMessage(), e);
		} finally {
			try {
				if (doc != null) {
					doc.close();
				}
				if (pdf != null) {
					pdf.close();
				}
			} catch (IOException e) {
				log.info(e.getMessage(), e);
			}
		}
		return file_name;

	}

	public static void stopService() {// web应用停止时调用
		System.out.println("关闭office转换服务....");
		if (officeManager != null) {
			officeManager.stop();
			officeManager = null;
		}
		System.out.println("关闭office转换成功!");
	}

	private static void copyFile(String oldPathFile, String newPathFile) {
		try {
			int byteread = 0;
			File oldfile = new File(oldPathFile);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPathFile);
				FileOutputStream fs = new FileOutputStream(newPathFile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				inStream = null;
				fs.close();
				fs = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
