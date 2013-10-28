/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.gui.swing.bluesteelLAF;

import java.awt.Color;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.auth.LoginService;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.container.utils.Messages;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.ontology.language.Language;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.ui.preferences.ColorType;
import org.universAAL.ontology.ui.preferences.GeneralInteractionPreferences;
import org.universAAL.ontology.ui.preferences.GenericFontFamily;
import org.universAAL.ontology.ui.preferences.Intensity;
import org.universAAL.ontology.ui.preferences.Size;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ontology.ui.preferences.VisualPreferences;
import org.universAAL.ontology.ui.preferences.WindowLayoutType;
import org.universAAL.ui.gui.swing.bluesteelLAF.support.UAALTray;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.InitInterface;

/**
 * the initialization class.
 * @author amedrano
 */
public class Init implements InitInterface {

    public static final String CONF_PREFIX = "ui.handler.gui.swing.bluesteelLAF.";
	private static final String TOOLTIP_ACTIVE = CONF_PREFIX + "tootip.active";
	private static final String TOOLTIP_DELAY = CONF_PREFIX + "tootip.delay";
	static final String WINDOWED = CONF_PREFIX + "windowed";
	static final String WINDOWED_WIDTH = WINDOWED + ".width";
	static final String WINDOWED_HEIGHT = WINDOWED + ".height";
	static final String WINDOWED_X = WINDOWED + ".x";
	static final String WINDOWED_Y = WINDOWED + ".y";

	private ColorLAF color;
    private UAALTray tray;
    private JDesktopPane desktop;
    private JFrame frame;
    private Renderer render;
	private boolean windowed;
	private Messages messages;

