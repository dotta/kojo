/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.kojo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledDocument;
import net.kogics.kojo.codingmode.SwitchMode;
import org.netbeans.api.options.OptionsDisplayer;
import org.openide.awt.UndoRedo.Manager;
import org.openide.util.NbBundle;
import org.openide.windows.CloneableOpenSupport;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.editor.EditorUI;
import org.netbeans.modules.editor.NbEditorDocument;
import org.openide.ErrorManager;
import org.openide.awt.Actions;
import org.openide.awt.Mnemonics;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.text.CloneableEditor;
import org.openide.text.CloneableEditorSupport;
import org.openide.util.Utilities;
import org.openide.util.actions.BooleanStateAction;
import org.openide.util.actions.Presenter;
import org.openide.util.actions.SystemAction;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//net.kogics.kojo//CodeEditor2//EN",
autostore = false)
public final class CodeEditorTopComponent extends CloneableEditor {

    private static CodeEditorTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "images/code-editor.png";
    private static final String PREFERRED_ID = "CodeEditorTopComponent";
    private JPopupMenu popupMenu;
    private CodeExecutionSupport codeExecSupport;
    // Cut/Copy/Paste
    final Action copyAction = new DefaultEditorKit.CopyAction();
    final Action cutAction = new DefaultEditorKit.CutAction();
    final Action pasteAction = new DefaultEditorKit.PasteAction();
    private String lastLoadStoreDir = "";
    private String codexEmail = "";
    private String codexPassword = "";

