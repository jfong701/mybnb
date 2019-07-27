package jdbc;

public class Main {

	public static boolean debug = true;
	public static void main(String[] args) {
		CommandLine commandLine = new CommandLine();
		if (commandLine.startSession() && commandLine.execute()) {
			commandLine.endSession();
		}

	}

}
