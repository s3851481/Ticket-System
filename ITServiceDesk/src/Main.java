
public class Main {
	private BackEnd backEnd;
	private ServiceDesk console;

	public Main() {
		this.backEnd = new BackEnd();
		this.console = new ServiceDesk(backEnd);
	}

	public static void main(String[] args) {
		Main application = new Main();
	}
}

