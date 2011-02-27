/*
	Copyright 2008-2010 SPIRIT, http://www.spirit-intl.com/
	SPIRIT S.A. E-BUSINESS AND COMMUNICATIONS ENGINEERING 
	
	See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	  http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package org.universAAL.ui.handler.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.Scrollable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
//import org.osgi.service.log.LogService;
import org.universAAL.middleware.io.rdf.ChoiceItem;
import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.middleware.io.rdf.FormControl;
import org.universAAL.middleware.io.rdf.Group;
import org.universAAL.middleware.io.rdf.InputField;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.middleware.io.rdf.MediaObject;
import org.universAAL.middleware.io.rdf.Range;
import org.universAAL.middleware.io.rdf.Repeat;
import org.universAAL.middleware.io.rdf.Select;
import org.universAAL.middleware.io.rdf.Select1;
import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.middleware.io.rdf.TextArea;
import org.universAAL.middleware.io.rdf.SimpleOutput;
import org.universAAL.ui.handler.gui.RendererGuiConstants;

import layout.TableLayout;



public class SwingRenderer extends JFrame implements ActionListener, FocusListener, Scrollable, ChangeListener, ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4986118000986648808L;
	public static final String PERSONA_ASSOCIATED_LABEL = "urn:org.persona.dilog:AssociatedLabel";
	public static final String PERSONA_CLOCK_THREAD = "urn:org.persona.dilog:TheClockThread";
	public static final String PERSONA_FORM_CONTROL = "urn:org.persona.dilog:FormControl";
	public static final String PERSONA_PANEL_COLUMNS = "urn:org.persona.dilog:PanelColumns";
	public static final String title = "PERSONA-GUI-IOHandler";	

	public int getSliderValue() {
		return sliderValue;
	}
	
	public void setSliderValue(int sliderValue) {
		this.sliderValue = sliderValue;
	}
	
	public String getListValue() {
		return listValue;
	}

	public void setListValue(String listValue) {
		this.listValue = listValue;
	}

	private BundleContext context = null;
	private int sliderValue=0;
	private JList list;
	private String listValue="";
	private String mainContainer="";

	// Administrate Panels and Controls
	private Hashtable<FormControl, JComponent> ctrlMap = new Hashtable<FormControl, JComponent>();

	private JButton date = null;

	private GUIIOHandler theHandler = null;
	
	private RendererGuiConstants rendererGuiConstants;

	/**
	 * There can be many GraphicsConfiguration objects associated with a single
	 * graphics device, representing different drawing modes or capabilities.
	 * The corresponding native structure will vary from platform to platform.
	 * For example, on X11 windowing systems, each visual is a different
	 * GraphicsConfiguration. On Microsoft Windows, GraphicsConfigurations
	 * represent PixelFormats available in the current resolution and color
	 * depth.
	 * 
	 * @param gc
	 */
	public SwingRenderer(GUIIOHandler theHandler, BundleContext context)
			throws HeadlessException {
		super();
		this.context = context;
		this.theHandler = theHandler;
		this.rendererGuiConstants = new RendererGuiConstants();
	}

	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if (src instanceof JComponent)
			checkInput((JComponent) src);
	}

	private JLabel addControlLabel(FormControl ctrl) {
		Label cl = ctrl.getLabel();
		if (cl == null)
			return null;
		
		String labelStr = cl.getText();
		String iconURL = cl.getIconURL();
		if (isEmptyString(labelStr)  &&  isEmptyString(iconURL))
			return null;

		String htmlvalue = (labelStr == null)? ""
				: "<html>" + "<FONT COLOR=#"+ rendererGuiConstants.getLabelFontColor() +">" + cl + "</FONT>";
		JLabel label = new JLabel(htmlvalue);
		if (iconURL != null) {
			labelStr = ctrl.getHintString();
			label.setIcon(
					(labelStr == null)? new ImageIcon(iconURL)
					: new ImageIcon(iconURL, labelStr));
		}
		label.setMinimumSize(label.getPreferredSize());
		setLookAndFeelFont(rendererGuiConstants.getLabelFont(), rendererGuiConstants.getLabelFontStyle(), rendererGuiConstants.getLabelFontSize(), label);
		label.setHorizontalAlignment(SwingConstants.LEADING);
		return label;

	}
	
	private int calcGridColumns(FormControl[] children, int max) {
		int size = children.length;
		for (int i=0; i<children.length; i++) {
			Label l = children[i].getLabel();
			if (l != null)
				size++;
		}
		return (size < max) ? size : max;
	}
	
	private synchronized void checkInput(JComponent src) {
		Object o = src.getClientProperty(PERSONA_FORM_CONTROL);
		if (o instanceof InputField) {
			Object value = null;
			if (src instanceof JCheckBox)
				value = new Boolean(((JCheckBox) src).isSelected());
			else if (src instanceof JTextField)
				value = Activator.getTypeMapper().getJavaInstance(
						((JTextField) src).getText(),
						((InputField) o).getTypeURI());
			if (!((InputField) o).storeUserInput(value))
				highlightError(src);
		} else if (o instanceof Select1) {
			// TODO:?
				
			ChoiceItem choiceItem = (ChoiceItem)((Select1)o).getChoices()[((JComboBox)src).getSelectedIndex()];
			if (!((Select1)o).storeUserInput(choiceItem.getValue()))
				highlightError(src);
		} else if (o instanceof Select) {
			// TODO:?
		} else if (o instanceof Submit) {
			FormControl missing = ((Submit) o).getMissingInputControl();
			if (missing == null) {
				theHandler.dialogFinished((Submit) o);
				finish();
			} else
				highlightError(ctrlMap.get(missing));
		}
	}
	
	private void createBorderLabel(JPanel pane, Group g) {
		Label l = g.getLabel();
		if (l == null  ||  g.isRootGroup())
			return;
		
		String lt = l.getText();
		if (lt != null) {
//			lt = lt + " - " + pane.getName() + " - " +	pane.getLayout().getClass().toString();
			TitledBorder aux = BorderFactory.createTitledBorder(lt);
			aux.setTitleFont(getLookAndFeelFont(rendererGuiConstants.getGroupLabelFont(), rendererGuiConstants.getGroupLabelFontStyle(), rendererGuiConstants.getGroupLabelFontSize()));
			aux.setTitleColor(rendererGuiConstants.getGroupLabelFontColor());
			pane.setBorder(aux);
			pane.setName(lt);
			pane.putClientProperty(PERSONA_ASSOCIATED_LABEL, aux);
		}
	}

	private JComponent createDateLabel() {
		date = new JButton(rendererGuiConstants.getDateTimeLabel());
		ImageIcon di = getIcon(rendererGuiConstants.getPersonaSoundImage(), null);
		di.setDescription(rendererGuiConstants.getSoundLabel());
		if (date != null) {
			if (di != null)
				date.setIcon(di);
			date.setHorizontalTextPosition(SwingConstants.LEADING);
			date.setVerticalTextPosition(SwingConstants.CENTER);
			date.setContentAreaFilled(false);
			date.setBorderPainted(false);
			date.setFocusPainted(false);
		}
		return date;
	}
	
	private Container createGUIFooter(Group buttons) {
		JPanel southPane = new JPanel();
		southPane.setName(rendererGuiConstants.getFooterPanelName());
		southPane.setBackground(rendererGuiConstants.getDefaultBackgroundColor());
		southPane.setLayout(new BorderLayout());
	
		// add Date Label
//		if (date == null)
//			date = (JButton) createDateLabel();
//		southPane.add(date, BorderLayout.LINE_END);
//		southPane.putClientProperty(PERSONA_CLOCK_THREAD, new DateTime(date).runTheClock()); 
		
		// add the buttons
		if (buttons != null) {
			FormControl[] children = buttons.getChildren();
			if (children != null  &&  children.length > 0) {
				JPanel pane = new JPanel();
				pane.setName(rendererGuiConstants.getFooterPanelName());
				pane.setBackground(rendererGuiConstants.getDefaultBackgroundColor());
				pane.setLayout(new GridLayout(1, children.length));
				pane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
				for (int i = 0; i < children.length; i++)
					if (children[i] instanceof Submit) {
						JComponent child = renderSubmitControl((Submit) children[i]);
						if (child != null)
							pane.add(child);
					}
				pane.setMinimumSize(pane.getPreferredSize());
				pane.paintComponents(pane.getGraphics());
				pane.setAlignmentY(SwingConstants.LEADING);
				southPane.add(pane, BorderLayout.LINE_START);
			}
		}
		return southPane;
	}

	private Container createGUIHeader(String title, boolean isSystemMenu) {
		JPanel headerPane = new JPanel();
		headerPane.setName(rendererGuiConstants.getHeaderPanelName());
		double border = rendererGuiConstants.getScreenBorder();
        double fill = TableLayout.FILL;
        double preferred = TableLayout.PREFERRED;
        double sizeforApps[][] = {{border, fill, preferred}, {fill}};
        double sizeForMainMenu[][] = {{border, preferred, fill}, {fill}}; 
        TableLayout headerLayout = new TableLayout(isSystemMenu?sizeForMainMenu:sizeforApps);
        headerPane.setLayout (headerLayout);
        
		headerPane.setBackground(rendererGuiConstants.getDefaultBackgroundColor());
		
		JLabel logoLabel = new JLabel();
		JLabel titleLabel = new JLabel();
		ImageIcon icon = new ImageIcon();
		titleLabel.setForeground(rendererGuiConstants.getForegroundTitleColor());
		setLookAndFeelFont(rendererGuiConstants.getTitleFont(), rendererGuiConstants.getTitleFontStyle(), rendererGuiConstants.getTitleFontSize(), titleLabel);

		if (isSystemMenu)
		{
			titleLabel.setText((null == icon)?"PERSONA":"");
			icon = getIcon(rendererGuiConstants.getPersonaLogoImage(), null);
		}
		else
		{
			titleLabel.putClientProperty("Stretched", new Integer(1));
			icon = getIcon(rendererGuiConstants.getPersonaTitleImage(), null);
			if (title != null)
				titleLabel.setText(title);
			else
				titleLabel.setText("PERSONA");
			logoLabel.putClientProperty("Stretched", new Integer(1));
		}
		logoLabel.setIcon(icon);
		logoLabel.setMinimumSize(logoLabel.getPreferredSize());	
				
		headerPane.add(titleLabel, "1, 0, 2, 0");
		headerPane.add(logoLabel, (isSystemMenu)?"1, 0, 2, 0":"0, 0, 2, 0");
		return headerPane;
	}
	
	private Container createSubmits(Group submits){
		JPanel submitsPanel = new JPanel();

		//submitsPanel.setName(rendererGuiConstants.getSubmitsPanelName());
		submitsPanel.setBackground(rendererGuiConstants.getDefaultBackgroundColor());
		submitsPanel.setLayout(new BorderLayout());
		
		// add Date Label
//		if (date == null)
//			date = (JButton) createDateLabel();
//		submitsPanel.add(date, BorderLayout.PAGE_END);
//		submitsPanel.putClientProperty(PERSONA_CLOCK_THREAD, new DateTime(date).runTheClock()); //org.persona.dilog or org.persona.dialog?

		// add the buttons
		if (submits != null) {
			FormControl[] children = submits.getChildren();
			if (children != null  &&  children.length > 0) {
				JPanel pane = new JPanel();

				pane.setBackground(rendererGuiConstants.getDefaultBackgroundColor());
				pane.setName(rendererGuiConstants.getSubmitsPanelName());
				pane.setLayout(new GridLayout(children.length,2));
				pane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
				for (int i = 0; i < children.length; i++)
					if (children[i] instanceof Submit) {
						JComponent child = renderSubmitControl((Submit) children[i]);
						if (child != null)
							pane.add(child);
					}
				pane.setMinimumSize(pane.getPreferredSize());
				pane.paintComponents(pane.getGraphics());
				pane.setAlignmentY(SwingConstants.LEADING);
				submitsPanel.add(pane, BorderLayout.PAGE_START);
			}
		}
		
		JScrollPane submitsScrollPane = new JScrollPane(submitsPanel);
		submitsScrollPane.setBorder(null);
		submitsPanel.setName(rendererGuiConstants.getSubmitsPanelName());
				
		return submitsScrollPane;
	}

	public void finish() {
		ctrlMap.clear();
		this.setVisible(false);
		Container content = getContentPane();
		Component[] children = content.getComponents();
		if (children != null)
			for (int i=0; i<children.length; i++)
				if (children[i] instanceof JPanel) {
					Object o = ((JPanel) children[i]).getClientProperty(PERSONA_CLOCK_THREAD);
					if (o instanceof Thread)
						((Thread) o).interrupt();
				}
		content.removeAll();
		this.dispose();
	}
	
	public void focusGained(FocusEvent e) {
		// do nothing
	}
	
	public void focusLost(FocusEvent e) {
		Object src = e.getSource();
		if (src instanceof JComponent)
			checkInput((JComponent) src);
	}

	private ImageIcon getIcon(String name, String description) {
		URL r = null;
		ImageIcon icon = null;
		if (this.context != null) {
			Bundle b = null;
			try {
				b = this.context.getBundle();
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (b.getEntry(name) != null){
				String path = b.getEntry(name).getPath();
				r = b.getResource(path);
			}
		} else {
			r = SwingRenderer.class.getResource(name);
		}
		if (r != null) {
			if (description == null)
				icon = new ImageIcon(r);
			else
				icon = new ImageIcon(r, description);
			// wait for image tracker to return!
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (icon != null)
				return icon;
		}
		return null;
	}
	
	private void highlightError(JComponent jcomp) {
		Object label = jcomp.getClientProperty(PERSONA_ASSOCIATED_LABEL);
		if (label instanceof JLabel)
			((JLabel) label).setForeground(Color.RED);
		else if (label instanceof TitledBorder)
			((TitledBorder) label).setTitleColor(Color.RED);
		jcomp.requestFocus(true);
	}

	/**
	 * Sets the initial contents of our <code>JFrame</code>
	 * <code>Container</code>
	 */
	public void initDefaultContainer(Form f) {
		Container content = this.getContentPane();
		content.setBackground(rendererGuiConstants.getDefaultBackgroundColor());
		this.setMinimumSize(content.getPreferredSize());
		
        double border = rendererGuiConstants.getScreenBorder();
        double preferred = TableLayout.PREFERRED;
        double fill = TableLayout.FILL;
        double size[][] = {{border, fill, preferred, border}, {border, preferred, fill, preferred, border}};
        TableLayout layout = new TableLayout(size);
        content.setLayout (layout);
		content.add(createGUIHeader(f.getTitle(), f.isSystemMenu()), "0, 1, 1, 1");
	}
	
	private boolean isEmptyString(String str) {
		return str == null  ||  str.equals("");
	}
	
	void popMessage(Form f) {
		String msg = (String) f.getIOControls().getChildren()[0].getValue();
		FormControl[] submits = f.getSubmits().getChildren();
		String[] options = new String[submits.length];
		for (int i=0; i<submits.length; i++)
			options[i] = submits[i].getLabel().getText();
		int selection = JOptionPane.showOptionDialog(this, msg, f.getTitle(),
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null, options, options[0]);
		theHandler.dialogFinished((Submit) submits[selection]);
	}

	public void renderForm(Form f) {
		rendererGuiConstants.loadLayoutConstants(f.getTitle());
		
		setTitle(f.getTitle());
		initDefaultContainer(f);

		Container content = getContentPane();
		
		
		JPanel mainPanelData = new JPanel();
		
		
		mainPanelData.setBorder(null);
		mainPanelData.setBackground(rendererGuiConstants.getDefaultBackgroundColor());
		if (f.isSystemMenu())
		{
			content.add(createGUIFooter(f.getStandardButtons()), "1, 3, 2, 3");
			mainPanelData = renderGroupControl(f.getIOControls(), rendererGuiConstants.getDefaultMenuCols(), true);
		}
		else {		
			content.add(createSubmits(f.getSubmits()), "2, 1, 2, 3");
			mainPanelData = renderGroupControl(f.getIOControls(), rendererGuiConstants.getDefaultCols(), false);
		}
	
		JScrollPane mainScrollPane = new JScrollPane(mainPanelData); //an to balw stin koryfi meta to new JPanel den emfanizetai to mainPanel me ta menu buttons
		
		if (mainPanelData != null)
		{
			if (f.isSystemMenu())
				mainPanelData.setName(rendererGuiConstants.getMainMenuPanelName()); //setting name before adding the panel to scrollPane, will result in deleting the panel name by the scrollPane
			else
				mainPanelData.setName(rendererGuiConstants.getMainPanelPanelName()); //setting name before adding the panel to scrollPane, will result in deleting the panel name by the scrollPane
			
			mainPanelData.setBackground(rendererGuiConstants.getDefaultBackgroundColor());
		}

		mainScrollPane.setBorder(null);
		
		getContentPane().add(mainScrollPane, (f.isSystemMenu())? "1, 2, 2, 2":"1, 2, 1, 2");
		
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setUndecorated(true);
		
		
		Runnable redrawContainer = new Runnable(){
    	   public void run(){
    		   repaint();
    		   setVisible(true);
    		   redrawContainer(getContentPane(), 0, getContentPane().getName());
    		   getContentPane().validate();
    		   repaintIcons(getContentPane());
    		   getContentPane().validate();
    		   repaint();

    	   }
		};

		SwingUtilities.invokeLater(redrawContainer);
        repaint();	
        setVisible(true);
//        
////        content.validate();
////        repaint();
//        setVisible(false);
//		content.paintAll(getGraphics());
//		
//		setVisible(true);
		
	}
	
	public void repaintIcons(Container c){
		for (int i=0;i<c.getComponentCount();i++)
        {
			 try
			 {
				 boolean canBeSetIcon=false; //the Component contains setIcon, getIcon methods (JButton, JLabel etc)
				 int stretched = 0;
				 
				 if (c.getComponent(i) instanceof JComponent )
				 {
					 JComponent component = (JComponent)c.getComponent(i);
					 Method methList[] = component.getClass().getMethods();
					 for (int mIdx=0; mIdx<methList.length; mIdx++){
						 canBeSetIcon=(canBeSetIcon || (methList[mIdx].getName() == "getIcon"));
					 }				 
					 Object stretchedObj = component.getClientProperty("Stretched");
					 stretched = (null == stretchedObj)?0:((Integer)stretchedObj).intValue();
				 }

				 
				 if ((canBeSetIcon) && (stretched == 1)) 
				 {
					if (c.getComponent(i) instanceof JButton)
		        	{
			    		ImageIcon icn = (ImageIcon)((JButton)c.getComponent(i)).getIcon();
						 Object stretchedObj = ((JComponent)c.getComponent(i)).getClientProperty("verticallyStretchedToText");
						 int stretchedButtonToText = (null == stretchedObj)?0:((Integer)stretchedObj).intValue();
			    		
			    		if (null != icn){
				    		if ((((JButton)c.getComponent(i)).getText() != null) && (!((JButton)c.getComponent(i)).getText().isEmpty()) && (stretchedButtonToText == 1))
				    		{
				    			((JButton)c.getComponent(i)).setIcon(scaleIcon(icn.getImage(),(int)((JButton)c.getComponent(i)).getWidth() + rendererGuiConstants.getWrapSubmitIncreaseWidth(),(int)((JButton)c.getComponent(i)).getFont().getSize() +rendererGuiConstants.getWrapSubmitIncreaseHeight()));
				    			((JButton)c.getComponent(i)).setSize(((JButton)c.getComponent(i)).getWidth() + rendererGuiConstants.getWrapSubmitIncreaseWidth(), (int)((JButton)c.getComponent(i)).getFont().getSize() +rendererGuiConstants.getWrapSubmitIncreaseHeight());
				    			((JButton)c.getComponent(i)).setPreferredSize(new Dimension(Double.valueOf(((JButton)c.getComponent(i)).getPreferredSize().getWidth() + rendererGuiConstants.getWrapSubmitIncreaseWidth()).intValue(), (int)((JButton)c.getComponent(i)).getFont().getSize()+rendererGuiConstants.getWrapSubmitIncreaseHeight()));
				    			((JButton)c.getComponent(i)).setVerticalAlignment(SwingConstants.CENTER);
				    			((JButton)c.getComponent(i)).setVerticalTextPosition(SwingConstants.CENTER);
				    		}
				    		else
					    		((JButton)c.getComponent(i)).setIcon(scaleIcon(icn.getImage(),(int)((JButton)c.getComponent(i)).getWidth(),(int)((JButton)c.getComponent(i)).getHeight()));
			    		}
		        	}
					else
						if (c.getComponent(i) instanceof JLabel)
			        	{
				    		ImageIcon icn = (ImageIcon)((JLabel)c.getComponent(i)).getIcon();
				    		if (null != icn)
				    			((JLabel)c.getComponent(i)).setIcon(scaleIcon(icn.getImage(),(int)((JLabel)c.getComponent(i)).getWidth(),(int)((JLabel)c.getComponent(i)).getHeight()));
			        	}
				 }
	        	else if (c.getComponent(i) instanceof Container)
	        		repaintIcons((Container)c.getComponent(i));
			 }
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }
        }		
	}

	private JPanel renderGroupControl(Group ctrl, int maxCols, boolean isMainMenu) {
		FormControl[] children = ctrl.getChildren();
		if (children == null || children.length == 0)
			return null;

		JPanel pane = new JPanel();
		pane.putClientProperty("isMainMenu", isMainMenu);
		
		System.err.println(
			ctrl.getFormObject().getTitle() + "\n" +
			ctrl.getFormObject().getURI());
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints layoutConstraints = new GridBagConstraints();
		//layoutConstraints.insets = new Insets(5,5,5,5);
		layoutConstraints.anchor = layoutConstraints.NORTHWEST;
		
		gridBagLayout.setConstraints(pane, layoutConstraints);
		pane.setLayout(gridBagLayout);		
		pane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		pane.putClientProperty(PERSONA_FORM_CONTROL, ctrl);
		createBorderLabel(pane, ctrl);

		int maxCol = calcGridColumns(children, maxCols), col = 0, row = 0;
		pane.putClientProperty(PERSONA_PANEL_COLUMNS, new Integer(maxCol));
		for (int i = 0; i < children.length; i++) {
			JComponent child = null;
			boolean newLine = false;
			if (children[i] instanceof InputField) {
				child = renderInputControl((InputField) children[i], true);
			} else if (children[i] instanceof SimpleOutput) {
				child = renderInputControl((SimpleOutput) children[i], false);
			} else if (children[i] instanceof Select1) {
				child = renderSelect1Control((Select1) children[i]);
			} else if (children[i] instanceof Select) {
				child = renderSelectControl((Select) children[i]);
			} else if (children[i] instanceof Repeat) {
				FormControl[] repeats = ((Repeat) children[i]).getChildren();
				if (repeats == null  ||  repeats.length != 1)
					child = new JLabel("Corrupted repeat!");
				else if (repeats[0] instanceof Group) {
					// use the dummy group for rendering the children, but overwrite it by the two changes below
					child = renderGroupControl((Group) repeats[0], maxCol, false);
					// change the border
					createBorderLabel((JPanel) child, (Repeat) children[i]);
					// change the associated ctrl
					((JPanel) child).putClientProperty(PERSONA_FORM_CONTROL, children[i]);
				} else
					child = renderGroupControl((Group) children[i], maxCol, false);
				newLine = true;
			} else if (children[i] instanceof Group) {
				child = renderGroupControl((Group) children[i], maxCol, false);
				newLine = true;
			} else if (children[i] instanceof Submit) {
				// also instances of SubdialogTrigger can be treated the same
				child = renderSubmitControl((Submit) children[i]);
			} else if (children[i] instanceof MediaObject) {
				JLabel photographLabel = new JLabel();
				ImageIcon myImg=renderMediaObject((MediaObject)children[i]);
				photographLabel.setIcon(myImg);
				//photographLabel.putClientProperty("Stretched", new Integer(1));
				child=photographLabel;
				
			} else if (children[i] instanceof TextArea) {
				child=renderTextArea((TextArea)children[i]);
			} else if (children[i] instanceof Range) {
				Range tempRng=(Range)children[i];
				if((Integer)tempRng.getMaxValue()>15)
					child=renderSpinnerControl((Range)children[i]);
				else
					child=renderRangeControl((Range)children[i]);				
			}
			if (child == null)
				continue;
			Object[] added = new Object[]{child.getClientProperty(PERSONA_ASSOCIATED_LABEL), child};
			for (int j=0; j<2; j++)
				if (j == 1  ||  added[0] instanceof JLabel) {
					if (newLine && col > 0) {
						col = 0;
						row++;
					}
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.gridx = col;
					gbc.gridy = row;
					gbc.anchor = gbc.NORTHWEST;

					if (added[j] instanceof JPanel) {
						Object o = ((JPanel) added[j]).getClientProperty(PERSONA_PANEL_COLUMNS);
						if (o instanceof Integer)
							gbc.gridwidth = ((Integer) o).intValue();
					}
					pane.add((JComponent) added[j], gbc);
					if (newLine  ||  ++col == maxCol) {
						col = 0;
						row++;
					}
				}
		}
		Repeat r = (ctrl instanceof Repeat)? (Repeat) ctrl
				: (ctrl.getParentGroup() instanceof Repeat)? (Repeat) ctrl.getParentGroup()
						: null;
		if (r != null) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = (col > 0)? row+1 : row;
			gbc.gridwidth = maxCol;
			pane.add(new RepeatTablePane(r, pane.getComponents()), gbc);
		}
		pane.setMinimumSize(pane.getPreferredSize());
		pane.paintComponents(pane.getGraphics());
		pane.setAlignmentY(SwingConstants.LEADING);
		ctrlMap.put(ctrl, pane);
		return pane;
	}

	private JSlider renderRangeControl(Range rng){
		 Comparable<?> min_Value=rng.getMinValue();
		 int mnValue=(Integer)min_Value;
		 Comparable<?> max_Value=rng.getMaxValue();
		 int mxValue=(Integer)max_Value;
		 int initValue=(Integer)rng.getValue();
		JSlider rangeComp = new JSlider(JSlider.HORIZONTAL,
				mnValue, mxValue, initValue);
		rangeComp.addChangeListener(this);

       //Turn on labels at major tick marks.

		rangeComp.setMajorTickSpacing(10);
		rangeComp.setMinorTickSpacing(1);
		rangeComp.setPaintTicks(true);
		rangeComp.setPaintLabels(true);
		rangeComp.setBorder(
               BorderFactory.createEmptyBorder(0,0,10,0));
		setLookAndFeelFont(rendererGuiConstants.getSliderFont(), rendererGuiConstants.getSliderFontStyle(), rendererGuiConstants.getSliderFontSize(), rangeComp);
		return rangeComp;
	}	
	
	private JSpinner renderSpinnerControl(Range rng){
		Comparable<?> min_Value=rng.getMinValue();
		 int mnValue=(Integer)min_Value;
		 Comparable<?> max_Value=rng.getMaxValue();
		 int mxValue=(Integer)max_Value;
		 int initValue=(Integer)rng.getValue();
		 SpinnerModel sModel = new SpinnerNumberModel(initValue, //initial value
				 mnValue, //min
				 mxValue, //max
                 1);     //step
		 sModel.addChangeListener(this);
		 JSpinner spinner = new JSpinner(sModel);
		 return spinner;
	}
	
	private JScrollPane renderTextArea(TextArea txtArea){
		String initialValue=(String) txtArea.getValue();
		JTextPane textPane = createTextPane(initialValue);
		JScrollPane paneScrollPane = new JScrollPane(textPane);
        paneScrollPane.setPreferredSize(new Dimension(250, 155));
        paneScrollPane.setMinimumSize(new Dimension(10, 10));
        return paneScrollPane;
	}

    private JTextPane createTextPane(String initValue) {
        String[] initStyles =
                { "regular", "format"};

        JTextPane textPane = new JTextPane();
        StyledDocument doc = textPane.getStyledDocument();
        boolean myStyle=addStylesToDocument(doc);

        try {
            if(!myStyle) {
                doc.insertString(doc.getLength(), initValue,
                                 doc.getStyle(initStyles[0]));
            }else if(myStyle){
            	doc.insertString(doc.getLength(), initValue,
                        doc.getStyle(initStyles[1]));
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text into text pane.");
        }

        return textPane;
    }  
    
    protected boolean addStylesToDocument(StyledDocument doc) {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, rendererGuiConstants.getTextAreaFont());
        StyleConstants.setFontSize(def, rendererGuiConstants.getTextAreaFontSize());
        
        String fontStyle=rendererGuiConstants.getTextAreaFontStyle();
        Style s=null;
        boolean formatSet=false;
        if(fontStyle.equalsIgnoreCase("italic")){
        	s = doc.addStyle("format", regular);
            StyleConstants.setItalic(s, true);
            StyleConstants.setFontSize(s, rendererGuiConstants.getTextAreaFontSize());
            formatSet=true;
        }else if(fontStyle.equalsIgnoreCase("bold")){
        	s = doc.addStyle("format", regular);
            StyleConstants.setBold(s, true);
            StyleConstants.setFontSize(s, rendererGuiConstants.getTextAreaFontSize());
            formatSet=true;
        }
        return formatSet;
    }

	private ImageIcon renderMediaObject(MediaObject mo){
		try{
			if ((mo != null) && (mo.getContentURL() != null))
				if ((mo.getResolutionPreferredX() > 0) && (mo.getResolutionPreferredY() > 0))
					return scaleIcon(getIcon(mo.getContentURL(), null).getImage(), mo.getResolutionPreferredX(), mo.getResolutionPreferredY());
				else
					return getIcon(mo.getContentURL(), null);
			else
				System.err.println("Media object or mediaObject.ContentURL empty!");
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
	private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }	
	
	private JComponent renderInputControl(final FormControl ctrl, boolean isEditable) {
		JLabel label = addControlLabel(ctrl);

		Object initVal = ctrl.getValue();
		JComponent res = null;
		if (ctrl.isOfBooleanType()) {
			JCheckBox cb = new JCheckBox("");
			cb.setSelected(initVal instanceof Boolean
					&& ((Boolean) initVal).booleanValue());
			cb.setEnabled(isEditable);
			cb.setHorizontalTextPosition(SwingConstants.CENTER);
			cb.setBackground(rendererGuiConstants.getDefaultBackgroundColor());
			cb.addActionListener(this);
			res = cb;
		} else {
			JTextComponent tf;
			if (isEditable)
			{
				tf = new JTextField();
				FontMetrics fm= tf.getFontMetrics(tf.getFont());
				if (initVal != null)
					tf.setSize(fm.stringWidth(initVal.toString())/tf.getFont().getSize(), tf.getHeight());
				else
					((JTextField)tf).setColumns(10);
				((JTextField)tf).addActionListener(this);
			}
			else
			{
				tf = new JTextArea();
				((JTextArea)tf).setColumns(10); //minimum
				((JTextArea)tf).setLineWrap(false); //expand the textarea to the longest string (use \n for new line)	
			}
			
			if (initVal != null)
				tf.setText(initVal.toString());
			tf.setEditable(isEditable);
			
			tf.addFocusListener(this);
			res = tf;
		}
		setLookAndFeelFont(rendererGuiConstants.getEditFont(), rendererGuiConstants.getEditFontStyle(), (isEditable? rendererGuiConstants.getEditFontSizeEditable() : rendererGuiConstants.getEditFontSizeReadOnly()), res);
		ctrlMap.put(ctrl, res);
		res.putClientProperty(PERSONA_FORM_CONTROL, ctrl);
		if (label != null)
			res.putClientProperty(PERSONA_ASSOCIATED_LABEL, label);
		if ((!isEditable) && (initVal == null))
			return null;
		else
			return res;
	}

	private JComponent renderSelect1Control(Select1 ctrl) {		
//		return renderSelectControl(ctrl);
		// get Input Label

		JLabel label = addControlLabel(ctrl);
		JComponent res = null;
		if (ctrl.isMultilevel()) {
			// TODO:
			System.out.println("in multilevel");
		} else {
			
			JComboBox cb = new JComboBox(ctrl.getChoices());
			
			for (int i=0;i<ctrl.getChoices().length;i++){
				if(((ChoiceItem)ctrl.getChoices()[i]).getValue().equals(ctrl.getValue())){
					cb.setSelectedIndex(i);
				}
			}
			
			cb.setAlignmentX(Component.CENTER_ALIGNMENT);
			cb.setAlignmentY(Component.CENTER_ALIGNMENT);
			if (!rendererGuiConstants.getStretchSelectToMax())
				cb.setMinimumSize(cb.getPreferredSize());
			cb.setEditable(true);
			cb.addActionListener(this);
			res = cb;
		}
		setLookAndFeelFont(rendererGuiConstants.getSelectFont(), rendererGuiConstants.getSelectFontStyle(), rendererGuiConstants.getSelectFontSize(), res);
		res.setForeground(rendererGuiConstants.getSelectFontColor());
		res.putClientProperty(PERSONA_FORM_CONTROL, ctrl);
		if (label != null)
			res.putClientProperty(PERSONA_ASSOCIATED_LABEL, label);
		ctrlMap.put(ctrl, res);
		return res;		
		
	}

	private JComponent renderSelectControl(Select ctrl) {
		JLabel label = addControlLabel(ctrl);
		list=new JList(ctrl.getChoices());
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(rendererGuiConstants.getSelectVisibleRows());
        list.putClientProperty(PERSONA_FORM_CONTROL, ctrl);
        JScrollPane listScrollPane = new JScrollPane(list);
        if (label != null)
        	listScrollPane.putClientProperty(PERSONA_ASSOCIATED_LABEL, label);
		setLookAndFeelFont(rendererGuiConstants.getSelectFont(), rendererGuiConstants.getSelectFontStyle(), rendererGuiConstants.getSelectFontSize(), listScrollPane);
		listScrollPane.setForeground(rendererGuiConstants.getSelectFontColor());
		ctrlMap.put(ctrl, listScrollPane);
		return listScrollPane;		
	}

	private JButton renderSubmitControl(Submit ctrl) {
		Label cl = ctrl.getLabel();
		String labelStr = cl.getText();
		cl.setResourceLabel(labelStr.replace(" ", "<p>"));
		String iconURL = cl.getIconURL();
		String htmlvalue = isEmptyString(labelStr)? ""
				: "<html>" + "<FONT COLOR=#"+ Integer.toHexString(rendererGuiConstants.getSubmitFontColor().getRGB()).substring(2) +">" + ctrl.getLabel() + "</FONT>";
		
		if (rendererGuiConstants.getWrapSubmitText())
			htmlvalue = htmlvalue.replaceFirst("FONT COLOR", "FONT_COLOR").replace(" ", "<P>").replaceFirst("FONT_COLOR", "FONT COLOR");
		JButton submit = new JButton(htmlvalue);
		if (iconURL != null) {
			labelStr = ctrl.getHintString();
			submit.setIcon(
					(labelStr == null)? getIcon(iconURL, null) : getIcon(iconURL, labelStr));
		}
		submit.putClientProperty(PERSONA_FORM_CONTROL, ctrl);
		setLookAndFeelFont(rendererGuiConstants.getSubmitFont(), rendererGuiConstants.getSubmitFontStyle(), rendererGuiConstants.getSubmitFontSize(), submit);
		
		submit.setBorderPainted(false);
		submit.setFocusPainted(false);
		submit.setContentAreaFilled(false);
//		submit.setMargin(new Insets(10,20, 10, 20)); //don't do that to any button because there is not need. look the submit section on repaintIcons function
		submit.setHorizontalTextPosition(SwingConstants.CENTER);

		submit.setVerticalAlignment(SwingConstants.CENTER);
		submit.addActionListener(this);
		submit.setName(ctrl.getLabel().getText());
		if (iconURL == null){
			ImageIcon icon = getIcon(rendererGuiConstants.getSubmitIcon(), null);
			submit.setIcon(icon);
		}
		submit.putClientProperty("Stretched", new Integer(1));

		ctrlMap.put(ctrl, submit);
		return submit;
	}

	private ImageIcon scaleIcon(final Image image, final int width, final int height) {
        ImageIcon scaledIcon = new ImageIcon(image)  
        {    
            public void paintIcon(Component c, Graphics g, int x, int y)  
            {  
                g.drawImage(image, 0, 0, width, height, c);  
            }  
        };  
        return scaledIcon; 
	}

	/**
	 * Sets the font style on a component
	 * 
	 * @param fontName
	 * @param style
	 * @param size
	 * @param comp
	 */
	public void setLookAndFeelFont(String fontName, String style, int size,
			Component comp) {
		// Determine font style
		String[] styles = { "plain", "bold", "italic" };
		Font f = null;
		for (int i = 0; i < styles.length; i++) {
			if (styles[i].equalsIgnoreCase(style)) {
				switch (i) {
				case 0:
					f = new Font(fontName, Font.PLAIN, size);
				case 1:
					f = new Font(fontName, Font.BOLD, size);
				case 2:
					f = new Font(fontName, Font.ITALIC, size);
				}
				break;
			}
		}
		comp.setFont(f);
	}

	public Font getLookAndFeelFont(String fontName, String style, int size) {
		// Determine font style
		String[] styles = { "plain", "bold", "italic" };
		Font f = null;
		for (int i = 0; i < styles.length; i++) {
			if (styles[i].equalsIgnoreCase(style)) {
				switch (i) {
				case 0:
					f = new Font(fontName, Font.PLAIN, size);
				case 1:
					f = new Font(fontName, Font.BOLD, size);
				case 2:
					f = new Font(fontName, Font.ITALIC, size);
				}
				break;
			}
		}
		return f;
	}
	
	public void updateScreenResolution(int max_x, int max_y, int min_x,
			int min_y) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		if (max_x != -1) {
			if (max_x < dim.width)
				this.setSize(max_x, this.getHeight());
			else
				this.setSize(dim);
		} else if (max_y != -1) {
			if (max_y < dim.height)
				this.setSize(this.getWidth(), max_y);
			else
				this.setSize(dim);
		} else if (min_x != -1) {
			if (min_x > dim.width)
				this.setSize(dim.width, this.getHeight());
			else
				this.setSize(min_x, this.getHeight());
		} else if (min_y != -1) {
			if (min_y > dim.height)
				this.setSize(this.getWidth(), dim.height);
			else
				this.setSize(this.getWidth(), min_y);
		}
		this.getContentPane().paintAll(getContentPane().getGraphics());
		this.pack();
	}

    private void redrawContainer(Container container, int containerLevel, String mainContainerName) {
    	System.err.println("]] redrawing container " + container.getName() + " - mainContainerName = " + mainContainerName + " level = " + containerLevel);
    	//containerLevel++;
		for (int i=0;i<container.getComponentCount();i++)
		{
			if (
					(container.getComponent(i).getClass().getName().endsWith("Pane")) || 
					(container.getComponent(i).getClass().getName().endsWith("Panel")) || 
					(container.getComponent(i).getClass().getName().endsWith("Frame")) ||
					(container.getComponent(i).getClass().getName().endsWith("JViewport")) 
			)
			{
				mainContainer = mainContainerName;
				if ( 
						(container.getComponent(i).getName() != null) &&
						(
							(container.getComponent(i).getName().equals(rendererGuiConstants.getFooterPanelName())) ||
							(container.getComponent(i).getName().equals(rendererGuiConstants.getHeaderPanelName())) ||
							(container.getComponent(i).getName().equals(rendererGuiConstants.getSubmitsPanelName())) ||
							(container.getComponent(i).getName().equals(rendererGuiConstants.getMainMenuPanelName())) ||
							(container.getComponent(i).getName().equals(rendererGuiConstants.getMainPanelPanelName()))
						)
					)
				{
					mainContainer = container.getComponent(i).getName();
					containerLevel = 0;
				}
				redrawContainer(((Container)container.getComponent(i)), containerLevel+1, mainContainer);
			}
		}
		if (container instanceof JScrollPane)
			//redrawFinalContainer((Container)((JScrollPane)container).getViewport(), containerLevel, mainContainerName);
			System.out.println("scrollpane");
		else
			if ((container instanceof JViewport) && (container.getComponentCount() > 0))
			{
				redrawComponent(-1, container, container.getComponent(0), containerLevel, 0, 0, 0, container.getName(), mainContainerName);
			}
			else
				if (mainContainerName.equals(rendererGuiConstants.getMainMenuPanelName()))
					redrawMainMenu(container, containerLevel, mainContainerName);
				else
					redrawFinalContainer(container, containerLevel, mainContainerName);
	}

    private double[][] getLayoutSize(int appropriatePanelsPerRow, int appropriatePanelsPerColumn){
		double border = rendererGuiConstants.getComponentsBorder();
		double preferred = TableLayout.PREFERRED;
		double fill = TableLayout.FILL;
		
		double columns[] = new double[appropriatePanelsPerRow*2];
		double rows[] = new double[appropriatePanelsPerColumn*2];
		
		if (appropriatePanelsPerRow == 1)
		{
			if (rendererGuiConstants.keepWideLastControlsColumn())
				columns[0] = fill;
			else
				columns[0] = preferred;
			columns[1] = border;
		}
		else			
			for (int col=0;col<appropriatePanelsPerRow*2;col++){
				if (col == ((appropriatePanelsPerRow*2)-2)) //last column of controls
					if (rendererGuiConstants.keepWideLastControlsColumn())
						columns[col] = fill;
					else
						columns[col] = preferred;
				else
					columns[col] = preferred;
				columns[++col] = border;
			}
//		columns[columns.length-1] = fill;

		for (int row=0;row<Double.valueOf(appropriatePanelsPerColumn).intValue()*2;row++){
			rows[row] = preferred;
			rows[++row] = border;
		}
//		rows[rows.length-1] = fill;
       
		double size[][] = {columns, rows};
		return size;
    }
   
    private void redrawComponent(double viewPortWidth, Container container, Component component, int containerLevel, int row, int col, int componentIndex, String containerName, String mainContainerName){		
		if (component instanceof JButton)
		{
			JButton button = (JButton)component;
			if ((button.getText() != null) && (!button.getText().isEmpty()))
			{
				String FontName = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".submitFont", rendererGuiConstants.getSubmitFont());
				String FontStyle = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".submitFontStyle", rendererGuiConstants.getSubmitFontStyle());
				int FontSize = Integer.valueOf(rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".submitFontSize", Integer.valueOf(rendererGuiConstants.getSubmitFontSize()).toString())).intValue();
				setLookAndFeelFont(FontName, FontStyle, FontSize, component);

				String oldFontColor = button.getText().substring(button.getText().indexOf("#")+1, button.getText().indexOf("#")+7);
				String submitFontColorStr = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".submitFontColor", null);
				String newFontColor = (submitFontColorStr == null)?Integer.toHexString(rendererGuiConstants.getSubmitFontColor().getRGB()).substring(2):submitFontColorStr;
				
				button.setText(button.getText().replace(oldFontColor, newFontColor));
				
				String wrapSubmitsText = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".wrapSubmitText", "");
				if ((!wrapSubmitsText.isEmpty()) && (wrapSubmitsText.equalsIgnoreCase("no")))
				{
					button.setText(button.getText().replace("<P>"," " ));
					button.putClientProperty("verticallyStretchedToText", new Integer(1));
				}
			}
			component = button;
		}
		else
			if (component instanceof JTextField)	
			{
				String FontName = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".editFont", rendererGuiConstants.getEditFont());
				String FontStyle = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".editFontStyle", rendererGuiConstants.getEditFontStyle());
				int fontSize = ((JTextField)component).isEditable()?Integer.valueOf(rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".editFontSizeEditable", Integer.valueOf(rendererGuiConstants.getEditFontSizeEditable()).toString())).intValue():Integer.valueOf(rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".editFontSizeReadOnly", Integer.valueOf(rendererGuiConstants.getEditFontSizeReadOnly()).toString())).intValue();
				setLookAndFeelFont(FontName, FontStyle, fontSize, component);
				
				String fontColorStr = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".editFontColor", null);
				Color fontColor = (fontColorStr == null)?rendererGuiConstants.getEditFontColor():new Color(Integer.decode("0x"+fontColorStr));
				component.setForeground(fontColor);
			}
			else
				if ((component instanceof JTextArea) || (component instanceof JTextPane))	
				{
					String FontName = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".textAreaFont", rendererGuiConstants.getTextAreaFont());
					String FontStyle = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".textAreaFontStyle", rendererGuiConstants.getTextAreaFontStyle());
					int fontSize = Integer.valueOf(rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".textAreaFontSize", Integer.valueOf(rendererGuiConstants.getTextAreaFontSize()).toString())).intValue();
					setLookAndFeelFont(FontName, FontStyle, fontSize, component);
					
					String fontColorStr = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".textAreaFontColor", null);
					Color fontColor = (fontColorStr == null)?rendererGuiConstants.getTextAreaFontColor():new Color(Integer.decode("0x"+fontColorStr));
					component.setForeground(fontColor);
					
				}		
				else
					if (component instanceof JSlider)	
					{
						String FontName = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".sliderFont", rendererGuiConstants.getSliderFont());
						String FontStyle = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".sliderFontStyle", rendererGuiConstants.getSliderFontStyle());
						int fontSize = Integer.valueOf(rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".sliderFontSize", Integer.valueOf(rendererGuiConstants.getSliderFontSize()).toString())).intValue();
						setLookAndFeelFont(FontName, FontStyle, fontSize, component);
						
						String fontColorStr = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".sliderFontColor", null);
						Color fontColor = (fontColorStr == null)?rendererGuiConstants.getSliderFontColor():new Color(Integer.decode("0x"+fontColorStr));
						component.setForeground(fontColor);
					}	
					else
						if ((component instanceof JComboBox) || (component instanceof JList))	
						{
							String FontName = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".selectFont", rendererGuiConstants.getSelectFont());
							String FontStyle = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".selectFontStyle", rendererGuiConstants.getSelectFontStyle());
							int fontSize = Integer.valueOf(rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".selectFontSize", Integer.valueOf(rendererGuiConstants.getSelectFontSize()).toString())).intValue();
							setLookAndFeelFont(FontName, FontStyle, fontSize, component);
							
							String fontColorStr = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".selectFontColor", null);
							Color fontColor = (fontColorStr == null)?rendererGuiConstants.getSelectFontColor():new Color(Integer.decode("0x"+fontColorStr));
							String backgroundColorStr = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".backgroundColorStr", null);
							Color backgroundColor = (backgroundColorStr == null)?rendererGuiConstants.getSelectBackgroundColor():new Color(Integer.decode("0x"+backgroundColorStr));
							
							component.setBackground(backgroundColor);
							component.setForeground(fontColor);
							
						  
						    ComboBoxEditor editor = ((JComboBox)component).getEditor();
						    if (editor != null) {
						        Component editorComp = editor.getEditorComponent();
						        if (editorComp instanceof JComponent) {
						            JComponent editorJComp = (JComponent) editorComp;
						            editorJComp.setForeground(fontColor);
						        }
						    }
							
							if (rendererGuiConstants.getStretchSelectToMax() && (viewPortWidth > 0))
								component.setPreferredSize(new Dimension(Double.valueOf(viewPortWidth - component.getLocation().getX()).intValue() - 10, Double.valueOf(component.getPreferredSize().getHeight()).intValue()));
