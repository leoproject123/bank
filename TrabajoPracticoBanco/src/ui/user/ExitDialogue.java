package ui.user;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitDialogue implements ActionListener {
	private static ExitDialogue instance;

	public static ExitDialogue getInstance() {
		if (instance == null)
			instance = new ExitDialogue();
		return instance;
	}

	public void actionPerformed(ActionEvent e) {
		String[] options = { "Salir", "Cancelar" };
		int option = JOptionPane.showOptionDialog(null, "¿Estás seguro que querés salir?", "Adios!",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
		if (option == JOptionPane.YES_OPTION)
			System.exit(0);
	}
}
