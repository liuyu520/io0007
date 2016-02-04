package com.common.util;

import com.common.bean.ZipFileBean;
import com.common.bean.ZipFileModifiedInfo;
import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;
import com.swing.messagebox.GUIUtil23;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/***
 * use apache
 * commons-compress,http://www.cnblogs.com/un4sure/archive/2011/09/27/
 * 2193298.html,http://hw1287789687.iteye.com/blog/1976309
 * 
 * @author huangwei
 * @since 2013-11-18
 */
public final class CompressZipUtil {
	private static boolean isPrint;
	private CompressZipUtil() {
		throw new Error("Don't let anyone instantiate this class.");
	}

	/***
	 * 
	 * @param zipOut
	 * @param filepath
	 *            :要压缩的单个文件
	 * @throws IOException
	 */
	public static void addEntry(ZipArchiveOutputStream zipOut, String filepath)
			throws IOException {
		File singleFile = new File(filepath);
		if(!singleFile.exists()){//文件不存在
			return;
		}
		if (singleFile.isDirectory()) {
			return;
		}
		ZipArchiveEntry zipEntry2 = new ZipArchiveEntry(singleFile,
				SystemHWUtil.getFileSimpleName(filepath));
		zipEntry2.setSize(singleFile.length());
		zipOut.putArchiveEntry(zipEntry2);
		FileInputStream fin = new FileInputStream(filepath);
		// 不要关闭zipOut，关闭之前要执行closeArchiveEntry()
		FileUtils.writeIn2Output(fin, zipOut, false, true);
	}

	/***
	 * 与上述方法的区别就是第二个参数的类型.
	 * 
	 * @param zipOut
	 * @param filepath
	 *            : 要压缩的单个文件
	 * @throws IOException
	 */
	public static void addEntry(ZipArchiveOutputStream zipOut, File filepath)
			throws IOException {
		if(!filepath.exists()){//文件不存在
			return;
		}
		if (filepath.isDirectory()) {
			return;
		}
		ZipArchiveEntry zipEntry2 = new ZipArchiveEntry(filepath,
				filepath.getName());
		zipEntry2.setSize(filepath.length());
		zipOut.putArchiveEntry(zipEntry2);
		FileInputStream fin = new FileInputStream(filepath);
		// 不要关闭zipOut，关闭之前要执行closeArchiveEntry()
		FileUtils.writeIn2Output(fin, zipOut, false, true);
	}

	/***
	 * 把从fin 中读取的字节流写入zipOut 中.
	 * 
	 * @param zipOut
	 * @param filepath
	 *            : 要压缩的单个文件
	 * @throws IOException
	 */
	public static void addEntry(ZipArchiveOutputStream zipOut, InputStream fin,
			String fileName) throws IOException {
		ZipArchiveEntry zipEntry2 = new ZipArchiveEntry(fileName);
		zipOut.putArchiveEntry(zipEntry2);
		// 不要关闭zipOut，关闭之前要执行closeArchiveEntry()
		FileUtils.writeIn2Output(fin, zipOut, false, true/* isCloseInput */);
	}

	/***
	 * 把inputZipBytes 写入zipOut
	 * 
	 * @param zipOut
	 * @param inputZipBytes
	 * @param fileName
	 * @throws IOException
	 */
	public static void addEntry(ZipArchiveOutputStream zipOut,
			byte[] inputZipBytes, String fileName, long size)
			throws IOException {
		if(ValueWidget.isNullOrEmpty(inputZipBytes)){
			return ;
		}
		ZipArchiveEntry zipEntry2 = new ZipArchiveEntry(fileName);
		if(isPrint)
		System.out.println("size:" + size);
		zipEntry2.setSize(size);
		zipOut.putArchiveEntry(zipEntry2);
		// 不要关闭zipOut，关闭之前要执行closeArchiveEntry()
		zipOut.write(inputZipBytes);
		zipOut.flush();
		if(isPrint)
		System.out.println("flush");
		zipOut.closeArchiveEntry();
	}

