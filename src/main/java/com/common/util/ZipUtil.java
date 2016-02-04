package com.common.util;

import com.io.hw.file.util.FileUtils;
import com.string.widget.util.RandomUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 问题：中文乱码
 * Description User: I8800
 */
public final class ZipUtil {

	/**
	 * 获得zip压缩包中文件数(包括目录)
	 * 
	 * @param zipFile
	 * @return
	 */
	public static int getFiles(final ZipFile zipFile) {
		return zipFile.size();
	}

	/**
	 * 获得zip压缩包二进制数组中文件数(包括目录)
	 * 
	 * @param zipBytes
	 * @return
	 * @throws IOException
	 */
	public static int getFiles(final byte[] zipBytes) throws IOException {
		int files = 0;
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				zipBytes);
		ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);
		while (zipInputStream.getNextEntry() != null) {
			files++;
		}
		zipInputStream.closeEntry();
		zipInputStream.close();
		byteArrayInputStream.close();
		return files;
	}

	/**
	 * 获得zip压缩包中文件数(包括目录)
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static int getFiles(final String filename) throws IOException {
		ZipFile zipFile = new ZipFile(filename);
		int files = getFiles(zipFile);
		zipFile.close();
		return files;
	}

	/**
	 * 获得zip压缩包中文件数(包括目录)
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static int getFiles(final File file) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		int files = getFiles(zipFile);
		zipFile.close();
		return files;
	}

	/**
	 * 从zip包中读取给定文件名的内容
	 * 
	 * @param zipFilename
	 * @param name
	 *            获得内容的文件全文件名 注：大小写敏感
	 * @return
	 * @throws IOException
	 */
	public static byte[] getContent(final String zipFilename, final String name)
			throws IOException {
		ZipFile zipFile = new ZipFile(zipFilename);
		byte[] bytes = getContent(zipFile, name);
		zipFile.close();
		return bytes;
	}

	/**
	 * 从zip包中读取给定文件名的内容
	 * 
	 * @param file
	 * @param name
	 *            获得内容的文件全文件名 注：大小写敏感
	 * @return
	 * @throws IOException
	 */
	public static byte[] getContent(final File file, final String name)
			throws IOException {
		ZipFile zipFile = new ZipFile(file);
		byte[] bytes = getContent(zipFile, name);
		zipFile.close();
		return bytes;
	}

	/**
	 * 从zip包中读取给定文件名的内容
	 * 
	 * @param zipFile
	 * @param name
	 *            获得内容的文件全文件名 注：大小写敏感
	 * @return
	 * @throws IOException
	 */
	public static byte[] getContent(final ZipFile zipFile, final String name)
			throws IOException {
		ZipEntry zipEntry = zipFile.getEntry(name);
		return getContent(zipFile, zipEntry);
	}

	/**
	 * 从zip包中读取给定文件名的内容
	 * 
	 * @param zipFile
	 * @param zipEntry
	 * @return
	 * @throws IOException
	 */
	public static byte[] getContent(final ZipFile zipFile,
			final ZipEntry zipEntry) throws IOException {
		InputStream inputStream = zipFile.getInputStream(zipEntry);
		byte[] buffer = new byte[1024];
		byte[] bytes = new byte[0];
		int length;
		while ((length = (inputStream.read(buffer))) != -1) {
			byte[] readBytes = new byte[length];
			System.arraycopy(buffer, 0, readBytes, 0, length);
			bytes = SystemHWUtil.mergeArray(bytes, readBytes);
		}
		inputStream.close();
		return bytes;
	}
	public static byte[] getContent2(final ZipFile zipFile, final ZipEntry zipEntry)
            throws IOException {
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        int length = inputStream.available();
        byte[] bytes = new byte[length];
        inputStream.read(bytes);
        inputStream.close();
        return bytes;
    }


	/**
	 * 从二进制zip包byte数组中获取给定文件名的内容
	 * 
	 * @param zipBytes
	 * @param name
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static byte[] getContent(final byte[] zipBytes, final String name)
			throws IOException {
		File file = File.createTempFile(RandomUtils.getTimeRandom(), "."
				+ "zip");
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(zipBytes);
		fileOutputStream.flush();
		fileOutputStream.close();
		byte[] bytes = getContent(file, name);
		file.delete();
		return bytes;
	}

	/**
	 * @param zipFilename
	 * @param filename
	 * @throws IOException
	 */
	public static void add(final String zipFilename, final String filename)
			throws IOException {
		File zipFile = new File(zipFilename);
		File file = new File(filename);
		add(zipFile, file);
	}

	/**
	 * @param zipFile
	 * @param file
	 * @throws IOException
	 */
	public static void add(final File zipFile, final File file)
			throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		int length = fileInputStream.available();
		byte[] bytes = new byte[length];
		fileInputStream.read(bytes);
		fileInputStream.close();
		add(zipFile, file.getName(), bytes);
	}

	/**
	 * @param zipFile
	 * @param filename
	 * @param bytes
	 * @throws IOException
	 */
	public static void add(final File zipFile, final String filename,
			final byte[] bytes) throws IOException {
		ZipFile zip = new ZipFile(zipFile);
		Enumeration entries = zip.entries();
		FileOutputStream outputStream = new FileOutputStream(zipFile, true);
		ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			addEntry(zipOutputStream, entry,
					getContent(zipFile, entry.getName()));
		}
		ZipEntry zipEntry = new ZipEntry(filename);
		addEntry(zipOutputStream, zipEntry, bytes);

		zipOutputStream.flush();
		zipOutputStream.close();

		outputStream.flush();
		outputStream.close();
		zip.close();
	}

	/**
	 * @param zipFile
	 * @param filename
	 * @throws IOException
	 */
	public static void delete(final File zipFile, final String filename)
			throws IOException {
		ZipFile zip = new ZipFile(zipFile);
		Enumeration entries = zip.entries();
		FileOutputStream outputStream = new FileOutputStream(zipFile, true);
		ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			if (!entry.getName().equalsIgnoreCase(filename)) {
				addEntry(zipOutputStream, entry,
						getContent(zipFile, entry.getName()));
			}
		}
		zipOutputStream.flush();
		zipOutputStream.close();

		outputStream.flush();
		outputStream.close();
		zip.close();
	}

	/**
	 * 添加单个ZipEntry
	 * 
	 * @param zipOutputStream
	 * @param zipEntry
	 * @param bytes
	 * @throws IOException
	 */
	private static void addEntry(ZipOutputStream zipOutputStream,
			ZipEntry zipEntry, byte[] bytes) throws IOException {
		zipOutputStream.putNextEntry(zipEntry);
		zipOutputStream.write(bytes);
		zipOutputStream.flush();
		zipOutputStream.closeEntry();
	}

	/**
	 * 获得zip包中的文件名列表
	 * 
	 * @param zipFile
	 * @return
	 */
	public static List<String> list(final ZipFile zipFile) {
		List<String> list = new ArrayList<String>();
		Enumeration zipEntries = zipFile.entries();
		while (zipEntries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) zipEntries.nextElement();
			list.add(entry.getName());
		}
		return list;
	}

	
	/**
	 * 获得zip包中的文件名列表
	 * 
	 * @param file
	 * @return
	 */
	public static List<String> list(final File file) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		List<String> filenameList = list(zipFile);
		zipFile.close();
		return filenameList;
	}

	public static InputStream getInputStreamFromZip(final ZipFile zipFile,
			final String name) throws IOException {
		ZipEntry zipEntry = zipFile.getEntry(name);
		InputStream inputStream = zipFile.getInputStream(zipEntry);
		return inputStream;
	}

	/***
	 * 
	 * @param zipFilename
	 * @param name
	 * @return InputStream
	 * @throws IOException
	 */
	public static InputStream getInputStreamFromZip(final String zipFilename,
			final String name) throws IOException {
		ZipFile zipFile = new ZipFile(zipFilename);
		return getInputStreamFromZip(zipFile, name);
	}

	public static InputStream getInputStreamFromZip(final File file,
													final String name) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		return getInputStreamFromZip(zipFile, name);
	}

	/**
	 * 获得zip包中的文件名列表
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static List<String> list(final String filename) throws IOException {
		File file = new File(filename);
		return list(file);
	}

	/**
	 * 把源文件夹压缩成zip包
	 * 
	 * @param zipFile
	 * @param srcFolder
	 * @throws IOException
	 */
	public static void compress(final File zipFile, final String srcFolder)
			throws IOException {
		File folder = new File(srcFolder);
		FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
				fileOutputStream);
		ZipOutputStream zipOutputStream = new ZipOutputStream(
				bufferedOutputStream);
		if (!folder.isDirectory()) {
			compress(zipOutputStream, folder, srcFolder);
		} else {
			List<String> filenameList = listFilename(srcFolder);
			for (String filename : filenameList) {
				File file = new File(filename);
				compress(zipOutputStream, file, srcFolder);
			}
		}
		zipOutputStream.flush();
		zipOutputStream.close();
		bufferedOutputStream.flush();
		bufferedOutputStream.close();
		fileOutputStream.flush();
		fileOutputStream.close();
	}

	/**
	 * @param zipOutputStream
	 * @param file
	 * @throws IOException
	 */
	private static void compress(final ZipOutputStream zipOutputStream,
			final File file, final String srcFolder) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				fileInputStream, 1024);
		String fileName2=file.getPath().substring(
				srcFolder.length() + 1);
		System.out.println(fileName2);
		fileName2=SystemHWUtil.convertUTF2GBK(fileName2);
		ZipEntry entry = new ZipEntry(fileName2);
		zipOutputStream.putNextEntry(entry);
		byte[] data = new byte[bufferedInputStream.available()];
		data =FileUtils.readBytesFromInputStream(bufferedInputStream, bufferedInputStream.available(),true);
