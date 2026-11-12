package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 외부 설정 파일에서 DB·SFTP·메일 등의 비밀값을 읽어오는 클래스.
 * 기본 경로는 /etc/egocci.properties 이며,
 * 환경변수 EGOCCI_CONFIG 로 다른 경로를 지정할 수 있다.
 */
public class AppConfig {

	private static final String DEFAULT_PATH = "/etc/egocci.properties";
	private static final Properties props = new Properties();

	static {
		String path = System.getenv("EGOCCI_CONFIG");
		if (path == null || path.trim().isEmpty()) {
			path = DEFAULT_PATH;
		}
		try (InputStream in = new FileInputStream(path)) {
			props.load(in);
			System.out.println(">>> 설정 파일 로드 완료: " + path);
		} catch (IOException e) {
			System.out.println(">>> 설정 파일을 읽을 수 없습니다: " + path);
			e.printStackTrace();
		}
	}

	private AppConfig() {
	}

	public static String get(String key) {
		String value = props.getProperty(key);
		if (value == null) {
			throw new IllegalStateException("설정 값이 없습니다: " + key);
		}
		return value.trim();
	}
}