	/***
	 * 压缩之后，文件都是在同一级别.
	 * 
	 * @param zipOut
	 * @param filePaths
	 *            :要压缩的文件
	 * @throws IOException
	 */
	public static void compressZip(ZipArchiveOutputStream zipOut,
			@SuppressWarnings("rawtypes") List filePaths) throws IOException {
		for (Object fileObj : filePaths) {
			if (fileObj instanceof File) {
				File file = (File) fileObj;
				addEntry(zipOut, file);
			} else {
				addEntry(zipOut, (String) fileObj);
			}
		}
		closeZip(zipOut, true);
	}

	/***
	 * 压缩之后的收尾操作.
	 * 
	 * @param zipOut
	 * @throws IOException
	 */
	private static void closeZip(ZipArchiveOutputStream zipOut,
			boolean iscloseArchiveEntry) throws IOException {
		if (iscloseArchiveEntry) {
			zipOut.closeArchiveEntry();// it is necessary
		}
		zipOut.flush();
		zipOut.finish();
		zipOut.close();
	}

	/***
	 * 压缩.
	 * 
	 * @param zipFile
	 *            :压缩的结果--zip文件
	 * @param filePaths
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static void compressZip(File zipFile,
			@SuppressWarnings("rawtypes") List filePaths) throws IOException,
			ArchiveException {
		if(ValueWidget.isNullOrEmpty(filePaths)){
			return;
		}
		FileOutputStream fou = new FileOutputStream(zipFile);
		ArchiveOutputStream archOuts = new ArchiveStreamFactory()
				.createArchiveOutputStream(ArchiveStreamFactory.ZIP, fou);
		if (archOuts instanceof ZipArchiveOutputStream) {
			ZipArchiveOutputStream zipOut = (ZipArchiveOutputStream) archOuts;
			compressZip(zipOut, filePaths);
		}
	}

	/***
	 * 压缩文件.
	 * 
	 * @param zipFile
	 * @param folderPaths
	 * @return
	 * @throws ArchiveException
	 * @throws IOException
	 */
	public static boolean compressZipRecursion(String zipFile,
			String folderPaths) throws ArchiveException, IOException {
		FileOutputStream fou = new FileOutputStream(zipFile);
		ArchiveOutputStream archOuts = new ArchiveStreamFactory()
				.createArchiveOutputStream(ArchiveStreamFactory.ZIP, fou);
		if (archOuts instanceof ZipArchiveOutputStream) {
			ZipArchiveOutputStream zipOut = (ZipArchiveOutputStream) archOuts;
			List<ZipArchiveEntry> zipEntrys = getZipFileListRecursion(new File(
					folderPaths), null);
			for (int i = 0; i < zipEntrys.size(); i++) {
				ZipArchiveEntry zipEntry2 = zipEntrys.get(i);
				zipOut.putArchiveEntry(zipEntry2);
				File file = new File(folderPaths, zipEntry2.getName());
				if (!file.exists()) {
					return false;
				}
				if (!file.isDirectory()) {
					FileInputStream fin = new FileInputStream(file);
					// 不要关闭zipOut，关闭之前要执行closeArchiveEntry()
					FileUtils.writeIn2Output(fin, zipOut, false, true);
				}
			}
			closeZip(zipOut, true);

		}
		return true;
	}

