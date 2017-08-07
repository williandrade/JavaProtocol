package me.williandrade.converter;

import java.io.File;

import me.williandrade.protocol.PatronusProtocol;
import me.williandrade.protocol.SplitProtocol;

public class BasicConverter implements Converter {

	private String path = "";
	private Integer maxSize;

	public BasicConverter(String path) {
		super();
		this.path = path;
		this.maxSize = 1000 * 10;
	}

	@Override
	public String pack() throws Exception {
		PatronusProtocol patronus = new PatronusProtocol(new File(this.path));
		String outPutFile = patronus.parse();

		SplitProtocol split = new SplitProtocol(new File(outPutFile), this.maxSize);
		String result = split.parse();

		return result;
	}

	@Override
	public String unpack() throws Exception {
		SplitProtocol split = new SplitProtocol(new File(this.path), this.maxSize);
		String result = split.recover();

		File f = new File(result);
		PatronusProtocol patronus = new PatronusProtocol(f);
		String outPutFile = patronus.recover();

		f.delete();

		return outPutFile;
	}

	// silo

}
