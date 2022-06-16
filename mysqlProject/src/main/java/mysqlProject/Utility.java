package mysqlProject;

public class Utility {
	public static String toJSON(String source) {
		return source.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r\n", "\\n");
	}
}