	/***
	 * 
	 * @param zipFile
	 *            :压缩的结果--zip文件
	 * @param filePaths
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static void compressZip(String zipFile,
			@SuppressWarnings("rawtypes") List filePaths) throws IOException,
			ArchiveException {
		if(ValueWidget.isNullOrEmpty(filePaths)){
			return;
		}
		compressZip(new File(zipFile), filePaths);
	}

	/***
	 * compress single file.
	 * 
	 * @param zipFile
	 * @param filepath
	 *            :single file
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static void compressSingeFile(String zipFile, String filepath)
			throws IOException, ArchiveException {
		List filePaths = new ArrayList();
		filePaths.add(filepath);
		compressZip(zipFile, filePaths);
	}

	/***
	 * compress single file.与上述方法的区别就是第二个参数的类型.
	 * 
	 * @param zipFile
	 * @param filepath
	 *            :single file
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static void compressSingeFile(String zipFile, File filepath)
			throws IOException, ArchiveException {
		List filePaths = new ArrayList();
		filePaths.add(filepath);
		compressZip(zipFile, filePaths);
	}

	/***
	 * 压缩指定文件夹中的所有文件.
	 * 
	 * @param zipFile
	 * @param folderPaths
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static void compressZip(String zipFile, String folderPaths)
			throws IOException, ArchiveException {
		List list = FileUtils.getListFiles(new File(folderPaths));
		if(ValueWidget.isNullOrEmpty(list)){
			return;
		}
		compressZip(zipFile, list);
	}

	/***
	 * 压缩指定文件夹中的所有文件.
	 * 
	 * @param zipFile
	 * @param folderPaths
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static void compressZip(String zipFile, File folderPaths)
			throws IOException, ArchiveException {
		List list = FileUtils.getListFiles(folderPaths);
		compressZip(zipFile, list);
	}

	/***
	 * 解压zip
	 * 
	 * @param zipFile
	 * @param decompressLoc
	 *            :解压之后的文件所在目录
	 * @throws ArchiveException
	 * @throws IOException
	 */
	public static boolean deCompressRecursion(String zipFile,
			File decompressLoc, String charSet) throws ArchiveException,
			IOException {
		if(ValueWidget.isNullOrEmpty(charSet)){
			charSet=SystemHWUtil.CHARSET_UTF;
		}
		FileInputStream fin = new FileInputStream(zipFile);
		ArchiveInputStream archIns = new ArchiveStreamFactory()
				.createArchiveInputStream(ArchiveStreamFactory.ZIP, fin);
		ZipArchiveInputStream zipIn = (ZipArchiveInputStream) archIns;
		boolean isSuccess = deCompressRecursion(zipIn, decompressLoc, charSet);
		zipIn.close();
		fin.close();
		return isSuccess;
	}

	/***
	 * 递归解压缩.
	 * 
	 * @param zipIn
	 * @param decompressLoc
	 * @return
	 * @throws IOException
	 */
	private static boolean deCompressRecursion(ZipArchiveInputStream zipIn,
			File decompressLoc, String charset) throws IOException {
		ZipArchiveEntry zipEntry;
		if (ValueWidget.isNullOrEmpty(charset)) {
			charset = SystemHWUtil.CHARSET_UTF;
		}
		while (!ValueWidget.isNullOrEmpty(zipEntry = zipIn.getNextZipEntry())) {
			byte[] rawName = zipEntry.getRawName();
			String fileName = new String(rawName, charset);
			// System.out.println(fileName);
			if (zipEntry.isDirectory()) {// 是目录
				File newFolder = new File(decompressLoc, fileName);// 若子目录不存在，则创建之
				if(isPrint){
					System.out.println(newFolder.getAbsolutePath());
				}
				if (!newFolder.exists()) {
					newFolder.mkdir();
				}
				// deCompressRecursion(zipIn, decompressLoc,charset);
			} else {// 是普通文件
				File singFile = new File(decompressLoc, fileName);
				if(isPrint){
					System.out.println(singFile.getAbsolutePath());
				}
				if (singFile.exists()) {// 若解压后的文件已经存在，则直接退出
					GUIUtil23.warningDialog("File \""
							+ singFile.getAbsolutePath() + "\" does  exist.");
					return false;
				}
				/**
				 * 以下四行代码是后来添加的，为了解决父目录不存在的问题
				 */
				File fatherFolder = singFile.getParentFile();
				if (!fatherFolder.exists()) {
					fatherFolder.mkdirs();
				}
				FileUtils.writeIn2Output(zipIn, new FileOutputStream(singFile),
						true, false);
			}
		}
		return true;
	}

