package me.williandrade.protocol;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PatronusProtocol implements Protocol {

	private File file;
	private String outPutPath;
	private static final String PREFIX = ".patronus";
	private static final String END = "------------------CONTENT------------------";

	private static final String NAME_FIELD = "name";
	private static final String TYPE_FIELD = "type";
	private static final String SIZE_FIELD = "size";

	public PatronusProtocol(File file) {
		super();
		this.file = file;
		String path = this.file.getPath();
		path = path.substring(0, path.lastIndexOf(File.separator));
		this.outPutPath = path;
	}

	@Override
	public String parse() throws Exception {
		byte[] result = null;

		byte[] headBytes = null;
		byte[] fileBytes = null;

		if (this.file == null) {
			throw new Exception("");
		}

		fileBytes = Files.readAllBytes(this.file.toPath());

		StringBuilder head = new StringBuilder();

		String[] nameSplited = file.getName().split("\\.");
		String fileName = nameSplited[0];
		String fileType = nameSplited[1];

		head.append(this.NAME_FIELD).append("=").append(fileName).append("\n");
		head.append(this.TYPE_FIELD).append("=").append(fileType).append("\n");
		head.append(this.SIZE_FIELD).append("=").append(fileBytes.length).append("\n");
		head.append(this.END).append("\n");

		headBytes = head.toString().getBytes();

		result = new byte[headBytes.length + fileBytes.length];
		System.arraycopy(headBytes, 0, result, 0, headBytes.length);
		System.arraycopy(fileBytes, 0, result, headBytes.length, fileBytes.length);

		String destPath = this.outPutPath + "/" + this.file.getName() + this.PREFIX;

		FileOutputStream fos = new FileOutputStream(destPath);
		fos.write(result);
		fos.close();

		return destPath;
	}

	@Override
	public String recover() throws Exception {
		Map<String, String> values = new HashMap<>();

		byte[] fileBytes = Files.readAllBytes(this.file.toPath());

		InputStream is = new ByteArrayInputStream(fileBytes);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		for (String line; (line = br.readLine()) != null;) {
			if (line.equalsIgnoreCase(this.END)) {
				break;
			} else {
				String[] splited = line.split("=");
				String key = splited[0];
				String value = splited[1];

				values.put(key, value);
			}
		}

		Integer size = Integer.parseInt(values.get(this.SIZE_FIELD));
		byte[] data = new byte[size];

		data = Arrays.copyOfRange(fileBytes, fileBytes.length - size, fileBytes.length);

		String destPath = this.outPutPath + "/" + this.file.getName().replaceAll(this.PREFIX, "");

		FileOutputStream fos = new FileOutputStream(destPath);
		fos.write(data);
		fos.close();

		return destPath;
	}

}