//							else //it doesn't work here, so I put it in the renderSelect function.
//								component.setPreferredSize(component.getMinimumSize());
						}	
						else
							{
								//there are used label font contstants as general font constants. In case they must be different, new general font constants must be added and used there
								String FontName = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".labelFontName", rendererGuiConstants.getLabelFont());
								String FontStyle = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".labelFontStyle", rendererGuiConstants.getLabelFontStyle());
								int fontSize = Integer.valueOf(rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".labelFontSize", Integer.valueOf(rendererGuiConstants.getLabelFontSize()).toString())).intValue();
								setLookAndFeelFont(FontName, FontStyle, fontSize, component);
								
								String fontColorStr = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".labelFontColor", null);
								Color fontColor = (fontColorStr == null)?rendererGuiConstants.getLabelFontColor():new Color(Integer.decode("0x"+fontColorStr));
								component.setForeground(fontColor);
							}		
    }
    
	private void redrawFinalContainer(Container container, int containerLevel, String mainContainerName) {	
		Component[] containerComponents = container.getComponents();
		String logString = new String("");
		logString += "redrawFinalContainer Container name: " + container.getName();
		logString += " - mainContainer : " + mainContainerName;
		logString += " - Container level: " + containerLevel;
		logString += " - Container childs: " + containerComponents.length; 
		
		String backgroundColorCode = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".backgroundColor", null);
		Color backgroundColor = ((backgroundColorCode!=null)?new Color(Integer.decode("0x"+backgroundColorCode)):rendererGuiConstants.getDefaultBackgroundColor()) ;	
		container.setBackground(backgroundColor);		
					
		String colsStr = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".columns", null);
		String rowsStr = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".rows", Integer.valueOf(rendererGuiConstants.getDefaultRows()).toString());
		
		int appropriatePanelsPerRow = 0;
		if (rowsStr.equals("1"))
			appropriatePanelsPerRow = containerComponents.length;
		else
			if (colsStr != null)
				appropriatePanelsPerRow = Integer.valueOf(colsStr);
			else
				if (container.getName() != null) //if there is not special property set for this container, look for general purpose constants in renderGuiConstants
					if (container.getName().equals(rendererGuiConstants.getMainMenuPanelName()))
						appropriatePanelsPerRow = rendererGuiConstants.getDefaultMenuCols(); 
					else  //redraw only mainPanel or its childs
						if ( (container.getName().equals(rendererGuiConstants.getMainPanelPanelName())) ||
							 (mainContainerName.equals(rendererGuiConstants.getMainPanelPanelName()))
							)
							if (containerComponents.length > 1)
								appropriatePanelsPerRow = rendererGuiConstants.getDefaultCols();
							else
								appropriatePanelsPerRow = 1;
						else;
				else
					if (mainContainerName.equals(rendererGuiConstants.getMainPanelPanelName()))
						appropriatePanelsPerRow = rendererGuiConstants.getDefaultCols();	
		
		logString += " - cols = " + appropriatePanelsPerRow;

		if ((containerComponents.length > 0) &&(appropriatePanelsPerRow > 0)) //this excepts JScrollBar which needs JScrollBarLayout, and possibly other empty containers
		{
			int appropriatePanelsPerColumn = (int)Math.ceil((double)containerComponents.length/(double)appropriatePanelsPerRow);
			container.removeAll();
			
			TableLayout layout = new TableLayout(getLayoutSize(appropriatePanelsPerRow, appropriatePanelsPerColumn));
			
			container.setLayout (layout);				
			int componentIndex = 0;
			
			logString += " - Container components redrawn: ";
			logString += " - cols X rows = " + appropriatePanelsPerRow + " X " + appropriatePanelsPerColumn;
			
			Container parent = container.getParent();
			double viewPortWidth = -1;
			if (parent != null) //go back to find the main viewport of one of four basic screen panels. Needed to know the visible width of the main panel
				while (!((parent instanceof JViewport) || (parent instanceof JFrame)))
				{
					parent = parent.getParent();
				}
				if ((parent instanceof JViewport) &&
					(((JViewport)parent).getParent().getName() != null) &&	
					( //is one of four basic (top, right, bottom, center menu / center application) containers
						(((JViewport)parent).getParent().getName().equals(rendererGuiConstants.getFooterPanelName())) ||
						(((JViewport)parent).getParent().getName().equals(rendererGuiConstants.getHeaderPanelName())) ||
						(((JViewport)parent).getParent().getName().equals(rendererGuiConstants.getSubmitsPanelName())) ||
						(((JViewport)parent).getParent().getName().equals(rendererGuiConstants.getMainMenuPanelName())) ||
						(((JViewport)parent).getParent().getName().equals(rendererGuiConstants.getMainPanelPanelName()))
					)
				)
				viewPortWidth = ((JViewport)parent).getViewRect().getWidth();
			
			
			for (int rowIndex=0;rowIndex<appropriatePanelsPerColumn;rowIndex++){
				for (int colIndex=0;colIndex<appropriatePanelsPerRow;colIndex++){
					if (componentIndex < containerComponents.length)
					{
						Component component = containerComponents[componentIndex];
				    	String HAlign = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".HAlign", rendererGuiConstants.getDefaultHAlign());
				    	String VAlign = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".VAlign", rendererGuiConstants.getDefaultVAlign());
						
						redrawComponent(viewPortWidth, container, component, containerLevel, rowIndex, colIndex, componentIndex, container.getName(), mainContainerName);
						container.add(component, colIndex*2 + ", " + rowIndex*2 + ", " + HAlign + ", " + VAlign );
						
					}
					componentIndex++;
				}
			}
		}
		System.out.println(logString);		
	}

	private void redrawMainMenu(Container container, int containerLevel, String mainContainerName) {
		Component[] containerComponents = container.getComponents();	
		
		String logString = new String("");
		logString += "redrawMainMenu Container name: " + container.getName();
		logString += " - Container level: " + containerLevel;
		logString += " - Container childs: " + containerComponents.length; 		
		
		String backgroundColorCode = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".backgroundColor", null);
		Color backgroundColor = ((backgroundColorCode!=null)?new Color(Integer.decode("0x"+backgroundColorCode)):rendererGuiConstants.getDefaultBackgroundColor()) ;	
		container.setBackground(backgroundColor);
				
		String colsObj = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".columns", null);
		String rowsStr = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".rows", Integer.valueOf(rendererGuiConstants.getDefaultRows()).toString());
		
		int appropriatePanelsPerRow = 0;
		if (rowsStr.equals("1"))
			appropriatePanelsPerRow = containerComponents.length;
		else		
			if (colsObj != null)
				appropriatePanelsPerRow = Integer.valueOf(colsObj);
			else
				if ((container.getName() != null) && (container.getName().equals(rendererGuiConstants.getMainMenuPanelName())))
					appropriatePanelsPerRow = rendererGuiConstants.getDefaultMenuCols();		
				else
					if ((container.getName() != null) && (container.getName().equals(rendererGuiConstants.getSearchBoxLabel())))
						appropriatePanelsPerRow = rendererGuiConstants.getDefaultCols();		
		
		if ((containerComponents.length > 0) &&(appropriatePanelsPerRow > 0)) //this excepts JScrollBar which needs JScrollBarLayout, and possibly other empty containers
		{
			int appropriatePanelsPerColumn = (int)Math.ceil((double)containerComponents.length/(double)appropriatePanelsPerRow);
			//we suppose that in the main menu there is one component (the search panel) which must be managed not in the same way that other components (buttons)
			//as it is shown in the screen, it's better the search component to be placed on the right of the buttons, so one more column is added, plus the border column.
			appropriatePanelsPerRow++;
			
			container.removeAll();
			
			TableLayout layout = new TableLayout(getLayoutSize(appropriatePanelsPerRow, appropriatePanelsPerColumn));
			container.setLayout(layout);				
			int componentIndex = 0;
		
			logString += " - Container components redrawn: ";
			logString += " - cols X rows = " + appropriatePanelsPerRow + " X " + appropriatePanelsPerColumn;
			
			Container parent = container.getParent();
			double viewPortWidth = -1;
			
			while (!((parent instanceof JViewport) || (parent instanceof JFrame)))
			{
				parent = parent.getParent();
			}
			if (parent instanceof JViewport)
				viewPortWidth = ((JViewport)parent).getViewRect().getWidth();
			
			for (int rowIndex=0;rowIndex<appropriatePanelsPerColumn;rowIndex++){
				for (int colIndex=0;colIndex<appropriatePanelsPerRow-1;colIndex++){
					
					if (componentIndex < containerComponents.length)
					{
				    	String HAlign = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".HAlign", rendererGuiConstants.getDefaultHAlign());
				    	String VAlign = rendererGuiConstants.getSpecialProperty(mainContainerName + ".Level" + containerLevel + ".VAlign", rendererGuiConstants.getDefaultVAlign());

						Component component = containerComponents[componentIndex];
						
						redrawComponent(viewPortWidth, container, component, containerLevel, rowIndex, colIndex, componentIndex, container.getName(), mainContainerName);

						if ( 	(containerComponents[componentIndex] instanceof JPanel) && 
								(
									(((JPanel)containerComponents[componentIndex]).getName() != null) &&
									(((JPanel)containerComponents[componentIndex]).getName().equals(rendererGuiConstants.getSearchBoxLabel()))
								)
							)
						{
							containerComponents[componentIndex].setSize(containerComponents[componentIndex].getWidth()+10, containerComponents[componentIndex].getHeight()+10);
							containerComponents[componentIndex].setPreferredSize(new Dimension(containerComponents[componentIndex].getWidth()+10, containerComponents[componentIndex].getHeight()+10));
							container.add(containerComponents[componentIndex], (appropriatePanelsPerRow-1)*2 + ", " + 0 + ", " + (appropriatePanelsPerRow-1)*2 + ", " + /*(appropriatePanelsPerColumn-1)*2*/2 + ", " + HAlign+ ", " + VAlign);
//							System.err.println(">> " + containerComponents[componentIndex].getClass().getName() + " : " + colIndex*2 + ", " + rowIndex*2 + ", " + HAlign+ ", " + VAlign);
						}
						else
						{
							container.add(containerComponents[componentIndex], colIndex*2 + ", " + rowIndex*2 + ", " + HAlign+ ", " + VAlign); 
//							System.err.println(">> " + containerComponents[componentIndex].getClass().getName() + " : " + colIndex*2 + ", " + rowIndex*2 + ", " + HAlign+ ", " + VAlign);
						}
					}
					componentIndex++;
				}
			}			
		}
		System.out.println(logString);
	}

	public Dimension getPreferredScrollableViewportSize() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean getScrollableTracksViewportHeight() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void stateChanged(ChangeEvent e) {
		Object varSource = e.getSource();
		if(varSource instanceof JSlider){
			JSlider source=(JSlider)e.getSource();
		if (source.getValueIsAdjusting()) {
            int selectedValue = (Integer)source.getValue();
            setSliderValue(selectedValue);
		}
		}else if(varSource instanceof SpinnerNumberModel){
			// SpinnerModel numberModel=(SpinnerModel)e.getSource();
			// int selValue=(Integer)numberModel.getValue();
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) 
		{
            if (list.getSelectedIndex() == -1) 
            {
                setListValue("");
            } 
            else 
            {
            	if(list.getSelectedValue() instanceof ChoiceItem)
            	{
	            	Object userSelection=list.getSelectedValue();
	            	ChoiceItem value=(ChoiceItem)userSelection;
	            	if(value.getValue() instanceof String)
	            	{
	            		setListValue((String)value.getValue());
	            	}
	            	else 
	            		if(value.getValue() instanceof Integer)
	            		{
		            		int intValue= (Integer)value.getValue();
		            		setListValue(""+intValue);	
	            		}
            	}
            }
		}
	}	
	
	
	/**
	 * 
	 * @param panel
	 * @param componentsHorizontalBorder
	 * @param componentsVerticalBorder
	 * @return appropriateWidth, appropriateHeight, appropriateCols, appropriateRows
	 * Checks all the possible compositions in order to show x components on a panel in a way that the shape of all panels will be analog to the shape of the container panel
	 * E.g. if we have to show 10 components, it checks which from the compositions 2x5, 3x4, 4x3, 5x2 creates a shape having a width/height ratio, close to that of the container panel.
	 * Taking into consideration that the width of each column equals the max(width) of its components and the height of each row equals the max(height) of its components, there is computed the total panel width and height for each of the above compositions
	 */