	/***
	 * 解压zip
	 * 
	 * @param zipFile
	 * @param decompressLoc
	 *            :解压之后的文件所在目录
	 * @throws ArchiveException
	 * @throws IOException
	 */
	public static boolean decompress(String zipFile, String decompressLocStr,
			String charSet) throws ArchiveException, IOException {
		if(ValueWidget.isNullOrEmpty(decompressLocStr)){
			return false;
		}
		if (ValueWidget.isNullOrEmpty(charSet)) {
			charSet = SystemHWUtil.CHARSET_UTF;
		}
		File decompressLoc = new File(decompressLocStr.trim());
		return deCompressRecursion(zipFile, decompressLoc, charSet);
	}

	/***
	 * 获取指定目录下的文件和目录.
	 * 
	 * @param directory
	 * @param folder
	 * @return
	 */
	public static List<ZipArchiveEntry> getZipFileListRecursion(File directory,
			String folder) {
		List<ZipArchiveEntry> zipEntrys = new ArrayList<ZipArchiveEntry>();
		File[] fileArr = directory.listFiles();
		if(ValueWidget.isNullOrEmpty(fileArr)){
			return null;
		}
		for (int i = 0; i < fileArr.length; i++) {
			File file = fileArr[i];
			if (file.isDirectory()) {// 是文件夹
				String newFolder = null;
				if (ValueWidget.isNullOrEmpty(folder)) {
					newFolder = file.getName();
				} else {
					newFolder = folder + SystemHWUtil.SEPARATOR
							+ file.getName();
				}
				ZipArchiveEntry zipEntry = new ZipArchiveEntry(file, newFolder);
				zipEntrys.add(zipEntry);
				zipEntrys.addAll(getZipFileListRecursion(file, newFolder));
			} else {// 是普通的文件
				String entryName = null;
				if (ValueWidget.isNullOrEmpty(folder)) {
					entryName = file.getName();
				} else {
					entryName = folder + SystemHWUtil.SEPARATOR
							+ file.getName();
				}
				ZipArchiveEntry zipEntry = new ZipArchiveEntry(file, entryName);
				zipEntrys.add(zipEntry);
			}
		}

		return zipEntrys;
	}

	/***
	 * 判断是否是zip文件，注意：后缀名必须是zip
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isZip(String filePath) {
		if (filePath.endsWith("zip") || filePath.endsWith("ZIP")||filePath.toLowerCase().endsWith("zip")) {
			FileInputStream fin;
			try {
				fin = new FileInputStream(filePath);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return false;
			}
			try {
				ArchiveInputStream archOuts = new ArchiveStreamFactory()
						.createArchiveInputStream(ArchiveStreamFactory.ZIP, fin);
				ZipArchiveInputStream zipIn = (ZipArchiveInputStream) archOuts;
				ZipArchiveEntry zipEntry = zipIn.getNextZipEntry();
				// System.out.println("zipEntry:"+zipEntry);
				if (ValueWidget.isNullOrEmpty(zipEntry)) {
					return false;
				}
			} catch (ArchiveException e) {
				// e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}

	/***
	 * 获取zip文件包含的文件列表（文件清单）
	 * 
	 * @param zipIn
	 * @param charset
	 * @param isCloseZipInputStream : 是否关闭zipIn
	 * @return
	 * @throws IOException
	 */
	public static List<ZipFileBean> deCompressRecursionFileList(
			ZipArchiveInputStream zipIn, String charset,boolean isCloseZipInputStream) throws IOException {
		if(zipIn==null){
			return null;
		}
		List<ZipFileBean> zipFiles = new ArrayList<ZipFileBean>();
		String decompressLoc = SystemHWUtil.SYSTEM_TEMP_FOLDER;
		ZipArchiveEntry zipEntry;
		if (ValueWidget.isNullOrEmpty(charset)) {
			charset = SystemHWUtil.CHARSET_UTF;
		}
		while (!ValueWidget.isNullOrEmpty(zipEntry = zipIn.getNextZipEntry())) {
			byte[] rawName = zipEntry.getRawName();
			String fileName = new String(rawName, charset);
			ZipFileBean zipFileBean = new ZipFileBean();
			// System.out.println(fileName);
			if (zipEntry.isDirectory()) {// 是目录
				File newFolder = new File(decompressLoc, fileName);// 若子目录不存在，则创建之
				// System.out.println(newFolder.getAbsolutePath());
				// if (!newFolder.exists()) {
				// newFolder.mkdir();
				// }
				zipFileBean.setDir(true);
				zipFileBean.setFileName(fileName);
				zipFileBean.setFilePath(newFolder.getAbsolutePath());
			} else {// 是普通文件
				File singFile = new File(decompressLoc, fileName);
				// System.out.println(singFile.getAbsolutePath());
				if (singFile.exists()) {// 若解压后的文件已经存在，则直接退出
					GUIUtil23.warningDialog("File \""
							+ singFile.getAbsolutePath() + "\" does  exist.");
					return null;
				}
				zipFileBean.setDir(false);
				zipFileBean.setFileName(fileName);// "editor_360llq/360mse_H080108.apk"
				zipFileBean.setFilePath(singFile.getAbsolutePath());
				long length = zipEntry.getSize();
				if (length < 0) {
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					IOUtils.copy(zipIn, os, SystemHWUtil.BUFF_SIZE_1024);// 解决zipEntry.getSize()
																			// 返回为-1
																			// 的问题
					zipFileBean.setContent(os.toByteArray());
					// zipFileBean.setContent(FileUtils.readBytes(zipIn));
				} else {
					zipFileBean.setContent(FileUtils.readBytesFromInputStream(
							zipIn, length, false));
				}

				// FileUtils.writeIn2Output(zipIn, new
				// FileOutputStream(singFile),
				// true, false);
			}
			zipFiles.add(zipFileBean);
		}
		if (isCloseZipInputStream) {
			zipIn.close();
		}
		return zipFiles;
	}

