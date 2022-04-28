package vn.gpay.jitin.core.base;

public class ResponseError {
	public int getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public static int getErrcodeRuntimeException() {
		return ERRCODE_RUNTIME_EXCEPTION;
	}
	public static int getErrcodeUnknownError() {
		return ERRCODE_UNKNOWN_ERROR;
	}
	public static final int ERRCODE_RUNTIME_EXCEPTION 	= 1000;
	public static final int ERRCODE_UNKNOWN_ERROR 		= 9999;
	private int errorcode;
	private String message;
	public ResponseError() {
		this.errorcode	=	ERRCODE_UNKNOWN_ERROR;
		this.message	=	"Unknown error";
	}
	
}
