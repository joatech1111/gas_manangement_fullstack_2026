package com.joainfo.common.util.jdbc;

/**
 * JdbcUpdateException
 * @author 백원태
 *
 */
public class JdbcUpdateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3701487230522564489L;

	/**
     * JdbcUpdateException 디폴트 생성자<br>
     * RuntimeException 디폴트 생성자를 override한다. 
     */
    public JdbcUpdateException() {
        super();
    }

    /**
     * JdbcUpdateException 생성자<br>
     * RuntimeException의 java.lang.String을 매개변수로 전달 받는 생성자를 override 한다.
     * @param message 생성자에 전달되는 값.
     */
    public JdbcUpdateException(String message) {
        super(message);
    }


}
