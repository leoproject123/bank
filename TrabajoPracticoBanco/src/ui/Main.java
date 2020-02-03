package ui;

import ui.manager.PanelManager;

public class Main {

	private PanelManager manager;

	public static void main(String[] args) {

		Main main = new Main();
		main.iniciarManager();
		main.showFrame();
	}

	public void iniciarManager() {
		manager = new PanelManager();
		manager.buildManager();
	}

	public void showFrame() {
		manager.showFrame();
	}

}
