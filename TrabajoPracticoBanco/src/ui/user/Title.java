package ui.user;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Title extends JPanel {

	private String mainTitle;

	public Title(String title) {
		this.mainTitle = title;
		buildForm();
	}

	private void buildForm() {

		this.setSize(500, 200);
		JLabel title = new JLabel(mainTitle);
		title.setForeground(Color.BLUE);
		this.add(title);

	}

}