	/** {@inheritDoc} */
    public void install(Renderer render) {
    	this.render = render;
    	color = new ColorLAF();
    	windowed = Boolean.parseBoolean(render.getProperty(WINDOWED, "false"));
        MetalLookAndFeel.setCurrentTheme(color);
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
        	LogUtils.logWarn(render.getModuleContext(), 
        			getClass(), "install",
        			new String[] {"unable to set MetalLookAndFeel."}, e);
        }
        try {
			tray = new UAALTray(render);
		} catch (Exception e) {
        	LogUtils.logWarn(render.getModuleContext(), 
        			getClass(), "install",
        			new String[] {"unable to start Tray Icon."}, e);
		}
        try {
			createDesktop();
		} catch (Exception e1) {
        	LogUtils.logWarn(render.getModuleContext(), 
        			getClass(), "install",
        			new String[] {"unable to start the desktop."}, e1);
		}
        UIManager.put("ToolTip.background", ColorLAF.getOverSytem());
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(Color.BLACK, 2));
        UIManager.put("ToolTip.font", color.getLabelFont());
        ToolTipManager.sharedInstance()
        	.setInitialDelay(Integer.parseInt(render.getProperty(TOOLTIP_DELAY, "500")));
        ToolTipManager.sharedInstance()
    		.setEnabled(Boolean.parseBoolean(render.getProperty(TOOLTIP_ACTIVE, "true")));
        URL propertiesURL = getClass().getClassLoader().getResource("internationalization/messages.properties");
        try {
			messages = new Messages(propertiesURL);
		} catch (Exception e) {
			LogUtils.logError(render.getModuleContext(), getClass(),
					"install", new String[]{"unable to load internationalization Messages."}, e);
		} 
    }
    
    public ColorLAF getColorLAF(){
    	return color;
    }

	public void uninstall() {
		if (tray != null)
			tray.dispose();
	    if (desktop != null)
	    	desktop.setVisible(false);
	    if (frame != null)
	    frame.dispose();
	}

	public void userLogIn(User usr) {
		if (!frame.isVisible()) {
			frame.setVisible(true);
		}
		if (tray != null) {
			tray.update();
		}
	}
	
	public void userLogOff(User usr) {
		if (tray != null)
			tray.update();
		if (desktop != null)
			desktop.removeAll();
		if (frame != null)
			frame.setVisible(false);		
	}

	public void showLoginScreen() {
		if (!frame.isVisible()) {
			frame.setVisible(true);
		}
		JXLoginPane lp = new JXLoginPane(new RendererLoginService());
		JXLoginPane.showLoginDialog(frame, lp);
	}
	
	public JDesktopPane getDesktop() {
	    return desktop;
	}

	private void createDesktop() {
	    frame = new JFrame();
	    desktop = new JDesktopPane();
	    desktop.setVisible(true);
	    frame.setContentPane(desktop);
	    if (!windowed){
	    	desktop.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	    	frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	    	frame.setUndecorated(true);
	    } else {
	    	frame.setResizable(true);
	    	frame.setBounds(
	    			Integer.valueOf(render.getProperty(WINDOWED_X, "0")),
	    			Integer.valueOf(render.getProperty(WINDOWED_Y, "0")),
	    			Integer.valueOf(render.getProperty(WINDOWED_WIDTH, "800")),
	    			Integer.valueOf(render.getProperty(WINDOWED_HEIGHT, "600")));
	    }
	    frame.setVisible(true);
	}

	public static Init getInstance(Renderer render){
	    return (Init) render.getInitLAF();
	}

	
	private class RendererLoginService extends LoginService {

		@Override
		public boolean authenticate(String arg0, char[] arg1, String arg2)
				throws Exception {
			return render.authenticate(arg0, new String(arg1));
		}
		
	}

	private boolean tryLoadingMessages(Locale l){
		URL resource = getClass().getClassLoader().getResource("internationalization/messages.properties");
		try {
			messages = new Messages(resource, l);
			return messages.getCurrentLocale().equals(l);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param currentDialog
	 */
	public void processPrefs(UIRequest currentDialog) {
		VisualPreferences vp = (VisualPreferences) currentDialog
				.getProperty(UIPreferencesSubProfile.PROP_VISUAL_PREFERENCES);
		
		GeneralInteractionPreferences gip = (GeneralInteractionPreferences) currentDialog
				.getProperty(UIPreferencesSubProfile.PROP_INTERACTION_PREFERENCES);
		
		if (gip != null){
			Language preferred  = gip.getPreferredLanguage();
			if (preferred != null
					&& !tryLoadingMessages(new Locale(preferred.getIso639code()))){
				Language secondary = gip.getSecondaryLanguage();
				if (secondary != null){
					tryLoadingMessages(new Locale(secondary.getIso639code()));
				} else {
					tryLoadingMessages(Locale.ENGLISH);
				}
			}
		}

		if (vp != null){
//			ColorType bgColor = vp.getBackgroundColor();
			ColorType fColor = vp.getFontColor();
			Size fSize = vp.getFontSize();
			GenericFontFamily fFamily = vp.getFontFamily();
			WindowLayoutType wl = vp.getWindowLayout();
			Intensity spacing = vp.getComponentSpacing();
/*			if (bgColor != null){
				switch (bgColor.ord()) {
				case ColorType.WHITE:
				case ColorType.BLACK:
				case ColorType.LIGHT_GRAY:
				case ColorType.DARK_GREY:
					
					break;
				case ColorType.LIGHT_BLUE:
				case ColorType.DARK_BLUE:
				case ColorType.CYAN:
					
					break;
				case ColorType.LIGHT_GREEN:
				case ColorType.DARK_GREEN:
					
					break;
				case ColorType.LIGHT_RED:
				case ColorType.DARK_RED:
				case ColorType.MAGENTA:
				case ColorType.PURPLE:
				case ColorType.PINK:
					
					break;
				case ColorType.YELLOW:
				case ColorType.ORANGE:
				
					break;

				default:
					break;
				}
			}*/
			if (fColor != null){
				switch (fColor.ord()) {
				case ColorType.WHITE:
					color.setFontColor(Color.WHITE);
					break;
				case ColorType.BLACK:
					color.setFontColor(Color.BLACK);
					break;
				case ColorType.LIGHT_GRAY:
					color.setFontColor(Color.LIGHT_GRAY);
					break;
				case ColorType.DARK_GREY:
					color.setFontColor(Color.DARK_GRAY);
					break;
				case ColorType.LIGHT_BLUE:
					color.setFontColor(new Color(0xADD8E6));
					break;
				case ColorType.DARK_BLUE:
					color.setFontColor(new Color(0x0000A0));
					break;
				case ColorType.LIGHT_GREEN:
					color.setFontColor(new Color(0x90EE90));
					break;
				case ColorType.DARK_GREEN:
					color.setFontColor(new Color(0x254117));
					break;
				case ColorType.LIGHT_RED:
					color.setFontColor(new Color(0x0000A0));
					break;
				case ColorType.DARK_RED:
					color.setFontColor(new Color(0x8B0000));
					break;
				case ColorType.ORANGE:
					color.setFontColor(Color.ORANGE);
					break;
				case ColorType.YELLOW:
					color.setFontColor(Color.YELLOW);
					break;
				case ColorType.CYAN:
					color.setFontColor(Color.CYAN);
					break;
				case ColorType.PURPLE:
					color.setFontColor(new Color(0x800080));
					break;
				case ColorType.MAGENTA:
					color.setFontColor(Color.MAGENTA);
					break;
				case ColorType.PINK:
					color.setFontColor(Color.PINK);
					break;
				default:
					break;
				}
			}
			if (fFamily != null){
				switch (fFamily.ord()) {
				case GenericFontFamily.SERIF:
					color.setFontFamily("Serif");
					break;
				case GenericFontFamily.SANS_SERIF:
					color.setFontFamily("SansSerif");
					break;
				case GenericFontFamily.CURSIVE:
					color.setFontFamily("Monotype Corsiva");
					break;
				case GenericFontFamily.FANTASY:
					color.setFontFamily("Old English Text MT");
					break;
				case GenericFontFamily.MONOSPACE:
					color.setFontFamily("Monospaced");
					break;

				default:
					break;
				}
			}
			if (fSize != null){
				switch (fSize.ord()) {
				case Size.LARGE:
					color.setFontSizeBase(30);
					break;
				case Size.MEDIUM:
					color.setFontSizeBase(20);
					break;
				case Size.SMALL:
					color.setFontSizeBase(12);
					break;
				default:
					break;
				}
			}
			if (wl != null){
				switch (wl.ord()) {
				case WindowLayoutType.OVERLAP:
					windowed = true;
					break;
				case WindowLayoutType.TILED:
					windowed = false;
					break;
				default:
					break;
				}
			}
			if (spacing != null){
				switch (spacing.ord()) {
				case Intensity.LOW:
					color.setGap(5);
					break;
				case Intensity.MEDIUM:
					color.setGap(10);					
					break;
				case Intensity.HIGH:
					color.setGap(20);					
					break;

				default:
					break;
				}
			}
		}
	}

	/**
	 * get the Internationalized message corresponding to the provided Key
	 * @param key the Key of the message.
	 * @return the string.
	 */
	public String getMessage(String key) {
		return messages.getString(key);
	}
	
	/**
	 * Tells whether the frame should be full screen or windowed.
	 * @return
	 */
	public boolean isWindowed(){
		return windowed;
	}
}
