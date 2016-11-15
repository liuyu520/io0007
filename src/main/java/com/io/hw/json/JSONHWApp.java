package com.io.hw.json;

import com.common.util.SystemHWUtil;
import com.common.util.WindowUtil;
import com.swing.component.UndoTextArea;
import com.swing.dialog.GenericFrame;
import com.swing.menu.MenuUtil2;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JSONHWApp extends GenericFrame {

	public static final String STATE_READONLY="read only";
	public static final String STATE_EDITABLE="can edit";
	private static final long serialVersionUID = 5834969449266906363L;
	private UndoTextArea inputTA;
	private UndoTextArea outputTA;
	private UndoTextArea stateTA;

	public static void main(String[] args) {
		new JSONHWApp().launchFrame();
	}

	/***
	 * 必须执行launchFrame ,layout3才会被调用
	 * @param contentPane
     */
	@Override
	public void layout3(Container contentPane) {
		super.setLoc(900, 400);
		contentPane.setLayout(new BorderLayout());
		JPanel buttonPane = new JPanel();

		JButton clearBtn = new JButton("clear input");
		clearBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				inputTA.setText("");
			}
		});
		buttonPane.add(clearBtn);

		JToggleButton toggleBtn = new JToggleButton("enable");
		toggleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isEditable = inputTA.isEditable();
				String stateInfo = null;
				if (isEditable) {
					inputTA.setEditable(false);
					stateInfo = STATE_READONLY;

				} else {
					inputTA.setEditable(true);
					stateInfo = STATE_EDITABLE;
				}
				setStateInfo(stateInfo);
			}
		});
		buttonPane.add(toggleBtn);

		JButton pasteBtn = new JButton("paste Input");
		pasteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputClipboard = WindowUtil.getSysClipboardText();
				if (StringUtils.isNotBlank(inputClipboard)) {
					inputTA.setText(inputClipboard);
				}
			}
		});
		buttonPane.add(pasteBtn);

		JSeparator separator=new JSeparator();
		separator.setOrientation(JSeparator.VERTICAL);
		Border border1 = BorderFactory.createLineBorder(Color.BLUE, 120);
//				new Color(148, 145, 140));

		separator.setBorder(border1);
		buttonPane.add(separator);

		JButton addQuotation = new JButton("add quotation");
		addQuotation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String input = inputTA.getText();
				String result = JSONHWUtil.addQuotationMarks(input);
				outputTA.setText(result);
			}
		});
		buttonPane.add(addQuotation);
		JButton deleteNBtn = new JButton("delete n");
		deleteNBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = outputTA.getText();
				// delete Carriage Return/Line

				outputTA.setText(SystemHWUtil.deleteAllCRLF(input));
			}
		});
		buttonPane.add(deleteNBtn);

		/**
		 * copy output textarea
		 */
		JButton copyBtn = new JButton("copy output");
		copyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String output = outputTA.getText();
				if (StringUtils.isNotBlank(output)) {
					WindowUtil.setSysClipboardText(output);
				}
			}
		});
		buttonPane.add(copyBtn);

		contentPane.add(buttonPane, BorderLayout.NORTH);

		JPanel centInputPane = new JPanel();
		centInputPane.setLayout(new GridLayout(1, 2));
		this.inputTA = new UndoTextArea();
        MenuUtil2.setPopupMenu(this.inputTA, null/*Map<String, ActionCallback> actionCallbackMap */);
        this.inputTA.setLineWrap(true);
		this.inputTA.setWrapStyleWord(true);
		this.inputTA.setEditable(false);

		this.outputTA = new UndoTextArea();
        MenuUtil2.setPopupMenu(this.outputTA, null/*Map<String, ActionCallback> actionCallbackMap */);
        this.outputTA.setEditable(false);
		this.outputTA.setLineWrap(true);
		this.outputTA.setWrapStyleWord(true);

		JScrollPane sp_input = new JScrollPane(this.inputTA);
		Border border_input = BorderFactory.createEtchedBorder(Color.white,
				new Color(148, 145, 140));
		TitledBorder inputTitle = new TitledBorder(border_input, "input");
		sp_input.setBorder(inputTitle);
		centInputPane.add(sp_input);

		JScrollPane sp_output = new JScrollPane(this.outputTA);
		Border border_output = BorderFactory.createEtchedBorder(Color.white,
				new Color(148, 145, 140));
		TitledBorder outputTitle = new TitledBorder(border_output, "out");
		sp_output.setBorder(outputTitle);
		centInputPane.add(sp_output);

		contentPane.add(centInputPane, BorderLayout.CENTER);

		contentPane.add(setStateBar(), BorderLayout.SOUTH);
	}

	private void setStateInfo(String stateInfo) {
		stateTA.setText(stateInfo);
	}

	private JPanel setStateBar() {
		JPanel statePanel = new JPanel();
		stateTA = new UndoTextArea();
		stateTA.setEditable(false);
		stateTA.setText(STATE_READONLY);
		stateTA.setLineWrap(true);
		stateTA.setWrapStyleWord(true);

		statePanel.setLayout(new BorderLayout());
		statePanel.add(stateTA);
		return statePanel;
	}

}
