package me.williandrade.protocol;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplitProtocol implements Protocol {

	private File file;
	private Integer maxSize;

	public SplitProtocol(File file, Integer maxSize) {
		super();
		this.file = file;
		this.maxSize = maxSize;
	}

	@Override
	public String parse() throws Exception {
		byte[] fileBytes = Files.readAllBytes(this.file.toPath());

		String destPath = file.getAbsolutePath();

		int length = fileBytes.length;
		int rest = fileBytes.length;
		int count = 0;

		byte[] data = new byte[this.maxSize];
		while (rest > 0) {
			int arrSize;

			if (rest > this.maxSize) {
				arrSize = this.maxSize;
			} else {
				arrSize = rest;
			}

			int begin = length - rest;
			data = Arrays.copyOfRange(fileBytes, begin, begin + arrSize);

			String fileName = destPath;
			if (count > 0) {
				fileName += "." + count;
			}

			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(data);
			fos.close();

			count++;
			rest = rest - arrSize;
		}

		return destPath;
	}

	@Override
	public String recover() throws Exception {
		List<File> files = new ArrayList<>();
		files.add(this.file);

		String absolutePath = this.file.getAbsolutePath();

		int count = 1;
		int totalSize = Files.readAllBytes(this.file.toPath()).length;
		
		while (true) {
			File file = new File(absolutePath + "."+ count);
			if (file.exists()) {
				count++;
				totalSize += Files.readAllBytes(file.toPath()).length;
				files.add(file);
			} else {
				break;
			}
		}
		
		byte[] result = new byte[totalSize];
		int begin = 0;

		for (int i = 0; i < files.size(); i++) {
			File file = files.get(i);
			byte[] fileBytes = Files.readAllBytes(file.toPath());
			int end = begin + fileBytes.length;

			System.arraycopy(fileBytes, 0, result, begin, fileBytes.length);
			begin = end;
			file.delete();
		}

		FileOutputStream fos = new FileOutputStream(absolutePath);
		fos.write(result);
		fos.close();

		return absolutePath;
	}

}