	private static ZipArchiveInputStream getZipArchiveInputStream(
			InputStream fin) throws ArchiveException {
		ArchiveInputStream archIns = new ArchiveStreamFactory()
				.createArchiveInputStream(ArchiveStreamFactory.ZIP, fin);
		return (ZipArchiveInputStream) archIns;
	}

	/***
	 * 从字节数组中获取zip压缩包
	 * 
	 * @param zipBytes
	 *            : zip压缩包
	 * @return
	 * @throws ArchiveException
	 */
	private static ZipArchiveInputStream getZipArchiveInputStream(
			byte[] zipBytes) throws ArchiveException {
		if(ValueWidget.isNullOrEmpty(zipBytes)){
			return null;
		}
		InputStream in = FileUtils.getByteArrayInputSreamFromByteArr(zipBytes);
		return getZipArchiveInputStream(in);
	}

	/***
	 * 从文件中读取zip压缩包
	 * 
	 * @param zipFile
	 * @param charset
	 * @return
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static List<ZipFileBean> deCompressRecursionFileList(File zipFile,
			String charset,boolean isCloseZipInputStream) throws IOException, ArchiveException {
		if(!zipFile.exists()){
			System.out.println("[deCompressRecursionFileList]zip file does not exist.");
			return null;
		}
		FileInputStream fin = new FileInputStream(zipFile);
		ZipArchiveInputStream zipIn = getZipArchiveInputStream(fin);

		return deCompressRecursionFileList(zipIn, charset,isCloseZipInputStream);
	}

	public static List<ZipFileBean> deCompressRecursionFileList(
			String zipFilePath, String charset,boolean isCloseZipInputStream) throws IOException,
			ArchiveException {
		File zipFile = new File(zipFilePath);
		if(!zipFile.exists()){
			System.out.println("[deCompressRecursionFileList]zip file does not exist.");
			return null;
		}
		return deCompressRecursionFileList(zipFile, charset,isCloseZipInputStream);
	}

	/***
	 * 从字节数组中解析zip压缩包
	 * 
	 * @param zipBytes
	 *            : zip压缩包
	 * @param charset
	 * @return
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static List<ZipFileBean> deCompressRecursionFileList(
			byte[] zipBytes, String charset,boolean isCloseZipInputStream) throws IOException,
			ArchiveException {
		if(ValueWidget.isNullOrEmpty(zipBytes)){
			return null;
		}
		ZipArchiveInputStream zipIn = getZipArchiveInputStream(zipBytes);
		return deCompressRecursionFileList(zipIn, charset,isCloseZipInputStream);
	}

	/***
	 * 以人性化的方式打印List<ZipFileBean> zipFiles
	 * 
	 * @param zipFiles
	 */
	public static void printZipFileBeans(List<ZipFileBean> zipFiles) {
		if(isPrint)
		System.out.println("是否是文件--------文件名----");
		for (int i = 0; i < zipFiles.size(); i++) {
			ZipFileBean zipFileBean = zipFiles.get(i);
			if(isPrint)
			System.out.println(String.valueOf(!zipFileBean.isDir()) + "\t\t"
					+ zipFileBean.getFileName());
		}
	}

