package com.aacoin.dig;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Response<T> implements Serializable {
	private static final long serialVersionUID = 7741130044036793402L;
	/**
	 * 成功标志
	 */
	public static final int OK = 0;
	/**
	 * 失败标志
	 */
	public static final int ERROR = 1;
	private int code;
	private String message;
	private T value;

	public Response() {
	}

	public Response(T value) {
		this.value = value;
	}

	public Response(int code, String message, T value) {
		this.code = code;
		this.message = message;
		this.value = value;
	}

	public Response(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public Response(int code, T value) {
		this.code = code;
		this.value = value;
	}

	public Response(int code) {
		this.code = code;
	}

	public Response(Result result) {
		this.code = result.getCode();
		this.message = result.getMessage();
	}

	public Response(Result result, T value) {
		this.code = result.getCode();
		this.message = result.getMessage();
		this.value = value;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