//		bufferedInputStream.read(data);
		zipOutputStream.write(data);
		zipOutputStream.flush();
//		bufferedInputStream.close();
		fileInputStream.close();
	}

	/**
	 * 解压zip包到目标目录里面
	 * 
	 * @param zipFile
	 * @param targetFolder
	 * @throws IOException
	 */
	public static void decompress(final File zipFile, final String targetFolder)
			throws IOException {
		List<String> filenameList = list(zipFile);
		for (String filename : filenameList) {
			File file = new File(targetFolder + File.separatorChar + filename);
			if (!file.exists()) {
				File parentPath = file.getParentFile();
				if (!parentPath.exists()) {
					parentPath.mkdirs();
				}
				file.createNewFile();
			}
			write(file, getContent(zipFile, filename));
		}
	}

	/**
	 * 解压zip包二进制数组到目标目录里面
	 * 
	 * @param zipBytes
	 * @param targetFolder
	 * @throws IOException
	 */
	public static void decompress(final byte[] zipBytes,
			final String targetFolder) throws IOException {
		File tempFile = File.createTempFile(RandomUtils.getTimeRandom(), "."
				+ "zip");
		FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
		fileOutputStream.write(zipBytes);
		fileOutputStream.flush();
		fileOutputStream.close();

		System.out.println(tempFile.getPath());
		decompress(tempFile, targetFolder);
		tempFile.delete();
	}

	/**
	 * 写入单个文件
	 * 
	 * @param file
	 * @param bytes
	 * @throws IOException
	 */
	private static void write(final File file, final byte[] bytes)
			throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(bytes);
		fileOutputStream.flush();
		fileOutputStream.close();
	}

	/**
	 * 遍历出所有的文件名和目录名
	 * 
	 * @param path
	 * @return
	 */
	public static List<String> listFilename(final String path) {
		List<String> list = new ArrayList<String>();
		File folder = new File(path);
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null && files.length > 0) {
				for (File file : files) {
					if (file.isDirectory()) {
						for (String filename : listFilename(file.getPath())) {
							list.add(filename);
						}
					} else {
						list.add(file.getPath());
					}
				}
			} else {
				list.add(folder.getPath());
			}
		} else {
			list.add(folder.getPath());
		}
		return list;
	}
	
	/**
     * 压缩文件 ,test ok!
     * compress
     * @param filePaths 存放文件物理路径的集合
     * @param fileNames 文件的名称(和文件路径对应，可以是中文)
     * @param outPath 压缩文件的输出路径(物理路径)
     */
    public static void reduceFile(List<String> filePaths, List<String> fileNames, String outPath) {
        if (null != filePaths && filePaths.size() > 0 && null != fileNames && fileNames.size()== filePaths.size()) {
            try {
                OutputStream fileOutput = new FileOutputStream(outPath);
                ZipOutputStream zipOutput = new ZipOutputStream(fileOutput);
                for (int i = 0; i < filePaths.size(); i++) {
                    File file = new File(filePaths.get(i));
                    if (file.exists() && !file.isDirectory()) {
                        InputStream input = new FileInputStream(file);
                        ZipEntry entry = new ZipEntry(SystemHWUtil.convertUTF2GBK(fileNames.get(i)));
                        zipOutput.putNextEntry(entry);
                        int readInt = 0;
                        while ((readInt = input.read()) != -1) {
                            zipOutput.write(readInt);
                        }
                        input.close();
                    }
                }
//                zipOutput.setEncoding("GBK");
                zipOutput.flush();
                zipOutput.closeEntry();
                zipOutput.close();
                fileOutput.flush();
                fileOutput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /***
     * Remaining issues: Chinese garbled
     * 
     * @param filePaths
     * @param fileNames
     * @param outPath
     */
    public static void compressFiles(List<String> filePaths, List<String> fileNames, String outPath) {
    	 if (null != filePaths && filePaths.size() > 0 && null != fileNames && fileNames.size()== filePaths.size()) {
             try {
                 OutputStream fileOutput = new FileOutputStream(outPath);
                 ZipOutputStream zipOutput = new ZipOutputStream(fileOutput);
                 for (int i = 0; i < filePaths.size(); i++) {
                     File file = new File(filePaths.get(i));
                     if (file.exists() && !file.isDirectory()) {
                         InputStream input = new FileInputStream(file);
                         BufferedInputStream binput=new BufferedInputStream(input);
                         ZipEntry entry = new ZipEntry(SystemHWUtil.convertUTF2GBK(fileNames.get(i)));
                         entry.setSize(binput.available());
                         zipOutput.putNextEntry(entry);
//                         BufferedOutputStream bout=new BufferedOutputStream(zipOutput);
                         byte[] bytes = new byte[binput.available()];
                         bytes =FileUtils.readBytesFromInputStream(binput, binput.available(),true);
//                         binput.read(bytes);
//                         int readInt = 0;
//                         while ((readInt = input.read(bytes)) != -1) {
//                        	 zipOutput.write(bytes,0,readInt);
//                         }
                         zipOutput.write(bytes);
                         input.close();
                     }
                 }
//                 zipOutput.setEncoding("GBK");
                 zipOutput.flush();
                 zipOutput.closeEntry();
                 zipOutput.close();
                 fileOutput.flush();
                 fileOutput.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }    	
    }
    
}