	/***
	 * 解压缩单个文件.
	 * 
	 * @param zipIn
	 * @param decompressLoc
	 * @return
	 * @throws IOException
	 */
	public static byte[] deCompressSingleFile(ZipArchiveInputStream zipIn,
			String fileName2, String charset) throws IOException {
		ZipArchiveEntry zipEntry;
		if (ValueWidget.isNullOrEmpty(charset)) {
			charset = SystemHWUtil.CHARSET_UTF;
		}
		while (!ValueWidget.isNullOrEmpty(zipEntry = zipIn.getNextZipEntry())) {
			byte[] rawName = zipEntry.getRawName();
			String fileName = new String(rawName, charset);
			// System.out.println(fileName);
			if (!zipEntry.isDirectory()) {// 是普通文件
				if (fileName.equalsIgnoreCase(fileName2)
						|| fileName.contains(fileName2)) {

					byte[] bytes = FileUtils.readBytes3(zipIn);
					zipIn.close();
					zipIn=null;
					return bytes;
				}
			}
		}
		if(null!=zipIn){
			zipIn.close();
		}
		return null;
	}

	/***
	 * 通过文件名获取zip输入流
	 * 
	 * @param zipIn
	 * @param fileNameInZip
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	private static ZipArchiveInputStream getZipArchiveInputStream(
			ZipArchiveInputStream zipIn, String fileNameInZip, String charset)
			throws IOException {
		ZipArchiveEntry zipEntry;
		if (ValueWidget.isNullOrEmpty(charset)) {
			charset = SystemHWUtil.CHARSET_UTF;
		}
		zipIn.mark(0);
		while (!ValueWidget.isNullOrEmpty(zipEntry = zipIn.getNextZipEntry())) {
			byte[] rawName = zipEntry.getRawName();
			String fileName = new String(rawName, charset);
			// System.out.println(fileName);
			if (!zipEntry.isDirectory()) {// 是普通文件
				if (fileName.equalsIgnoreCase(fileNameInZip)
						|| fileName.contains(fileNameInZip)) {
					return zipIn;
				}
			}
		}
		return null;
	}

	/***
	 * 解压单个文件.
	 * 
	 * @param zipFile
	 * @param fileName2
	 * @param charset
	 * @return
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static byte[] deCompressSingleFile(File zipFile, String fileName2,
			String charset) throws IOException, ArchiveException {
		if (ValueWidget.isNullOrEmpty(charset)) {
			charset = SystemHWUtil.CHARSET_UTF;
		}
		FileInputStream fin = new FileInputStream(zipFile);
		ArchiveInputStream archIns = new ArchiveStreamFactory()
				.createArchiveInputStream(ArchiveStreamFactory.ZIP, fin);
		ZipArchiveInputStream zipIn = (ZipArchiveInputStream) archIns;
		return deCompressSingleFile(zipIn, fileName2, charset);
	}

	/***
	 * 更新zip 包
	 * 
	 * @param zipOut
	 *            ：要持久化的目标zip文件
	 * @param zipFileModifiedInfos
	 * @param zipIn
	 *            ：要读取的zip文件
	 * @throws IOException
	 */
	// public static void persistenceZip(ZipArchiveOutputStream zipOut,
	// List<ZipFileModifiedInfo> zipFileModifiedInfos,
	// ZipArchiveInputStream zipIn) throws IOException {
	// for (int i = 0; i < zipFileModifiedInfos.size(); i++) {
	// ZipFileModifiedInfo zipFileModifiedInfo = zipFileModifiedInfos
	// .get(i);
	// String fileNameInZip = zipFileModifiedInfo.getFileName();
	// if (zipFileModifiedInfo.isModifyName()) {
	// fileNameInZip = zipFileModifiedInfo.getNewName();
	// }
	// // 如果有修改字符编码，只针对文本文件
	// if (zipFileModifiedInfo.isModifyCharset()) {
	// addEntry(zipOut, zipFileModifiedInfo.getContent(),
	// fileNameInZip);
	//
	// } else {// 没有修改字符编码
	//
	// zipIn = getZipArchiveInputStream(zipIn, fileNameInZip,
	// SystemHWUtil.CHARSET_UTF);
	// addEntry(zipOut, zipIn, fileNameInZip);
	// }
	// }
	// closeZip(zipOut);
	// zipIn.close();
	// }
	public static void persistenceZip(ZipArchiveOutputStream zipOut,
			List<? extends ZipFileModifiedInfo> zipFileModifiedInfos)
			throws IOException {
		// ZipArchiveEntry zipEntry = new
		// ZipArchiveEntry(SystemHWUtil.getParentDir(zipFileModifiedInfos.get(0).getFileName()));
		// zipEntry.set
		// zipOut.putArchiveEntry(zipEntry);
		for (int i = 0; i < zipFileModifiedInfos.size(); i++) {
			ZipFileModifiedInfo zipFileModifiedInfo = zipFileModifiedInfos
					.get(i);
			String fileNameInZip = null;
			if (zipFileModifiedInfo.isModifyName()) {
				fileNameInZip = zipFileModifiedInfo.getNewName();
			}else{
				fileNameInZip = zipFileModifiedInfo.getFileName();
			}
			byte[] bytes = null;
			// 如果有修改文件内容，只针对文本文件
			if (zipFileModifiedInfo.isModifyContent()) {
				bytes = zipFileModifiedInfo.getNewContent();
			} else {// 没有修改字符编码
				bytes = zipFileModifiedInfo.getContent();
				// zipIn = getZipArchiveInputStream(zipIn, fileNameInZip,
				// SystemHWUtil.CHARSET_UTF);

			}
			if(isPrint)
			System.out.println("bytes.length:" + bytes.length);
			addEntry(zipOut, bytes, fileNameInZip, bytes.length);
		}
		closeZip(zipOut, false);
		// zipIn.close();
	}

