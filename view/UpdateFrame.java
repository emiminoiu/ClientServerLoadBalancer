package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Model.Elev;
import controller.ClientMain;

public class UpdateFrame extends JDialog {
	private JLabel idLabel;
	private JLabel numeLabel;
	private JLabel prenumeLabel;
	private JLabel varstaLabel;
	private JTextField id;
	private JTextField nume;
	private JTextField prenume;
	private JTextField varsta;
	private JButton saveBtn;
	Elev elev;
	
	public UpdateFrame(ClientMain parent, Elev e)
	{
		super(parent, true);
		this.setLayout(new GridLayout(0,2));
		idLabel =  new JLabel("id");
		numeLabel =  new JLabel("nume");
		prenumeLabel =  new JLabel("prenume");
		varstaLabel =  new JLabel("varsta");
		id = new JTextField(new Integer(e.getId()).toString());
		id.setEnabled(false);
		nume = new JTextField(e.getNume());
		prenume = new JTextField(e.getPrenume());
		varsta = new JTextField(new Integer(e.getVarsta()).toString());
	
		saveBtn = new JButton("Save");
		add(idLabel);
		add(id);
		
		
		add(numeLabel);
		add(nume);		
		
		add(prenumeLabel);
		add(prenume);		
		
		add(varstaLabel);
		add(varsta);
		add(saveBtn);
		
		saveBtn.addActionListener(new ActionListener()
		{

					@Override
					public void actionPerformed(ActionEvent e) {
						elev = new Elev();
						elev.setId(new Integer(id.getText()).intValue());
						elev.setNume(nume.getText());
						elev.setPrenume(prenume.getText());
						elev.setVarsta(new Integer(varsta.getText()).intValue());
						
						UpdateFrame.this.dispose();
						
						
					}
			
		});
		pack();
		
		
		setVisible(true);
	}

	public UpdateFrame(ClientMain parent)
	{
		super(parent, true);
		this.setLayout(new GridLayout(0,2));
		idLabel =  new JLabel("id");
		numeLabel =  new JLabel("nume");
		prenumeLabel =  new JLabel("prenume");
		varstaLabel =  new JLabel("varsta");
		id = new JTextField();
		nume = new JTextField();
		prenume = new JTextField();
		varsta = new JTextField();
	
		saveBtn = new JButton("Save");
		add(idLabel);
		add(id);
		
		
		add(numeLabel);
		add(nume);		
		
		add(prenumeLabel);
		add(prenume);		
		
		add(varstaLabel);
		add(varsta);
		add(saveBtn);
		
		saveBtn.addActionListener(new ActionListener()
		{

					@Override
					public void actionPerformed(ActionEvent e) {
						elev = new Elev();
						elev.setId(new Integer(id.getText()).intValue());
						elev.setNume(nume.getText());
						elev.setPrenume(prenume.getText());
						elev.setVarsta(new Integer(varsta.getText()).intValue());
						
						UpdateFrame.this.dispose();
						
						
					}
			
		});
		pack();
		
		
		setVisible(true);
	}

	public Elev getElev()
	{
		return elev;
	}
}