//	private double[] getAppropriateContainer(Container container, double componentsHorizontalBorder, double componentsVerticalBorder){
//		double returnValues[] = new double[4];
//		Component[] components = container.getComponents();
//		int compositions = components.length/2;
//		compositions = ((compositions*2) < components.length)?compositions:compositions--; //if 10 components, (10/2)-1 compositions. if 11 components, 11/2 compositions
//		compositions++;
//		double appropriateWidth = 0;
//		double appropriateHeight = 0;
//		double appropriateRatio = 0;
//		double viewRectWidth = 0;
//		double viewRectHeight = 0;
//		double panelDimRatio = 0;
//		
//		Component parent = container.getParent();
//		while (!((parent instanceof JViewport) || (parent instanceof JFrame)))
//		{
//			parent = parent.getParent();
//		}
//		if (parent instanceof JViewport)
//			panelDimRatio = ((JViewport)parent).getViewRect().getWidth() / ((JViewport)parent).getViewRect().getHeight();
//			//panelDimRatio = ((JViewport)panel.getParent()).getViewRect().getWidth() / ((JViewport)panel.getParent()).getViewRect().getHeight();
//		int appropriateCols = 0;
//		int appropriateRows = 0;
//		
//		for (int colCount=2;colCount<=compositions;colCount++) //all available compositions : from 2 to components/2 or components/2 + 1 
//		{
//			double compositionWidth = 0;
//			double compositionHeight = 0;
//			double compositionDimRatio = 0;
//			
//			for (int compCol=0;compCol<colCount;compCol++) //max width of all columns e.g. maxWidth(01,03,05,07,09) + maxWidth (02,04,06,08,10)
//			{
//				double maxComponentWidth = 0;
//				for (int compRowCol=compCol; compRowCol<components.length; compRowCol += colCount) //max width of current column. e.g. maxWidth(01,03,05,07,09)
//				{
//					maxComponentWidth = (maxComponentWidth > components[compRowCol].getSize().getWidth())?maxComponentWidth:components[compRowCol].getSize().getWidth();
//				}
//				compositionWidth += maxComponentWidth + componentsHorizontalBorder;
//			}
//			
//			int rowCount = components.length / colCount;
//			rowCount = ((rowCount * colCount) < components.length)?rowCount++:rowCount;
//			
//			for (int compRow=0;compRow<rowCount;compRow++) //max height of all rows e.g. maxHeight(01,02) + maxHeight(03,04) + maxHeight(05,06) + maxHeight(07,08) + maxHeight(09,10)
//			{
//				double maxComponentHeight = 0;
//				for (int compColRow=compRow*colCount; compColRow<colCount*(compRow+1); compColRow++)
//				{
//					maxComponentHeight = (maxComponentHeight > components[compColRow].getSize().getHeight())?maxComponentHeight:components[compColRow].getSize().getHeight();
//				}
//				compositionHeight += maxComponentHeight + componentsVerticalBorder;
//			}
//			compositionDimRatio = compositionWidth/compositionHeight;
//			if (Math.abs(panelDimRatio - appropriateRatio) > Math.abs(panelDimRatio - (compositionDimRatio)))
//			{
//				appropriateRatio = compositionWidth/compositionHeight;
//				appropriateWidth = compositionWidth;
//				appropriateHeight = compositionHeight;
//				appropriateCols = colCount;
//				appropriateRows = rowCount;
//			}
//			
//		}		
//		returnValues[0] = appropriateWidth;
//		returnValues[1] = appropriateHeight;
//		returnValues[2] = Integer.valueOf(appropriateCols).doubleValue();
//		returnValues[3] = Integer.valueOf(appropriateRows).doubleValue();
//		return returnValues;
//	}	

}