    public CodeEditorTopComponent() {
        super(new KojoEditorSupport(new KojoEnv()));
        ((KojoEditorSupport) cloneableEditorSupport()).setTc(this);

        initComponents();

        cutAction.setEnabled(false);
        copyAction.setEnabled(false);
        pasteAction.setEnabled(true);

        ActionMap actionMap = getActionMap();
        actionMap.put(DefaultEditorKit.copyAction, copyAction);
        actionMap.put(DefaultEditorKit.cutAction, cutAction);
        actionMap.put(DefaultEditorKit.pasteAction, pasteAction);

        setName(NbBundle.getMessage(CodeEditorTopComponent.class, "CTL_CodeEditorTopComponent"));
        setToolTipText(NbBundle.getMessage(CodeEditorTopComponent.class, "HINT_CodeEditorTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);

        // The following code should have been good to provide Scala support 
        // within the script editor.
        // This code works great - except that we (sometimes) get exceptions related to 
        // GsfHints and the File Object being null
        // The exceptions seem related to the code pane getting focus too early on startup!
        // Intstead of trying to chase that down, I've gone with the code that's worked
        // well over most of Kojo's life.

//        JEditorPane pane = new JEditorPane();
//        EditorKit ek = org.openide.text.CloneableEditorSupport.getEditorKit("text/x-scala");
//        pane.setEditorKit(ek);
//        pane.getDocument().putProperty(org.netbeans.api.lexer.Language.class, org.netbeans.modules.scala.core.lexer.ScalaTokenId.language());
//
//        MouseListener[] ml = pane.getMouseListeners();
//        for (int i = 0; i < ml.length; i++) {
//            MouseListener mouseListener = ml[i];
//            if (mouseListener instanceof EditorUI) {
//                // remove default popop handler
//                pane.removeMouseListener(mouseListener);
//            }
//        }
//
//        pane.getDocument().addUndoableEditListener(undoRedoManager);
//
//        EditorUI editorUI = org.netbeans.editor.Utilities.getEditorUI(pane);
//        JPanel panel = (JPanel) editorUI.getExtComponent();
//
//        onCodePaneAvailable(pane, panel);
//        panel.add(getToolbar(), BorderLayout.NORTH);
//
//        setLayout(new BorderLayout());
//        add(panel, BorderLayout.CENTER);

    }

    public String getLastLoadStoreDir() {
        return lastLoadStoreDir;
    }

    public void setLastLoadStoreDir(String lastLoadStoreDir) {
        this.lastLoadStoreDir = lastLoadStoreDir;
    }

    public String getCodexEmail() {
        return codexEmail;
    }

    public void setCodexEmail(String codexEmail) {
        this.codexEmail = codexEmail;
    }

    public String getCodexPassword() {
        return codexPassword;
    }

    public void setCodexPassword(String codexPassword) {
        this.codexPassword = codexPassword;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized CodeEditorTopComponent getDefault() {
        if (instance == null) {
            instance = new CodeEditorTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the CodeEditorTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized CodeEditorTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(CodeEditorTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof CodeEditorTopComponent) {
            return (CodeEditorTopComponent) win;
        }
        Logger.getLogger(CodeEditorTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        p.setProperty("storeDir", lastLoadStoreDir == null ? "" : lastLoadStoreDir);
        p.setProperty("codexEmail", codexEmail == null ? "" : codexEmail);
        p.setProperty("codexPassword", codexPassword == null ? "" : codexPassword);
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        lastLoadStoreDir = p.getProperty("storeDir");
        codexEmail = p.getProperty("codexEmail");
        codexPassword = p.getProperty("codexPassword");
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

//    @Override
//    public Action[] getActions() {
//        return new Action[] {};
//    }
    void installPopup(JEditorPane ce) {
        ce.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    showPopup(evt);
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    showPopup(evt);
                }
            }
        });
    }

    void showPopup(MouseEvent evt) {
        if (popupMenu == null) {
            popupMenu = new CodeEditorPopupMenu();
        }

        popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
    }

    private void onCodePaneAvailable(JEditorPane j, JPanel parentPanel) {
        codeExecSupport = CodeExecutionSupport.initedInstance(j, (Manager) getUndoRedo());
        HistoryTopComponent.findInstance().selectLast();
        tweakActions(j);
        installPopup(j);

        // remove error annotations on the right
        LayoutManager layout = parentPanel.getLayout();
        if (layout instanceof BorderLayout) {
//            Component eastComponent = ((BorderLayout)layout).getLayoutComponent(BorderLayout.EAST);
//            parentPanel.remove(eastComponent);
            parentPanel.add(codeExecSupport.statusStrip(), BorderLayout.EAST);
        }
    }

    private JToolBar getToolbar() {
        return codeExecSupport.toolbar();
    }

    private void tweakActions(JEditorPane ce) {

        ActionMap actionMap = getActionMap();

        ce.addCaretListener(new CaretListener() {

            public void caretUpdate(CaretEvent e) {
                int dot = e.getDot();
                int mark = e.getMark();
                if (dot == mark) {
                    // no selection
                    cutAction.setEnabled(false);
                    copyAction.setEnabled(false);
                } else {
                    cutAction.setEnabled(true);
                    copyAction.setEnabled(true);
                }
            }
        });


        // Find/Replace
        // Don't need to mess with Find action. Already have a good one available
//        Object findKey = SystemAction.get(org.openide.actions.FindAction.class).getActionMapKey();
//        Action findAction = new FindAction();
//        // link Find Menu item to Find action
//        actionMap.put(findKey, findAction);
//        // Enable shortcut
//        KeyStroke ctrlF = Utilities.stringToKey("D-F"); // tight coupling with layer shortcut entry here. Bad!
//        ce.getInputMap().put(ctrlF, findKey);
//        ce.getActionMap().put(findKey, findAction);

        Object replaceKey = SystemAction.get(org.openide.actions.ReplaceAction.class).getActionMapKey();
        Action replaceAction = new ReplaceAction();
        // link Replace Menu item to Replace action
        actionMap.put(replaceKey, replaceAction);
        KeyStroke ctrlR = Utilities.stringToKey("D-R"); // tight coupling with layer shortcut entry here. Bad!
        ce.getInputMap().put(ctrlR, replaceKey);
        ce.getActionMap().put(replaceKey, replaceAction);

        KeyStroke ctrlL = Utilities.stringToKey("D-L");
        Object clearKey = new Object();
        ce.getInputMap().put(ctrlL, clearKey);
        ce.getActionMap().put(clearKey, new ClearCodeEditor());
    }

    void fileOpened(File file) {
        setDisplayName(String.format("Script Editor - %s", file.getName()));
    }

    void fileClosed() {
        setDisplayName("Script Editor");
    }

    static class KojoEditorSupport extends CloneableEditorSupport {

        CodeEditorTopComponent tc;

        public KojoEditorSupport(CloneableEditorSupport.Env env) {
            super(env);
//            setMIMEType(getMimeType());
        }

        public void setTc(CodeEditorTopComponent tc) {
            this.tc = tc;
        }

        @Override
        protected String messageSave() {
            return "Saving...";
        }

        @Override
        protected String messageName() {
            return "Script Editor";
        }

        @Override
        protected String messageToolTip() {
            return "ToolTip!";
        }

        @Override
        protected String messageOpening() {
            return "Opening...";
        }

        @Override
        protected String messageOpened() {
            return "Opened!";
        }

//        private String getMimeType() {
//            return "text/x-scala";
//        }
        @Override
        protected StyledDocument createStyledDocument(EditorKit kit) {
            // see CslEditorKit.createDefaultDocument()
            return new KojoDoc(kit.getContentType(), tc);
        }

        @Override
        protected boolean asynchronousOpen() {
            return true;
        }
    }

    static class KojoEnv implements CloneableEditorSupport.Env {

        public InputStream inputStream() throws IOException {
            return new ByteArrayInputStream(new byte[0]);
        }

        public OutputStream outputStream() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Date getTime() {
            return new java.util.Date();
        }

        public String getMimeType() {
            return "text/x-scala";
        }

        public void addPropertyChangeListener(PropertyChangeListener pl) {
        }

        public void removePropertyChangeListener(PropertyChangeListener pl) {
        }

        public void addVetoableChangeListener(VetoableChangeListener vl) {
        }

        public void removeVetoableChangeListener(VetoableChangeListener vl) {
        }

        public boolean isValid() {
            return true;
        }

        public boolean isModified() {
            return false;
        }

        public void markModified() throws IOException {
        }

        public void unmarkModified() {
        }

        public CloneableOpenSupport findCloneableOpenSupport() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    static class KojoDoc extends NbEditorDocument {

        CodeEditorTopComponent tc;

        public KojoDoc(String mimeType, CodeEditorTopComponent tc) {
            // see GSFDocument ctor
            super(mimeType);
            putProperty("mimeType", mimeType); //NOI18N
            putProperty(org.netbeans.api.lexer.Language.class, org.netbeans.modules.scala.core.lexer.ScalaTokenId.language());
            this.tc = tc;
        }

        public Component createEditor(JEditorPane j) {
            MouseListener[] ml = j.getMouseListeners();
            for (int i = 0; i < ml.length; i++) {
                MouseListener mouseListener = ml[i];
                if (mouseListener instanceof EditorUI) {
                    // remove default popop handler
                    j.removeMouseListener(mouseListener);
                }
            }
            JPanel panel = (JPanel) super.createEditor(j);
            tc.onCodePaneAvailable(j, panel);
            return panel;
        }

        public JToolBar createToolbar(JEditorPane jep) {
            return tc.getToolbar();
        }
    }

    static class CodeEditorPopupMenu extends JPopupMenu {

        public CodeEditorPopupMenu() {
            FileObject configRoot = FileUtil.getConfigRoot();
            addActionMenuItem(configRoot, "Actions/Edit/net-kogics-kojo-LoadFrom.instance");
            addActionMenuItem(configRoot, "Actions/Edit/net-kogics-kojo-Save.instance");
            addActionMenuItem(configRoot, "Actions/Edit/net-kogics-kojo-SaveAs.instance");
            add(new JSeparator());
            final SwitchMode switcher = new SwitchMode();
            final JCheckBoxMenuItem twCb = new JCheckBoxMenuItem(switcher);
            twCb.setText("Switch to Turtle Mode");
            twCb.setToolTipText("Activates Default/Turtle commands and code-completions, and hides Staging and MathWorld commands and code-completions");
            twCb.setActionCommand("Tw");
            add(twCb);
            final JCheckBoxMenuItem stagingCb = new JCheckBoxMenuItem(switcher);
            stagingCb.setText("Switch to Staging Mode");
            stagingCb.setToolTipText("Activates Staging commands and code-completions, and hides Turtle and MathWorld commands and code-completions");
            stagingCb.setActionCommand("Staging");
            add(stagingCb);
            final JCheckBoxMenuItem mwCb = new JCheckBoxMenuItem(switcher);
            mwCb.setText("Switch to MathWorld Mode");
            mwCb.setToolTipText("Activates MathWorld commands and code-completions, and hides Turtle and Staging commands and code-completions");
            mwCb.setActionCommand("Mw");
            add(mwCb);
            add(new JSeparator());
            addMenu(configRoot, "Menu/Edit", "Edit");
            add(new JSeparator());
            addMenu(configRoot, "Menu/Source", "Source");
            add(new JSeparator());
            addPopupPresenterActionMenuItem(configRoot, "Editors/Actions/toggle-line-numbers.instance");
            addFontMenuItem();
            JMenuItem clr = new JMenuItem(new ClearCodeEditor());
            clr.setText("Clear Editor and Close File");
            clr.setAccelerator(Utilities.stringToKey("D-L")); // shows ctrl-l in menu; functionality added in tweakActions
            add(clr);

            addPopupMenuListener(new PopupMenuListener() {

                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    switcher.updateCb(twCb);
                    switcher.updateCb(stagingCb);
                    switcher.updateCb(mwCb);
                }

                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            });
            
        }

        private void addPopupPresenterActionMenuItem(FileObject configRoot, String action) {
            // special handling for 'toggle line numbers' action
            // general purpose version below works for either
            // cut/copy/paste or (if modified) with 'toggle line numbers'
            // but not both!
            try {
                FileObject fo = configRoot.getFileObject(action);
                DataObject dob = DataObject.find(fo);
                InstanceCookie ck = dob.getCookie(InstanceCookie.class);
                Object instanceObj = ck.instanceCreate();

                if (instanceObj instanceof Presenter.Popup) {
                    JMenuItem menuItem = ((Presenter.Popup) instanceObj).getPopupPresenter();
                    if (menuItem != null) {
                        add(menuItem);
                    }
                }
            } catch (Exception ex) {
                ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, ex);
            }
        }

        private void addActionMenuItem(FileObject configRoot, String action) {
            try {
                FileObject fo = configRoot.getFileObject(action);
                DataObject dob = DataObject.find(fo);
                InstanceCookie ck = dob.getCookie(InstanceCookie.class);
                Object instanceObj = ck.instanceCreate();

                JMenuItem menuItem = getPopupMenuItem(instanceObj);
                if (menuItem != null) {
                    add(menuItem);
                }
            } catch (Exception ex) {
                ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, ex);
            }
        }

        private void addMenu(FileObject configRoot, String folderName, String menuName) {
            FileObject fo = configRoot.getFileObject(folderName);
            if (fo == null) {
                return;
            }
            JMenu menu = new JMenu(menuName);
            buildPopupMenu(fo, menu);
            add(menu);
        }

        private void buildPopupMenu(FileObject fo, JComponent comp) {

            DataFolder df = DataFolder.findFolder(fo);
            DataObject[] childs = df.getChildren();
            DataObject dob;
            Object instanceObj;

            for (int i = 0; i < childs.length; i++) {
                dob = childs[i];
                if (dob.getPrimaryFile().isFolder()) {
                    FileObject childFo = childs[i].getPrimaryFile();
                    JMenu menu = new JMenu();
                    Mnemonics.setLocalizedText(menu, dob.getNodeDelegate().getDisplayName());
                    comp.add(menu);
                    buildPopupMenu(childFo, menu);
                } else {
                    //Cookie or Lookup API discovery:
                    InstanceCookie ck = dob.getCookie(InstanceCookie.class);
                    try {
                        instanceObj = ck.instanceCreate();
                    } catch (Exception ex) {
                        instanceObj = null;
                        ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, ex);
                    }
                    if (instanceObj == null) {
                        continue;
                    }
                    if (instanceObj instanceof JSeparator) {
                        comp.add((JSeparator) instanceObj);
                    } else {
                        JMenuItem menuItem = getPopupMenuItem(instanceObj);
                        if (menuItem != null) {
                            comp.add(menuItem);
                        }
                    }

                }
            }
        }

