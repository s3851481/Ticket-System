
public class Main {
	private BackEnd backEnd;
	@SuppressWarnings("unused")
	private ServiceDesk console;

	public Main() {
		this.backEnd = new BackEnd();
		this.console = new ServiceDesk(backEnd);
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Main application = new Main();
	}
}