	/***
	 * 更新zip包
	 * @param zipFile
	 * @param zipFileModifiedInfos
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static void persistenceZip(File zipFile,
			List<? extends ZipFileModifiedInfo> zipFileModifiedInfos)
			throws IOException, ArchiveException {
		FileOutputStream fou = new FileOutputStream(zipFile);
		ArchiveOutputStream archOuts = new ArchiveStreamFactory()
				.createArchiveOutputStream(ArchiveStreamFactory.ZIP, fou);
		if (archOuts instanceof ZipArchiveOutputStream) {
			ZipArchiveOutputStream zipOut = (ZipArchiveOutputStream) archOuts;
			persistenceZip(zipOut, zipFileModifiedInfos);
		}
	}
	// public static boolean modifyFileNameInZip(ZipArchiveInputStream zipIn,
	// String oldFileName2, String newFileName,String charset)throws IOException
	// {
	// ZipArchiveEntry zipEntry;
	// if (ValueWidget.isNullOrEmpty(charset)) {
	// charset = SystemHWUtil.CHARSET_UTF;
	// }
	// while (!ValueWidget.isNullOrEmpty(zipEntry = zipIn.getNextZipEntry())) {
	// byte[] rawName = zipEntry.getRawName();
	// String fileName = new String(rawName, charset);
	// zipIn.
	// zipEntry.
	// }
	// return false;
	// }

	public static boolean isPrint() {
		return isPrint;
	}

	public static void setPrint(boolean isPrint) {
		CompressZipUtil.isPrint = isPrint;
	}

	
}