        private JMenuItem getPopupMenuItem(Object instanceObj) {

            if (instanceObj instanceof BooleanStateAction) {
                JMenuItem menuItem = new JCheckBoxMenuItem();
                Actions.connect(menuItem, (Action) instanceObj, true);
                return menuItem;
            }

            if (instanceObj instanceof Action) {
                JMenuItem menuItem = new JMenuItem();
                Actions.connect(menuItem, (Action) instanceObj, true);
                return menuItem;
            }

            if (instanceObj instanceof Presenter.Menu) {
                Action action = ((Presenter.Menu) instanceObj).getMenuPresenter().getAction();
                if (action != null) {
                    JMenuItem menuItem = new JMenuItem();
                    Actions.connect(menuItem, action, true);
                    return menuItem;
                }
            }

//            Does not work for Edit -> cut/copy/paste  
//            if (instanceObj instanceof Presenter.Popup) {
//                // caution: if popupPresenter is added to multiple menus, it will
//                // be visible only in the latest menu. Container.add() removes
//                // child form previous parent
//                return ((Presenter.Popup) instanceObj).getPopupPresenter();
//            }

            return null;
        }

        private void addFontMenuItem() {
            Action action = new AbstractAction("Advanced Options...") {

                public void actionPerformed(ActionEvent e) {
                    OptionsDisplayer.getDefault().open("FontsAndColors");
                }
            };
            JMenuItem item = new JMenuItem(action);

            JMenu experimental = new JMenu("Experimental Features! ");
            experimental.add(item);

            add(experimental);
        }
    }
}
