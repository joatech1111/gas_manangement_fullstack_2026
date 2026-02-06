package com.joainfo.common.util.jdbc;

/**
 * JdbcSelectException
 * @author 백원태
 *
 */
public class JdbcSelectException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6773295090864891865L;
	
	
	/**
     * JdbcSelectException 디폴트 생성자<br>
     * RuntimeException 디폴트 생성자를 override한다. 
     */
    public JdbcSelectException() {
        super();
    }

    /**
     * JdbcSelectException 생성자<br>
     * RuntimeException의 java.lang.String을 매개변수로 전달 받는 생성자를 override 한다.
     * @param message 생성자에 전달되는 값.
     */
    public JdbcSelectException(String message) {
        super(message);
    }


}
