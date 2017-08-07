package me.williandrade.protocol;

public interface Protocol {

	public String parse() throws Exception;

	public String recover() throws Exception;

}
