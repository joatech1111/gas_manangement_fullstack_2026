package com.joainfo.common.util.jdbc;

/**
 * JdbcDeleteException
 * @author 백원태
 *
 */
public class JdbcDeleteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4333462575264699677L;
	

	/**
     * JdbcDeleteException 디폴트 생성자<br>
     * RuntimeException 디폴트 생성자를 override한다. 
     */
    public JdbcDeleteException() {
        super();
    }

    /**
     * JdbcDeleteException 생성자<br>
     * RuntimeException의 java.lang.String을 매개변수로 전달 받는 생성자를 override 한다.
     * @param message 생성자에 전달되는 값.
     */
    public JdbcDeleteException(String message) {
        super(message);
    }


}
